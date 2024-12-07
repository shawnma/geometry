import java.awt.*;

class Point {
    final int x;
    final int y;

    private Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }
}

public class Piece {
    public static Piece[] getPieces() {
        return new Piece[]{new Piece()};
    }

    public Color getColor() {
        return Color.RED;
    }

    public Piece rotate() {
        return this;
    }

    public Point[] getPoints() {
        Point p[] = new Point[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                p[i * 4 + j] = Point.of(i, j);
            }
        }
        return p;
    }
}
