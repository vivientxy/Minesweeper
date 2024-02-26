package client;

import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) {

        // default host and port
        String host = "localhost";
        int port = 12345;

        // take in arguments
        if (args.length > 1) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        try (Socket socket = new Socket(host, port)) {
            // connected to socket, networkIO here
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            boolean gameOn = true;
            Console cons = System.console();
            while (gameOn) {
                String command = cons.readLine();

                // write command to server and read server's response
                dos.writeUTF(command);
                dos.flush();
                dis.readUTF();
            }
            dos.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
