package one_shot_system;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 7777);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF("Hello, Server!");
        dos.writeUTF("Hello, Server 2!");
        socket.close();
    }
}
