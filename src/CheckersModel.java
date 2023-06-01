package org.cis120.checkers;

import java.io.*;
import java.util.LinkedList;

/**
 * This class is a model for Checkers.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games.
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 *
 */
public class CheckersModel {

    private int[][] board;
    private boolean player1 = true;
    private boolean gameOver = false;

    private LinkedList<int[][]> undoList = new LinkedList<>();
    private LinkedList<Boolean> undoPlayers = new LinkedList<>();

    private boolean isJumpAgain = false;
    private int jumpAgainRow, jumpAgainCol;

    // change these if you want to save/load to a different file
    static final String PATH_TO_LOAD_GAME = "files/savedCheckers.txt";
    static final String PATH_TO_SAVE_GAME = "files/savedCheckers.txt";

    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int RED = 2;
    public static final int BLACK_KING = 3;
    public static final int RED_KING = 4;


    /**
     * Constructor sets up game state.
     */
    public CheckersModel() {
        reset();
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[8][8];
        player1 = true;
        undoList = new LinkedList<>();
        undoPlayers = new LinkedList<>();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = EMPTY;
                if (row == 0 || row == 2) {
                    if (col % 2 == 1) {
                        board[row][col] = BLACK;
                    }
                } else if (row == 1) {
                    if (col % 2 == 0) {
                        board[row][col] = BLACK;
                    }
                } else if (row == 5 || row == 7) {
                    if (col % 2 == 0) {
                        board[row][col] = RED;
                    }
                } else if (row == 6) {
                    if (col % 2 == 1) {
                        board[row][col] = RED;
                    }
                }
            }
        }
        addUndoStep();
    }

    // helper function to copy current state of board
    private int[][] copyBoard(int[][] currBoard) {
        int[][] temp = new int[8][8];
        for (int row = 0; row < 8; row++) {
            System.arraycopy(currBoard[row], 0, temp[row], 0, 8);
        }
        return temp;
    }

    public int getPiece(int row, int col) {
        return board[row][col];
    }

    public boolean getCurrentPlayer() {
        return player1;
    }

    public boolean getIsJumpAgain() {
        return isJumpAgain;
    }

    public boolean getIsGameOver() {
        return gameOver;
    }

    public void setBoard(int[][] newBoard) {
        for (int row = 0; row < 8; row++) {
            System.arraycopy(newBoard[row], 0, board[row], 0, 8);
        }
    }

    public int checkWinner() {

        if (!anyLegalMoves(player1)) {
            gameOver = true;
            if (player1) {
                return 2;
            } else {
                return 1;
            }
        }

        int blackLeft = 0;
        int redLeft = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == RED || board[row][col] == RED_KING) {
                    redLeft++;
                } else if (board[row][col] == BLACK || board[row][col] == BLACK_KING) {
                    blackLeft++;
                }
            }
        }
        if (redLeft == 0) {
            gameOver = true;
            return 1;
        } else if (blackLeft == 0) {
            gameOver = true;
            return 2;
        }
        return 0;
    }

    public void moveOrJump(int startRow, int startCol, int endRow, int endCol) {
        if (canMove(startRow, startCol, endRow, endCol)) {

            int curr = board[startRow][startCol];

            board[startRow][startCol] = EMPTY;
            board[endRow][endCol] = curr;
            checkPromotion();
            player1 = !player1;
            addUndoStep();
            checkWinner();

        } else if (canJump(startRow, startCol, endRow, endCol)) {

            int curr = board[startRow][startCol];
            int middleRow = (startRow + endRow) / 2;
            int middleCol = (startCol + endCol) / 2;

            board[startRow][startCol] = EMPTY;
            board[endRow][endCol] = curr;
            board[middleRow][middleCol] = EMPTY;

            jumpAgainRow = endRow;
            jumpAgainCol = endCol;

            if (!canJumpAgain(endRow, endCol)) {
                player1 = !player1;
                isJumpAgain = false;
            } else {
                isJumpAgain = true;
            }

            checkPromotion();

            addUndoStep();
            checkWinner();
        }
    }

    // helper function to add current board state and player turn to LinkedList for undo feature
    private void addUndoStep() {
        undoList.add(copyBoard(board));
        if (player1) {
            undoPlayers.add(true);
        } else {
            undoPlayers.add(false);
        }
    }

    // helper function to check if any piece on the board should be promoted to king
    private void checkPromotion() {
        for (int col = 0; col < 8; col++) {
            if (board[0][col] == RED) {
                board[0][col] = RED_KING;
                isJumpAgain = false;
            }
            if (board[7][col] == BLACK) {
                board[7][col] = BLACK_KING;
                isJumpAgain = false;
            }
        }
    }

    private boolean canMove(int startRow, int startCol, int endRow, int endCol) {

        // if player is in the middle of a double jump, he must do second jump
        if (isJumpAgain) {
            if (startRow != jumpAgainRow || startCol != jumpAgainCol) {
                return false;
            }
        }

        int startPiece = board[startRow][startCol];

        if (startPiece == EMPTY) {
            return false;
        }

        if (endRow >= 8 || endCol >= 8 || endRow < 0 || endCol < 0) {
            return false;
        }

        if (board[endRow][endCol] != EMPTY) {
            return false;
        }

        if (player1) {
            if (startPiece == RED || startPiece == RED_KING) {
                return false;
            }
        } else {
            if (startPiece == BLACK || startPiece == BLACK_KING) {
                return false;
            }
        }

        boolean blackMove = endRow == startRow + 1 &&
                (endCol == startCol - 1 || endCol == startCol + 1);
        boolean redMove = endRow == startRow - 1 &&
                (endCol == startCol - 1 || endCol == startCol + 1);

        if (startPiece == BLACK) {
            return blackMove;
        } else if (startPiece == RED) {
            return redMove;
        } else { // if start piece is king
            return blackMove || redMove;
        }
    }

    private boolean canJump(int startRow, int startCol, int endRow, int endCol) {

        // if player is in the middle of a double jump, he must do second jump
        if (isJumpAgain) {
            if (startRow != jumpAgainRow || startCol != jumpAgainCol) {
                return false;
            }
        }

        if (endRow >= 8 || endCol >= 8 || endCol < 0 || endRow < 0) {
            return false;
        }
        int startPiece = board[startRow][startCol];
        if (startPiece == EMPTY) {
            return false;
        }
        if (board[endRow][endCol] != EMPTY) {
            return false;
        }

        if (player1) {
            if (startPiece == RED || startPiece == RED_KING) {
                return false;
            }
        } else {
            if (startPiece == BLACK || startPiece == BLACK_KING) {
                return false;
            }
        }

        if (startPiece == BLACK) {
            if (endRow == startRow + 2 && endCol == startCol - 2 &&
                    (board[startRow + 1][startCol - 1] == RED ||
                            board[startRow + 1][startCol - 1] == RED_KING)) {
                return true;
            }
            if (endRow == startRow + 2 && endCol == startCol + 2 &&
                    (board[startRow + 1][startCol + 1] == RED ||
                            board[startRow + 1][startCol + 1] == RED_KING)) {
                return true;
            }
        } else if (startPiece == RED) {
            if (endRow == startRow - 2 && endCol == startCol - 2 &&
                    (board[startRow - 1][startCol - 1] == BLACK ||
                            board[startRow - 1][startCol - 1] == BLACK_KING)) {
                return true;
            }
            if (endRow == startRow - 2 && endCol == startCol + 2 &&
                    (board[startRow - 1][startCol + 1] == BLACK ||
                            board[startRow - 1][startCol + 1] == BLACK_KING)) {
                return true;
            }
        } else if (startPiece == BLACK_KING) {
            if (endRow == startRow + 2 && endCol == startCol - 2 &&
                    (board[startRow + 1][startCol - 1] == RED ||
                            board[startRow + 1][startCol - 1] == RED_KING)) {
                return true;
            }
            if (endRow == startRow + 2 && endCol == startCol + 2 &&
                    (board[startRow + 1][startCol + 1] == RED ||
                            board[startRow + 1][startCol + 1] == RED_KING)) {
                return true;
            }
            if (endRow == startRow - 2 && endCol == startCol - 2 &&
                    (board[startRow - 1][startCol - 1] == RED ||
                            board[startRow - 1][startCol - 1] == RED_KING)) {
                return true;
            }
            if (endRow == startRow - 2 && endCol == startCol + 2 &&
                    (board[startRow - 1][startCol + 1] == RED ||
                            board[startRow - 1][startCol + 1] == RED_KING)) {
                return true;
            }
        } else if (startPiece == RED_KING) {
            if (endRow == startRow - 2 && endCol == startCol - 2 &&
                    (board[startRow - 1][startCol - 1] == BLACK ||
                            board[startRow - 1][startCol - 1] == BLACK_KING)) {
                return true;
            }
            if (endRow == startRow - 2 && endCol == startCol + 2 &&
                    (board[startRow - 1][startCol + 1] == BLACK ||
                            board[startRow - 1][startCol + 1] == BLACK_KING)) {
                return true;
            }
            if (endRow == startRow + 2 && endCol == startCol - 2 &&
                    (board[startRow + 1][startCol - 1] == BLACK ||
                            board[startRow + 1][startCol - 1] == BLACK_KING)) {
                return true;
            }
            if (endRow == startRow + 2 && endCol == startCol + 2 &&
                    (board[startRow + 1][startCol + 1] == BLACK ||
                            board[startRow + 1][startCol + 1] == BLACK_KING)) {
                return true;
            }

        }
        return false;
    }

    private boolean canJumpAgain(int startRow, int startCol) {

        int startPiece = board[startRow][startCol];

        if (startPiece == BLACK) {
            return canJump(startRow, startCol, startRow + 2, startCol - 2) ||
                    canJump(startRow, startCol, startRow + 2, startCol + 2);
        } else if (startPiece == RED) {
            return canJump(startRow, startCol, startRow - 2, startCol - 2) ||
                    canJump(startRow, startCol, startRow - 2, startCol + 2);
        } else if (startPiece == BLACK_KING || startPiece == RED_KING) {
            return canJump(startRow, startCol, startRow + 2, startCol - 2) ||
                    canJump(startRow, startCol, startRow + 2, startCol + 2) ||
                    canJump(startRow, startCol, startRow - 2, startCol + 2) ||
                    canJump(startRow, startCol, startRow - 2, startCol - 2);
        }
        return false;
    }

    public boolean anyLegalMoves(boolean player) {
        if (player) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == BLACK || board[row][col] == BLACK_KING) {
                        if (canJumpAgain(row, col)) {
                            return true;
                        }
                    }
                    if (board[row][col] == BLACK) {
                        if (canMove(row, col, row + 1, col - 1) ||
                                canMove(row, col, row + 1, col + 1)) {
                            return true;
                        }
                    }
                    if (board[row][col] == BLACK_KING) {
                        if (canMove(row, col, row + 1, col - 1) ||
                                canMove(row, col, row + 1, col + 1) ||
                                canMove(row, col, row - 1, col + 1) ||
                                canMove(row, col, row - 1, col - 1)) {
                            return true;
                        }
                    }
                }
            }
        } else {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == RED || board[row][col] == RED_KING) {
                        if (canJumpAgain(row, col)) {
                            return true;
                        }
                    }
                    if (board[row][col] == RED) {
                        if (canMove(row, col, row - 1, col - 1) ||
                                canMove(row, col, row - 1, col + 1)) {
                            return true;
                        }
                    }
                    if (board[row][col] == RED_KING) {
                        if (canMove(row, col, row + 1, col - 1) ||
                                canMove(row, col, row + 1, col + 1) ||
                                canMove(row, col, row - 1, col + 1) ||
                                canMove(row, col, row - 1, col - 1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void undo() {
        if (undoList.size() <= 1) {
            return;
        }
        undoList.removeLast();
        undoPlayers.removeLast();
        isJumpAgain = false;
        player1 = undoPlayers.getLast();
        int[][] temp = undoList.getLast();
        setBoard(temp);
    }

    public void saveGame() {
        StringBuilder s = new StringBuilder();

        if (player1) {
            s.append("1");
            s.append("\n");
        } else {
            s.append("2");
            s.append("\n");
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                s.append(board[row][col]).append(" ");
            }
            s.append("\n");
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_TO_SAVE_GAME));
            bw.write(s.toString());
            bw.close();
        } catch (Exception e) {
            System.out.println("File not found or is invalid");
        }
    }

    public void loadGame() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(PATH_TO_LOAD_GAME));
            String line = "";

            String firstLine = br.readLine();
            if (Integer.parseInt(firstLine) == 1) {
                player1 = true;
            } else if (Integer.parseInt(firstLine) == 2) {
                player1 = false;
            } else {
                throw new IllegalArgumentException();
            }

            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(" ");
                for (int col = 0; col < 8; col++) {
                    String c = cols[col];
                    board[row][col] = Integer.parseInt(c);
                }
                row++;
            }
            br.close();
            undoList = new LinkedList<>();
            undoPlayers = new LinkedList<>();
            addUndoStep();
        } catch (Exception e) {
            System.out.println("File not found or is invalid");
        }
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     */
    public static void main(String[] args) {
        CheckersModel cm = new CheckersModel();
    }
}
