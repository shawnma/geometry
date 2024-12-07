import javax.swing.*;
import java.awt.*;

public class PieceTest extends JComponent {
    private final Piece root;

    public PieceTest(Piece piece, int width, int height) {
        super();

        setPreferredSize(new Dimension(width, height));

        root = piece;
    }

    public void paintComponent(Graphics g) {
        int MAX_ROTATIONS = 4;
        int padding = 4;
        int size = Math.min(getWidth() / MAX_ROTATIONS, getHeight()) - padding * 2;
        Piece p = root;
        for (int i = 0; i < MAX_ROTATIONS; i++) {
            drawPiece(g, p, new Rectangle(i * size + padding, padding, size, size));
            p = p.rotate();
            if (p.equals(root)) {
                //break;
            }
        }
     }

    /**
     * Draw the piece inside the given rectangle.
     */
    private void drawPiece(Graphics g, Piece piece, Rectangle r) {
        g.setColor(piece.getColor());
        //g.clipRect(r.x,r.y,r.width,);
        //g.drawRect(r.x, r.y, r.width, r.height);
        int padding = 2;
        int size = r.width/4-padding;
        for (Point p : piece.getPoints()) {
            g.fillRect(r.x+p.x*size+padding, r.y+(3-p.y)*size+padding, size-padding,size-padding);
        }
    }


    /**
     * Draws all the pieces by creating a JPieceTest for
     * each piece, and putting them all in a frame.
     */
    static public void main(String[] args) {
        JFrame frame = new JFrame("Piece Tester");
        Container container = frame.getContentPane();

        // Put in a BoxLayout to make a vertical list
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        Piece[] pieces = Piece.getPieces();

        for (int i = 0; i < pieces.length; i++) {
            PieceTest test = new PieceTest(pieces[i], 400, 100);
            container.add(test);
        }

        // Size the window and show it on screen
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
