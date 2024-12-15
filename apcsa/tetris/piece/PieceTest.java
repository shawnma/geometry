
import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

public class PieceTest extends JComponent {

    private final Piece root;

    public PieceTest(Piece piece, int width, int height) {
        super();
        setPreferredSize(new Dimension(width, height));
        root = piece;
    }

    @Override
    public void paintComponent(Graphics g) {
        int MAX_ROTATIONS = 4;
        int padding = 4;
        int size = Math.min(getWidth() / MAX_ROTATIONS, getHeight()) - padding * 2;
        Piece p = root;
        for (int i = 0; i < MAX_ROTATIONS; i++) {
            drawPiece(g, p, new Rectangle(i * size + padding, padding, size, size));
            p = p.next();
            if (p.equals(root)) {
                break;
            }
        }
    }

    /**
     * Draw the piece inside the given rectangle.
     */
    private void drawPiece(Graphics g, Piece piece, Rectangle r) {
        g.setColor(piece.getColor());
        int padding = 2;
        int size = r.width / 4 - padding;
        for (Point p : piece.getPoints()) {
            g.fillRect(r.x + p.x * size + padding, r.y + (3 - p.y) * size + padding, size - padding, size - padding);
        }
        g.setColor(Color.BLUE);
        g.setFont(Font.getFont("sans"));
        g.drawString(String.format("(%d,%d): %s",
                piece.getWidth(), piece.getHeight(), Arrays.toString(piece.getSkirt())), r.x, r.height + padding);
    }

    /**
     * Draws all the pieces by creating a JPieceTest for each piece, and putting
     * them all in a frame.
     */
    static public void main(String[] args) {
        JFrame frame = new JFrame("Piece Tester");
        Container container = frame.getContentPane();

        // Put in a BoxLayout to make a vertical list
        container.setLayout(new GridLayout(4, 2));

        Piece[] pieces = Piece.getPieces();

        for (Piece piece : pieces) {
            PieceTest test = new PieceTest(piece, 800, 200);
            container.add(test);
        }

        // Size the window and show it on screen
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
