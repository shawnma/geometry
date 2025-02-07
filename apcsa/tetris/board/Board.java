
import java.awt.Color;
import java.awt.Point;

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearning. Has an "undo" feature that allows clients to
 * add and remove pieces efficiently. Does not do any drawing or have any idea
 * of pixels. Intead, just represents the abtsract 2-d board. See
 * Tetris-Architecture.html for an overview.
 *
 * This is the starter file version -- a few simple things are filled in already
 *
 * @author	Nick Parlante
 * @version	1.0, Mar 1, 2001
 */
public final class Board {

    private int width;
    private int height;
    private Color[][] grid;
    private Color[][] backupGrid;
    private boolean committed;

    private boolean DEBUG = true;

    /**
     * Creates an empty board of the given width and height measured in blocks.
     */
    public Board(int w, int h) {
        width = w;
        height = h;

        grid = new Color[width][height];
        backupGrid = new Color[width][height];
    }

    /**
     * Returns the width of the board in blocks.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the board in blocks.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the max column height present in the board. For an empty board
     * this is 0.
     */
    public int getMaxHeight() {
        int max = 0;
        for (int i = 0; i < width; i++) {
            max = Math.max(max, getColumnHeight(i));
        }
        return max;
    }

    /**
     * Checks the board for internal consistency -- used for debugging.
     */
    public void sanityCheck() {
        if (DEBUG) {
            // consistency check the board state
        }
    }

    /**
     * Given a piece and an x, returns the y value where the piece would come to
     * rest if it were dropped straight down at that x.
     *
     * <p>
     * Implementation: use the skirt and the col heights to compute this fast --
     * O(skirt length).
     */
    public int dropHeight(Piece piece, int x) {
        // this is required to drop the piece directly down
        int[] skirt = piece.getSkirt();
        int h = 0;
        for (int i = 0; i < skirt.length; i++) {
            h = Math.max(h, getColumnHeight(x + i) - skirt[i]);
        }
        return h;
    }

    /**
     * Returns the height of the given column -- i.e. the y value of the highest
     * block + 1. The height is 0 if the column contains no blocks.
     */
    public int getColumnHeight(int x) {
        int h = 0;
        for (int i = height - 1; i >= 0; i--) {
            if (grid[x][i] != null) {
                h = i + 1;
                break;
            }
        }
        return h;
    }

    /**
     * Returns the number of filled blocks in the given row.
     */
    public int getRowWidth(int y) {
        int count = 0;
        for (int i = 0; i < width; i++) {
            if (grid[i][y] != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns true if the given block is filled in the board. Blocks outside of
     * the valid width/height area always return true.
     */
    public final Color getGrid(int x, int y) {
        return grid[x][y];
    }

    public static final int PLACE_OK = 0;
    public static final int PLACE_ROW_FILLED = 1;
    public static final int PLACE_OUT_BOUNDS = 2;
    public static final int PLACE_BAD = 3;

    /**
     * Attempts to add the body of a piece to the board. Copies the piece blocks
     * into the board grid. Returns PLACE_OK for a regular placement, or
     * PLACE_ROW_FILLED for a regular placement that causes at least one row to
     * be filled.
     *
     * <p>
     * Error cases: If part of the piece would fall out of bounds, the placement
     * does not change the board at all, and PLACE_OUT_BOUNDS is returned. If
     * the placement is "bad" --interfering with existing blocks in the grid --
     * then the placement is halted partially complete and PLACE_BAD is
     * returned. An undo() will remove the bad placement.
     */
    public int place(Piece piece, int x, int y) {
        for (int i = 0; i < width; i++) {
            System.arraycopy(grid[i], 0, backupGrid[i], 0, height);
        }
        committed = false;
        for (Point p : piece.getPoints()) {
            int px = p.x + x;
            int py = p.y + y;
            if (px < 0 || py < 0 || px >= width || py >= height) {
                return PLACE_OUT_BOUNDS;
            }
            if (grid[px][py] != null) {
                return PLACE_BAD;
            }
            grid[px][py] = piece.getColor();
        }
        for (int i = 0; i < getMaxHeight(); i++) {
            boolean filled = true;
            for (int j = 0; j < width; j++) {
                if (grid[j][i] == null) {
                    filled = false;
                    break;
                }
            }
            if (filled) {
                return PLACE_ROW_FILLED;
            }
        }
        return PLACE_OK;
    }

    /**
     * Deletes rows that are filled all the way across, moving things above
     * down. Returns true if any row clearing happened.
     *
     * <p>
     * Implementation: This is complicated. Ideally, you want to copy each row
     * down to its correct location in one pass. Note that more than one row may
     * be filled.
     */
    public boolean clearRows() {
        int j = 0;
        int h = getMaxHeight();
        for (int i = 0; i < h; i++) {
            boolean filled = true;
            for (int x = 0; x < width; x++) {
                if (grid[x][i] == null) {
                    filled = false;
                    break;
                }
            }
            if (!filled) {
                if (j != i) { // only copy when they are different
                    for (int x = 0; x < width; x++) {
                        grid[x][j] = grid[x][i];
                    }
                }
                j++;
            }
        }
        for (int i = j; i < h; i++) {
            for (int x = 0; x < width; x++) {
                grid[x][i] = null;
            }
        }
        return j != h;
    }

    /**
     * If a place() happens, optionally followed by a clearRows(), a subsequent
     * undo() reverts the board to its state before the place(). If the
     * conditions for undo() are not met, such as calling undo() twice in a row,
     * then the second undo() does nothing. See the overview docs.
     */
    public void undo() {
        if (committed) {
            return;
        }
        for (int i = 0; i < width; i++) {
            System.arraycopy(backupGrid[i], 0, grid[i], 0, height);
        }
        committed = true;
    }

    /**
     * Puts the board in the committed state. See the overview docs.
     */
    public void commit() {
        committed = true;
    }

    public void dump(String hint) {
        System.out.println("Board: (" + hint + ")");
        for (int j = height - 1; j >= 0; j--) {
            for (int i = 0; i < width; i++) {
                if (grid[i][j] != null) {
                    System.out.print("M");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}
