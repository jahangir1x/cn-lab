import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        // create a socket that connects to localhost on port 7777
        // and sends hello, def, zyx, quit
        // and prints the response from the server
        // and closes the connection
        Socket socket = new Socket("localhost", 7777);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        // Send each message separately with a newline delimiter
        // dos.writeUTF("hello\n");
        // Thread.sleep(10000);
        dos.writeUTF("def\n");
        Thread.sleep(10000);
        dos.writeUTF("zyx\n");
        Thread.sleep(10000);
        dos.writeUTF("quit\n");

        socket.close();
    }
}
