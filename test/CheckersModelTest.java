import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckersModelTest {

    @Test
    public void testHasToDoubleJumpAfterFirstJump() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 1, 0, 0, 0, 0, 1, 0},
                         {0, 0, 2, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0}};

        int[][] middle = {{0, 0, 0, 0, 0, 0, 1, 0},
                          {0, 0, 0, 0, 0, 0, 0, 0},
                          {0, 0, 0, 1, 0, 0, 0, 0},
                          {0, 0, 2, 0, 0, 0, 0, 0},
                          {0, 0, 0, 0, 0, 0, 0, 0},
                          {0, 0, 0, 0, 0, 0, 0, 0},
                          {0, 0, 0, 0, 0, 0, 0, 0},
                          {0, 0, 0, 0, 0, 0, 0, 0}};

        int[][] expected = {{0, 0, 0, 0, 0, 0, 1, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 1, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0}};

        cm.setBoard(board);
        cm.moveOrJump(0, 1, 2, 3);
        assertTrue(cm.getCurrentPlayer());

        // this shouldn't work as you must jump again if second jump is available after first
        cm.moveOrJump(0, 6, 1, 7);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), middle[row][col]);
            }
        }
        // execute second jump
        cm.moveOrJump(2, 3, 4, 1);
        assertFalse(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }
    }

    @Test
    public void testPromotion() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 1, 0, 0, 0, 0, 1, 0},
                         {0, 0, 2, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 1, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0}};

        int[][] expected = {{0, 1, 0, 0, 0, 0, 1, 0},
                            {0, 0, 2, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 2, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 3, 0, 0, 0}};

        cm.setBoard(board);
        cm.moveOrJump(6, 5, 7, 4);
        assertFalse(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }
    }

    @Test
    public void testJumpingIntoPromotion() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 1, 0, 0, 0, 0, 1, 0},
                         {0, 0, 2, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 1, 0},
                         {0, 0, 0, 0, 0, 2, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0}};

        int[][] expected = {{0, 1, 0, 0, 0, 0, 1, 0},
                            {0, 0, 2, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 2, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 3, 0, 0, 0}};

        cm.setBoard(board);
        cm.moveOrJump(5, 6, 7, 4);
        assertFalse(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }
    }

    @Test
    public void testWinningByPieces() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 1, 0, 0, 0, 0, 1, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 1, 0},
                         {0, 0, 0, 0, 0, 2, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0}};

        int[][] expected = {{0, 1, 0, 0, 0, 0, 1, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 3, 0, 0, 0}};

        cm.setBoard(board);
        cm.moveOrJump(5, 6, 7, 4);
        assertTrue(cm.getIsGameOver());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }

    }

    @Test
    public void testWinningByNoMoves() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 1, 0, 0, 0, 0, 1, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {1, 0, 1, 0, 0, 0, 1, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {2, 0, 0, 0, 0, 0, 0, 0}};

        int[][] expected = {{0, 1, 0, 0, 0, 0, 1, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0, 0, 1, 0},
                            {0, 1, 0, 0, 0, 0, 0, 0},
                            {2, 0, 0, 0, 0, 0, 0, 0}};

        cm.setBoard(board);
        cm.moveOrJump(5, 0, 6, 1);
        assertTrue(cm.getIsGameOver());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }
    }

    @Test
    public void testKingJumpingBackwardsAndForwards() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 1, 4, 0, 0, 0, 0, 0},
                         {0, 0, 0, 1, 0, 1, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {1, 0, 1, 0, 0, 0, 1, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0}};

        int[][] expected = {{0, 1, 0, 0, 0, 0, 4, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0, 0, 1, 0},
                            {0, 1, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0}};

        cm.setBoard(board);
        cm.moveOrJump(5, 0, 6, 1);
        cm.moveOrJump(0, 2, 2, 4);
        assertFalse(cm.getCurrentPlayer());
        cm.moveOrJump(2, 4, 0, 6);
        assertTrue(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }
    }

    @Test
    public void testDoubleJumpingIntoPromotion() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 1, 0, 1, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 1, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 0, 0, 0, 0},
                         {1, 0, 1, 0, 0, 0, 1, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0}};

        int[][] expected = {{0, 0, 4, 0, 0, 0, 0, 0},
                            {0, 0, 0, 1, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0, 0, 1, 0},
                            {0, 1, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0}};

        cm.setBoard(board);
        cm.moveOrJump(5, 0, 6, 1);

        cm.moveOrJump(4, 2, 2, 0);
        assertFalse(cm.getCurrentPlayer());
        cm.moveOrJump(2, 0, 0, 2);
        assertTrue(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }
    }

    @Test
    public void testNormalMove() {
        CheckersModel cm = new CheckersModel();
        cm.reset();
        assertTrue(cm.getCurrentPlayer());
        cm.moveOrJump(2, 1, 3, 2);
        assertFalse(cm.getCurrentPlayer());
        assertEquals(cm.getPiece(3, 2), 1);
        assertEquals(cm.getPiece(2, 1), 0);
    }

    @Test
    public void testCannotDoubleJumpRightAfterPromotion() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 1, 0, 1, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 1, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 0, 0, 0, 0},
                         {0, 0, 1, 0, 0, 0, 1, 0},
                         {0, 1, 0, 2, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0}};

        int[][] expected = {{0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 1, 0, 1, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 1, 0, 0, 0, 0, 0, 0},
                            {0, 0, 2, 0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0, 0, 1, 0},
                            {0, 0, 0, 2, 0, 0, 0, 0},
                            {0, 0, 3, 0, 0, 0, 0, 0}};

        cm.setBoard(board);
        cm.moveOrJump(6, 1, 7, 2);
        // this move shouldn't be allowed
        cm.moveOrJump(7, 2, 5, 4);
        assertFalse(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }
    }

    @Test
    public void testNormalJump() {
        CheckersModel cm = new CheckersModel();
        cm.reset();

        int[][] expected = {{0, 1, 0, 1, 0, 1, 0, 1},
                            {1, 0, 1, 0, 1, 0, 1, 0},
                            {0, 1, 0, 0, 0, 1, 0, 1},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {2, 0, 1, 0, 2, 0, 2, 0},
                            {0, 2, 0, 2, 0, 2, 0, 2},
                            {2, 0, 2, 0, 2, 0, 2, 0}};

        assertTrue(cm.getCurrentPlayer());
        cm.moveOrJump(2, 3, 3, 4);
        assertFalse(cm.getCurrentPlayer());
        cm.moveOrJump(5, 2, 4, 3);
        assertTrue(cm.getCurrentPlayer());
        cm.moveOrJump(3, 4, 5, 2);
        assertFalse(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }
    }

    @Test
    public void testTripleJump() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 1, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 2, 0, 0, 0},
                         {0, 1, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 2, 0, 0, 0},
                         {0, 0, 1, 0, 0, 0, 1, 0},
                         {0, 1, 0, 0, 2, 0, 0, 0},
                         {0, 0, 0, 0, 0, 3, 0, 0}};

        int[][] expected = {{0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 1, 0, 3, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 1, 0, 0, 0, 0, 0, 0},
                            {0, 0, 2, 0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0, 0, 1, 0},
                            {0, 1, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0}};

        cm.setBoard(board);
        cm.moveOrJump(7, 5, 5, 3);
        assertTrue(cm.getCurrentPlayer());
        cm.moveOrJump(5, 3, 3, 5);
        assertTrue(cm.getCurrentPlayer());
        cm.moveOrJump(3, 5, 1, 3);
        assertFalse(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), expected[row][col]);
            }
        }

    }

    @Test
    public void testInvalidMoveIntoOccupiedSpace() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 1, 0, 1, 0, 0, 0, 0},
                         {0, 0, 0, 0, 2, 0, 0, 0},
                         {0, 1, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 2, 0, 0, 0},
                         {0, 0, 1, 0, 0, 0, 1, 0},
                         {0, 1, 0, 0, 2, 0, 0, 0},
                         {0, 0, 0, 0, 0, 3, 0, 0}};

        // no change should occur
        cm.setBoard(board);
        assertTrue(cm.getCurrentPlayer());
        cm.moveOrJump(1, 3, 2, 4);
        assertTrue(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), board[row][col]);
            }
        }

    }

    @Test
    public void testInvalidMoveBackwards() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 1, 0, 1, 0, 0, 0, 0},
                         {0, 0, 0, 0, 2, 0, 0, 0},
                         {0, 1, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 2, 0, 0, 0},
                         {0, 0, 1, 0, 0, 0, 1, 0},
                         {0, 1, 0, 0, 2, 0, 0, 0},
                         {0, 0, 0, 0, 0, 3, 0, 0}};

        // no change should occur
        cm.setBoard(board);
        assertTrue(cm.getCurrentPlayer());
        cm.moveOrJump(1, 1, 0, 0);
        assertTrue(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), board[row][col]);
            }
        }

    }

    @Test
    public void testInvalidJumpNoCapture() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 1, 0, 1, 0, 0, 0, 0},
                         {0, 0, 0, 0, 2, 0, 0, 0},
                         {0, 1, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 2, 0, 0, 0},
                         {0, 0, 1, 0, 0, 0, 1, 0},
                         {0, 1, 0, 0, 2, 0, 0, 0},
                         {0, 0, 0, 0, 0, 3, 0, 0}};

        // no change should occur
        cm.setBoard(board);
        assertTrue(cm.getCurrentPlayer());
        cm.moveOrJump(1, 1, 3, 3);
        assertTrue(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), board[row][col]);
            }
        }
    }

    @Test
    public void testInvalidMoveToOutOfBounds() {
        CheckersModel cm = new CheckersModel();

        int[][] board = {{0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 1, 0, 1, 0, 0, 0, 0},
                         {0, 0, 0, 0, 2, 0, 0, 0},
                         {0, 1, 0, 0, 0, 0, 0, 0},
                         {0, 0, 2, 0, 2, 0, 0, 1},
                         {0, 0, 1, 0, 0, 0, 1, 0},
                         {0, 1, 0, 0, 2, 0, 0, 0},
                         {0, 0, 0, 0, 0, 3, 0, 0}};

        // no change should occur
        cm.setBoard(board);
        assertTrue(cm.getCurrentPlayer());
        cm.moveOrJump(4, 7, 5, 8);
        assertTrue(cm.getCurrentPlayer());
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(cm.getPiece(row, col), board[row][col]);
            }
        }

    }
}
