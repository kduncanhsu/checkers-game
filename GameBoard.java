package org.cis120.checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class instantiates a CheckersModel object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */

public class GameBoard extends JPanel {

    private CheckersModel cm; // model for the game
    private JLabel status; // current status text

    private Point prev;

    // Game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        cm = new CheckersModel(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                int c = p.x / 100;
                int r = p.y / 100;

                if (c >= 0 && c < 8 && r >= 0 && r < 8) {
                    handleSelectedSquare(r, c);
                }

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    private void handleSelectedSquare(int r, int c) {

        if (!cm.getIsJumpAgain()) {
            boolean blacksTurn = (cm.getPiece(r, c) == CheckersModel.BLACK ||
                    cm.getPiece(r, c) == CheckersModel.BLACK_KING) && cm.getCurrentPlayer();

            boolean redsTurn = (cm.getPiece(r, c) == CheckersModel.RED ||
                    cm.getPiece(r, c) == CheckersModel.RED_KING) && !cm.getCurrentPlayer();

            if (blacksTurn || redsTurn) {
                prev = new Point(r, c);
            }
        }

        if (cm.getPiece(r, c) == CheckersModel.EMPTY && prev != null) {
            cm.moveOrJump(prev.x, prev.y, r, c);
            if (cm.getIsJumpAgain()) {
                prev = new Point(r, c);
            }
        }
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (cm.getCurrentPlayer()) {
            status.setText("BLACK's Turn");
        } else {
            status.setText("RED's Turn");
        }

        int winner = cm.checkWinner();
        if (winner == 1) {
            status.setText("BLACK wins!!!");
        } else if (winner == 2) {
            status.setText("RED wins!!!");
        }
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        cm.reset();
        status.setText("BLACK's Turn");
        prev = null;
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Undo('s) the last move.
     */
    public void undo() {
        cm.undo();
        prev = null;
        updateStatus();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Load's saved game
     */
    public void loadGame() {
        cm.loadGame();
        updateStatus();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Save's current game
     */
    public void saveGame() {
        cm.saveGame();
        updateStatus();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw board base
        g.setColor(new Color(175, 128, 79)); // dark beige
        g.fillRect(0, 0, 800, 800);

        // Draws checker pieces, board squares, and highlight selected pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // draw board squares
                if (row % 2 == col % 2) {
                    g.setColor(new Color(225, 198, 153)); // beige
                    g.fillRect(col * 100, row * 100, 100, 100);
                }
                // highlight selected piece
                if (prev != null) {
                    if (cm.getPiece(row, col) != CheckersModel.EMPTY) {
                        if (row == prev.x && col == prev.y) {
                            g.setColor(new Color(252, 247, 135)); // light yellow
                            g.fillRect(col * 100, row * 100, 100, 100);
                        }
                    }
                }
                // draw checker pieces
                if (cm.getPiece(row, col) == CheckersModel.BLACK) {
                    g.setColor(Color.DARK_GRAY); // dark gray
                    g.fillOval(15 + col * 100, 15 + row * 100, 70, 70);
                }
                if (cm.getPiece(row, col) == CheckersModel.RED) {
                    g.setColor(new Color(202, 52, 51)); // light red
                    g.fillOval(15 + col * 100, 15 + row * 100, 70, 70);
                }
                if (cm.getPiece(row, col) == CheckersModel.BLACK_KING) {
                    g.setColor(Color.DARK_GRAY); // dark gray
                    g.fillOval(15 + col * 100, 15 + row * 100, 70, 70);
                    // kings denoted by another small circle on top left corner of checker piece
                    g.fillOval(15 + col * 100, 15 + row * 100, 20, 20);
                }
                if (cm.getPiece(row, col) == CheckersModel.RED_KING) {
                    g.setColor(new Color(202, 52, 51)); // light red
                    g.fillOval(15 + col * 100, 15 + row * 100, 70, 70);
                    g.fillOval(15 + col * 100, 15 + row * 100, 20, 20);
                }

            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
