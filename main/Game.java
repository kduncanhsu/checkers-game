import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game. Initializes the runnable game
     * class and runs it.
     */
    public static void main(String[] args) {
        Runnable game = new org.cis120.checkers.RunCheckers(); 
        
        SwingUtilities.invokeLater(game);
    }
}
