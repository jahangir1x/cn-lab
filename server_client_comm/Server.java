import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server is running...");
        // create a server that listens on port 7777
        // and sends hi when a client sends hello, abc when def, zxy when zyx
        // and closes the connection when client sends quit
        ServerSocket serverSocket = new ServerSocket(7777);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            String message = dis.readUTF();
            if (message.equals("hello\n")) {
                System.out.println("Message: hi");
            } else if (message.equals("def\n")) {
                System.out.println("Message: abc");
            } else if (message.equals("zyx\n")) {
                System.out.println("Message: zxy");
            } else if (message.equals("quit\n")) {
                clientSocket.close();
                break;
            }
        }
        serverSocket.close();
    }
}
