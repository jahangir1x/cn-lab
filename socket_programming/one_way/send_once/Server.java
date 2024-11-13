
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {
        try (ServerSocket ss = new ServerSocket(7777)) {
            Socket s1 = ss.accept();
            DataInputStream ds1 = new DataInputStream(s1.getInputStream());
            String msg = ds1.readUTF();
            System.out.println("Message---- " + msg);
        }

    }

}
