package server;

import java.net.Socket;

public class MinecraftEngine implements Runnable {
    private Socket socket;  // for IO at the socket

    public MinecraftEngine(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
