
// Move is used as a struct to store a single Move
record Move (int x, int y, Piece piece, double score) {
}


public interface Brain {

    /**
     * Given a piece and a board, returns a move object that represents the best
     * play for that piece, or returns null if no play is possible. The board
     * should be in the committed state when this is called. "limitHeight" is
     * the bottom section of the board that where pieces must come to rest --
     * typically 20. If the passed in move is non-null, it is used to hold the
     * result (just to save the memory allocation).
     */
    public Move bestMove(Board board, Piece piece, int limitHeight);
}
