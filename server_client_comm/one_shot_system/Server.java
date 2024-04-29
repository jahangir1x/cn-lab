package one_shot_system;
import java.io.DataInputStream;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server is running...");
        ServerSocket serverSocket = new ServerSocket(7777);
        DataInputStream dis = new DataInputStream(serverSocket.accept().getInputStream());
        System.out.println("Message: " + (String) dis.readUTF());
        serverSocket.close();
    }
}