
import java.io.*;
import java.util.*;
import javax.net.ssl.*;

class MailSender {

    private static DataOutputStream dos;
    private static BufferedReader br;

    public static void main(String[] args) {
        // Use environment variables for sensitive information
        String user = "s2011076129@ru.ac.bd";
        String pass = "weak_password";
        String recipient = "jahangir64r@gmail.com";

        String username = Base64.getEncoder().encodeToString(user.getBytes());
        String password = Base64.getEncoder().encodeToString(pass.getBytes());

        try (SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket("smtp.gmail.com", 465)) {
            dos = new DataOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            readResponse();  // Read initial server response

            // SMTP commands
            sendCommand("EHLO smtp.gmail.com");
            sendCommand("AUTH LOGIN");
            sendCommand(username);
            sendCommand(password);
            // sendCommand("MAIL FROM:<" + user + ">");
            // sendCommand("RCPT TO:<" + recipient + ">");
            // sendCommand("DATA");

            // // Email headers and body
            // sendRaw("From: " + user);
            // sendRaw("To: " + recipient);
            // sendRaw("Subject: Email test");
            // sendRaw("");  // Blank line between headers and body
            // sendRaw("This is a test email. Thank you.");
            // sendCommand(".");  // End of DATA command
            // sendCommand("QUIT");
            // System.out.println("Email sent successfully!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Sends a command and reads server response
    private static void sendCommand(String command) throws IOException {
        dos.writeBytes(command + "\r\n");
        Thread.sl System
        .out.println("CLIENT: " + command);
        readResponse();
    }

    // Sends raw email content without reading the response
    private static void sendRaw(String data) throws IOException {
        dos.writeBytes(data + "\r\n");
        dos.flush();
        System.out.println("CLIENT: " + data);
    }

    // Reads server response line by line
    private static void readResponse() throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("SERVER: " + line);
        }
    }
}
