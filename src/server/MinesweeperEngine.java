package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MinesweeperEngine implements Runnable {
    private Socket socket; // for IO at the socket

    public MinesweeperEngine(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            boolean gameOn = true;
            dos.writeUTF("Welcome to Minesweeper. Please select your difficulty level: \n\t- 1) EASY\n\t- 2) MEDIUM\n\t- 3) HARD");
            dos.flush();
            String difficultyLevel = dis.readUTF();
            Difficulty difficulty = null;
            switch (difficultyLevel) {
                case "1":
                    difficulty = Difficulty.EASY;
                    break;
                case "2":
                    difficulty = Difficulty.MEDIUM;
                    break;
                case "3":
                    difficulty = Difficulty.HARD;
                    break;
                default:
                    break;
            }
            Board board = new Board(difficulty);
            while (gameOn) {
                String command = dis.readUTF();

            }

            dos.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
