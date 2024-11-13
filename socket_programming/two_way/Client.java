
import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        try (Socket s = new Socket("localhost", 6666); DataOutputStream dout = new DataOutputStream(s.getOutputStream()); DataInputStream dis = new DataInputStream(s.getInputStream()); BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server.");

            while (true) {
                // Send message to server
                System.out.print("Client: ");
                String messageToServer = reader.readLine();
                dout.writeUTF(messageToServer);

                // Check for exit command from client
                if (messageToServer.equalsIgnoreCase("exit")) {
                    System.out.println("Client disconnected.");
                    break;
                }

                // Read message from server
                String messageFromServer = dis.readUTF();
                System.out.println("Server: " + messageFromServer);

                // Check for exit command from server
                if (messageFromServer.equalsIgnoreCase("exit")) {
                    System.out.println("Server disconnected.");
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
