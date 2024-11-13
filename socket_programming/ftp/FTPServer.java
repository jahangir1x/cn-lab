
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class FTPServer {

    private static final String SERVER_DIR = "./FTP_Server_Directory";
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException {
        new File(SERVER_DIR).mkdirs();
        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            System.out.println("Server Started...");

            try (Socket socket = serverSocket.accept()) {
                System.out.println("Connected with Client...");
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String command = dis.readUTF().trim();
                    System.out.print("Client Command: " + (command.isEmpty() ? "<Blank>" : command));

                    if (command.equals("exit")) {
                        System.out.println("Client Disconnected.");
                        break;
                    } else if (command.equals("ls")) {
                        listFiles(dos);
                    } else if (command.startsWith("get ")) {
                        sendFile(dos, command.substring(4).trim());
                    } else if (command.startsWith("put ")) {
                        receiveFile(dis, command.substring(4).trim());
                    } else {
                        dos.writeUTF("Invalid Command");
                        System.out.println("Invalid Command received from Client.");
                    }
                }
            }
        }
    }

    public static void listFiles(DataOutputStream dos) throws IOException {
        File directory = new File(SERVER_DIR);
        File[] files = directory.listFiles();
        StringBuilder fileList = new StringBuilder();

        if (files != null) {
            for (File file : files) {
                fileList.append(file.getName())
                        .append(file.isFile() ? "" : " (dir)")
                        .append("\n");
            }
        }
        dos.writeUTF(fileList.toString());
        dos.flush();
    }

    public static void sendFile(DataOutputStream dos, String fileName) throws IOException {
        File file = new File(SERVER_DIR, fileName);

        try (FileInputStream fis = new FileInputStream(file)) {
            dos.writeInt(2);
            dos.writeLong(file.length());

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }

            dos.flush();
            System.out.println("File sent successfully: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            sendError(dos, "File not found: " + fileName);
        }
    }

    public static void receiveFile(DataInputStream dis, String fileName) throws IOException {
        File file = new File(SERVER_DIR, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            long fileSize = dis.readLong();
            long bytesReceived = 0;
            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];

            while (bytesReceived < fileSize && (bytesRead = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                bytesReceived += bytesRead;
            }

            fos.flush();
            System.out.println("File received successfully: " + fileName);
        }
    }

    private static void sendError(DataOutputStream dos, String message) throws IOException {
        dos.writeInt(0);
        dos.writeUTF(message);
        dos.flush();
    }
}
