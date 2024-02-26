package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    public static void main(String[] args) {
        
        // default port
        int port = 12345;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket ss = new ServerSocket(port)) {
            ExecutorService threadPool = Executors.newFixedThreadPool(1);
            while (true) {
                System.out.println("Listening for client connection...");
                Socket socket = ss.accept();
                System.out.println("Connected!");
                MinecraftEngine engine = new MinecraftEngine(socket);
                threadPool.submit(engine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
