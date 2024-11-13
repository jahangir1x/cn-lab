
import java.io.*;
import java.net.Socket;

public class FTPClient {

    private static final String CLIENT_DIR = "./FTP_Client_Directory";
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException {
        new File(CLIENT_DIR).mkdirs();

        try (Socket socket = new Socket("localhost", 6666); DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); DataInputStream dis = new DataInputStream(socket.getInputStream()); BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to Server...");

            while (true) {
                System.out.print("> ");
                String command = reader.readLine().trim();
                dos.writeUTF(command);

                if ("exit".equals(command)) {
                    System.out.println("Disconnected from server.");
                    break;
                } else if ("ls".equals(command)) {
                    listServerFiles(dis);
                } else if (command.startsWith("get ")) {
                    String fileName = command.substring(4).trim();
                    receiveFile(dis, fileName);
                } else if (command.startsWith("put ")) {
                    String fileName = command.substring(4).trim();
                    sendFile(dos, fileName);
                } else {
                    System.out.println("Invalid command.");
                }
            }
        }
    }

    private static void listServerFiles(DataInputStream dis) throws IOException {
        String fileList = dis.readUTF();
        System.out.println("Files on Server:\n" + fileList);
    }

    private static void sendFile(DataOutputStream dos, String fileName) throws IOException {
        File file = new File(CLIENT_DIR, fileName);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File not found: " + fileName);
            dos.writeInt(0);  // Error condition for file not found
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            dos.writeInt(2);  // File found
            dos.writeLong(file.length());  // Send file size

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            dos.flush();
            System.out.println("File sent successfully: " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to send file: " + e.getMessage());
        }
    }

    private static void receiveFile(DataInputStream dis, String fileName) {
        try {
            int condition = dis.readInt();
            if (condition == 0) {
                System.out.println("File not found on server.");
                return;
            } else if (condition == 1) {
                System.out.println("Cannot receive: " + fileName + " is a directory.");
                return;
            }

            long fileSize = dis.readLong();  // Receive file size
            File file = new File(CLIENT_DIR, fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                long bytesReceived = 0;
                int bytesRead;

                while (bytesReceived < fileSize && (bytesRead = dis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    bytesReceived += bytesRead;
                }
                fos.flush();
                System.out.println("File received successfully: " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Failed to receive file: " + e.getMessage());
        }
    }
}
