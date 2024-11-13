
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {
        try (Socket s2 = new Socket("localhost", 7777)) {
            DataOutputStream ds2 = new DataOutputStream(s2.getOutputStream());
            ds2.writeUTF("Hello Bangladesh");
        }

    }
}
