
import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(6666); Socket s = ss.accept(); DataInputStream dis = new DataInputStream(s.getInputStream()); DataOutputStream dos = new DataOutputStream(s.getOutputStream()); BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Client connected.");

            while (true) {
                // Read message from client
                String messageFromClient = dis.readUTF();
                System.out.println("Client: " + messageFromClient);

                // Check for exit command from client
                if (messageFromClient.equals("exit")) {
                    System.out.println("Client disconnected.");
                    break;
                }

                // Send message to client
                System.out.print("Server: ");
                String messageToClient = reader.readLine();
                dos.writeUTF(messageToClient);

                // Check for exit command from server
                if (messageToClient.equalsIgnoreCase("exit")) {
                    System.out.println("Server disconnected.");
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
