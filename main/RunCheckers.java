import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. 
 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a Checkers object to serve as the game's model.
 */
public class RunCheckers implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Checkers");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton save = new JButton("Save");
        save.addActionListener(e -> board.saveGame());
        control_panel.add(save);

        final JButton load = new JButton("Load");
        load.addActionListener(e -> board.loadGame());
        control_panel.add(load);

        String instructionMessage = "For this project I chose to implement a classic " +
                "game of checkers!\n" +
                "\n" +
                "RULES OF CHECKERS:\n" +
                "- Each player begins with 12 discs/pieces \n" +
                "- Alternating turns, and black opens the game\n" +
                "- Pieces move diagonally by one space (if not capturing) and regular pieces are " +
                "limited to only forward moves\n" +
                "- To capture an opponent’s piece, your piece leaps over the opponent’s " +
                "and lands on the straight diagonal on the other side \n" +
                "- If a piece can perform a second capture after a jump/capture, " +
                "it MUST perform the second jump (unless immediately after promotion)\n" +
                "- When a piece reaches the other end of the board, the piece is upgraded to a " +
                "king (denoted by small second circle in the top left of piece)\n" +
                "- Same rules apply to kings but they can move in both directions\n" +
                "- The first player to have all their pieces captured OR have no possible " +
                "moves left LOSES the game\n" +
                "\n" +
                "FEATURES I ADDED:\n" +
                "- game does not allow the player to make illegal moves (including " +
                "requiring double jumping where possible)\n" +
                "- highlights the piece which user has selected (and is about to move)\n" +
                "- the ability to pause the game (via saving/loading to and from a text file)\n" +
                "- undo feature, where the user has unlimited undo until the start of the " +
                "game (or when the game was loaded in)";

        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e ->
                JOptionPane.showMessageDialog(frame, instructionMessage, "Instructions", 2));
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
