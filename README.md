# Checkers Game

_CheckersModel:_ This class maintains the current state of the board. It contains all the logic for whether a move/jump is valid, and the game has ended/winner is found. It stores the LinkedList of all versions of the board as well as the method for the undo feature. It also contains the methods for saving and loading the game.

_GameBoard:_ This class instantiates a CheckersModel object, which is the model for the game. This class handles user clicks–the model is updated every time the user clicks the game board. Whenever the model is updated, the game board repaints itself and updates the message at the bottom of the window.

_RunCheckers:_ This class instantiates a GameBoard object, and sets up the top-level frame and widgets for the GUI. This class implements controller functionality with the various buttons such as Reset, Undo, Save, Load, and Instructions.

_Game:_ Runs the main game

