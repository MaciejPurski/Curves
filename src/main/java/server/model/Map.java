package server.model;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Class keeps in the memory game map which is represented by a Buffered Image
 */
class Map {

    /**
     * Decimal value of black color used to compare pixels
     */
    private static final int BLACK = -16777216;
    static final int SIZE_X = 700;
    static final int SIZE_Y = 700;
    static final int LINE_WIDTH = 6;
    private BufferedImage map;
    private Graphics2D painter;

    public Map() {
        map = new BufferedImage(SIZE_X, SIZE_Y, BufferedImage.TYPE_INT_RGB);
        painter = map.createGraphics();
    }

    /**
     * Paints the map black when the game is initialized
     */
    void init() {
        painter.setColor(Color.BLACK);
        painter.fillRect(0, 0, SIZE_X, SIZE_Y);
    }

    void putLine(float x1, float y1, float x2, float y2) {
        Line2D line = new Line2D.Float(new Point2D.Float(x1, y1), new Point2D.Float(x2, y2));
        painter.setColor(Color.RED);
        painter.setStroke(new BasicStroke(LINE_WIDTH));
        painter.draw(line);
    }

    /**
     * Method checks if the pixel is empty = if it is black
     */
    boolean isEmpty(int x, int y) {
        return map.getRGB(x, y) == BLACK;
    }
}
