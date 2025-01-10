
import java.awt.Color;
import java.awt.Point;


public class Piece {

    private final Color color;
    private final Point[] points;
    private int height;
    private int width;
    private int[] skirt;
    private Piece next;

    private Piece(Color color, int[] coords) {
        this.color = color;
        this.points = new Point[4];
        for (int i = 0; i < 4; i++) {
            points[i] = new Point(coords[i * 2], coords[i * 2 + 1]);
        }
        computeSize();
    }

    private Piece(Color color, Point[] points) {
        this.color = color;
        this.points = points;
        computeSize();
    }

    public static Piece[] getPieces() {
        Piece l = constructRow(Color.MAGENTA, new int[]{0, 0, 0, 1, 0, 2, 1, 0}, 4);
        Piece l2 = constructRow(Color.RED, new int[]{0, 0, 1, 1, 1, 0, 1, 2}, 4);
        Piece dog = constructRow(Color.ORANGE, new int[]{0, 0, 1, 0, 1, 1, 2, 1}, 2);
        Piece dog2 = constructRow(Color.YELLOW, new int[]{0, 1, 1, 1, 1, 0, 2, 0}, 2);
        Piece line = constructRow(Color.CYAN, new int[]{0, 0, 0, 1, 0, 2, 0, 3}, 2);
        Piece t = constructRow(Color.GREEN, new int[]{0, 0, 1, 0, 2, 0, 1, 1}, 4);
        Piece square = constructRow(Color.BLUE, new int[]{0, 0, 0, 1, 1, 0, 1, 1}, 1);
        return new Piece[]{l, l2, dog, dog2, line, t, square};
    }

    private void computeSize() {
        for (int i = 0; i < 4; i++) {
            if (points[i].x + 1 > width) {
                width = points[i].x + 1;
            }
            if (points[i].y + 1 > height) {
                height = points[i].y + 1;
            }
        }
        skirt = new int[width];
        for (int i = 0; i < skirt.length; i++) {
            skirt[i] = height;
        }
        for (Point point : points) {
            int x = point.x;
            if (skirt[x] > point.y) {
                skirt[x] = point.y;
            }
        }
    }

    private static Piece constructRow(Color color, int[] coords, int n) {
        Piece root = new Piece(color, coords);
        Piece current = root;
        for (int i = 0; i < n - 1; i++) {
            Piece next = current.rotate();
            current.next = next;
            current = next;
        }
        current.next = root;
        return root;
    }

    public Color getColor() {
        return color;
    }

    public Piece next() {
        return next;
    }

    private Piece rotate() {
        Point[] p = new Point[4];
        int shift = 0;
        for (int i = 0; i < 4; i++) {
            if (points[i].y > shift) {
                shift = points[i].y;
            }
        }
        for (int i = 0; i < 4; i++) {
            Point old = points[i];
            int newx = -old.y + shift;
            int newy = old.x;
            p[i] = new Point(newx, newy);
        }
        return new Piece(color, p);
    }

    public Point[] getPoints() {
        return points;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getSkirt() {
        return skirt;
    }
}
