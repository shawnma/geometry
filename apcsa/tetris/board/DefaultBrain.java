// DefaultBrain.java

/**
 * Provided code.
 * A simple Brain implementation.
 * bestMove() iterates through all the possible x values
 * and rotations to play a particular piece (there are only
 * around 10-30 ways to play a piece).
 *
 * For each play, it uses the rateBoard() message to rate how
 * good the resulting board is and it just remembers the
 * play with the lowest score. Undo() is used to back-out
 * each play before trying the next. To experiment with writing your own
 * brain -- just subclass off DefaultBrain and override rateBoard().
 */
public class DefaultBrain implements Brain {

    /**
     * Given a piece and a board, returns a move object that represents the best
     * play for that piece, or returns null if no play is possible. See the
     * Brain interface for details.
     */
    @Override
    public Move bestMove(Board board, Piece piece, int limitHeight) {
        double bestScore = 1e20;
        int bestX = 0;
        int bestY = 0;
        Piece bestPiece = null;
        Piece current = piece;

        board.commit();

        // loop through all the rotations
        while (true) {
            final int yBound = limitHeight - current.getHeight() + 1;
            final int xBound = board.getWidth() - current.getWidth() + 1;

            // For current rotation, try all the possible columns
            for (int x = 0; x < xBound; x++) {
                int y = board.dropHeight(current, x);
                if (y < yBound) {    // piece does not stick up too far
                    int result = board.place(current, x, y);
                    if (result <= Board.PLACE_ROW_FILLED) {
                        int cleared = 0;
                        if (result == Board.PLACE_ROW_FILLED) {
                            cleared = board.clearRows();
                        }

                        double score = rateBoard(board, cleared);

                        if (score < bestScore) {
                            bestScore = score;
                            bestX = x;
                            bestY = y;
                            bestPiece = current;
                        }
                    }

                    board.undo();    // back out that play, loop around for the next
                }
            }

            current = current.next();
            if (current == piece) {
                break;    // break if back to original rotation

                    }}

        if (bestPiece == null) {
            return (null);    // could not find a play at all!
         }else {
            return new Move(bestX, bestY, bestPiece, bestScore);
        }
    }


    /*
     A simple brain function.
     Given a board, produce a number that rates
     that board position -- larger numbers for worse boards.
     This version just counts the height
     and the number of "holes" in the board.
     */
    public double rateBoard(Board board, int cleared) {
        return 0;
    }

}
