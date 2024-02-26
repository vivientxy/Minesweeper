package server;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board {
    private Difficulty difficulty;
    private int[][] board;
    private int[][] overlayBoard;

    public static void main(String[] args) {

        // int[] array = new int[] {1,2,3,4,5,6,7,8,9,0};
        // System.out.println(Arrays.toString(array));

        Board bbbbb = new Board(Difficulty.HARD);
        bbbbb.printBoard();

        System.out.println("---- selection ----");
        bbbbb.selectCell(1, 1, -2);
        bbbbb.printOverlayBoard();

    }

    /**
     * 9 = bomb
     * 0 = empty cell (unveil surrounding empty cells / number cells)
     * 1 to 8 = number cells indicating how many adjacent bombs there are
     * -1 = unopened
     * -2 = opened
     * -3 = flagged
     */

    public Board(Difficulty difficulty) {
        this.difficulty = difficulty;
        int rows = 0;
        int columns = 0;
        switch (difficulty) {
            case EASY:
                rows = 10;
                break;
            case MEDIUM:
                rows = 15;
                break;
            case HARD:
                rows = 20;
                break;
            default:
                break;
        }
        columns = rows; // square boards
        this.board = new int[rows][columns];
        generateOverlayBoard(rows, columns);
        populateMines();
        populateNumberCells();
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    private void generateOverlayBoard(int rows, int columns) {
        this.overlayBoard = new int[rows][columns];
        for (int row = 0; row < rows; row++) {
            Arrays.fill(this.overlayBoard[row], -1);
        }
    }

    private void populateMines() {
        double mineDensity = 0.2; // 20% density of mines per row
        int rows = this.board.length;
        int columns = this.board[0].length;
        Random rand = new Random();
        int minesPerRow = (int) (columns * mineDensity);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < minesPerRow; j++) {
                int mine = rand.nextInt(columns);
                while (this.board[i][mine] != 0) {
                    mine = rand.nextInt(columns);
                }
                this.board[i][mine] = 9;
            }
        }
    }

    private void populateNumberCells() {
        int rows = this.board.length;
        int columns = this.board[0].length;
        for (int i = 0; i < rows; i++) { // row
            for (int j = 0; j < columns; j++) { // column
                // skip bombs
                if (this.board[i][j] != 9) {
                    int bombCount = 0;
                    // check surrounding for 9's
                    for (int r = i - 1; r < i + 2; r++) {
                        for (int c = j - 1; c < j + 2; c++) {
                            if (r >= 0 && r < rows && c >= 0 && c < columns && board[r][c] == 9) {
                                bombCount++;
                            } // if r or c < 0, skip
                        }
                    }
                    this.board[i][j] = bombCount;
                }
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < this.board.length; i++) {
            System.out.println(Arrays.toString(this.board[i]));
        }
    }

    public void printOverlayBoard() {
        for (int i = 0; i < this.overlayBoard.length; i++) {
            System.out.println(Arrays.toString(this.overlayBoard[i]));
        }
    }

    public boolean isBombDetonated() {
        boolean bombDetonated = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (this.overlayBoard[i][j] == 9) {
                    bombDetonated = true;
                }
            }
        }
        return bombDetonated;
    }

    private String toCoordinates(int row, int column) {
        StringBuilder sb = new StringBuilder();
        return sb.append(row).append("-").append(column).toString();
    }

    private int getRow(String coordinates) {
        String[] parsedCoordinates = coordinates.split("-");
        return Integer.parseInt(parsedCoordinates[0]);
    }

    private int getColumn(String coordinates) {
        String[] parsedCoordinates = coordinates.split("-");
        return Integer.parseInt(parsedCoordinates[1]);
    }

    public void selectCell(int row, int column, int action) {
        // action -2 = open, -3 = flag
        if (action == -3) {
            // RIGHT CLICK: check if it's an unopened cell. if yes, flag it
            if (this.overlayBoard[row][column] == -1) {
                this.overlayBoard[row][column] = -3;
            }
            // RIGHT CLICK: check if it's an existing flag. if yes, unflag it
            else if (this.overlayBoard[row][column] == -3) {
                this.overlayBoard[row][column] = -1;
            }
        }

        if (action == -2) {
            // LEFT CLICK: only unopened cells can be opened (flagged and already opened
            // cells = no action)
            if (this.overlayBoard[row][column] == -1) {
                this.overlayBoard[row][column] = this.board[row][column];
                // if opened an empty cell, then open surrounding non-9 cells (exclude
                // diagonals)
                if (this.board[row][column] == 0) {
                    List<String> checkZeros = new ArrayList<>();
                    checkZeros.add(toCoordinates(row, column));
                    for (int i = 0; i < checkZeros.size(); i++) {
                        int tempRow = getRow(checkZeros.get(i));
                        int tempColumn = getColumn(checkZeros.get(i));
                        int r;
                        int c;
                        if (tempRow > 0) { // check top
                            r = tempRow - 1;
                            c = tempColumn;
                            this.overlayBoard[r][c] = this.board[r][c];
                            if (this.board[r][c] == 0 && !checkZeros.contains(toCoordinates(r, c))) {
                                checkZeros.add(toCoordinates(r, c));
                            }
                        }
                        if (tempRow < this.board.length - 1) { // check bottom
                            r = tempRow + 1;
                            c = tempColumn;
                            this.overlayBoard[r][c] = this.board[r][c];
                            if (this.board[r][c] == 0 && !checkZeros.contains(toCoordinates(r, c))) {
                                checkZeros.add(toCoordinates(r, c));
                            }
                        }
                        if (tempColumn > 0) { // check left
                            r = tempRow;
                            c = tempColumn - 1;
                            this.overlayBoard[r][c] = this.board[r][c];
                            if (this.board[r][c] == 0 && !checkZeros.contains(toCoordinates(r, c))) {
                                checkZeros.add(toCoordinates(r, c));
                            }
                        }
                        if (tempColumn < this.board[0].length - 1) { // check right
                            r = tempRow;
                            c = tempColumn + 1;
                            this.overlayBoard[r][c] = this.board[r][c];
                            if (this.board[r][c] == 0 && !checkZeros.contains(toCoordinates(r, c))) {
                                checkZeros.add(toCoordinates(r, c));
                            }
                        }

                    }
                }
            }
        }

    }

}