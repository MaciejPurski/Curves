package Server.Model;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Created by maciej on 29.04.17.
 */
public class Map {
    public static final int BLACK = -16777216;
    public static final int SIZE_X = 800;
    public static final int SIZE_Y = 800;
    private BufferedImage map;
    private Graphics2D painter;

    public Map () {
        map = new BufferedImage(SIZE_X, SIZE_Y, BufferedImage.TYPE_INT_RGB);
        painter = map.createGraphics();
    }

    public void init () {
        painter.setColor(Color.BLACK);
        painter.fillRect(0,0,SIZE_X, SIZE_Y);
    }

    public void putLine(float x1, float y1, float x2, float y2) {
        Line2D line = new Line2D.Float(new Point2D.Float(x1, y1), new Point2D.Float(x2, y2));
        painter.setColor(new Color(255, 0, 0));
            /*TODO: different thickness */
        painter.setStroke( new BasicStroke(6));
        painter.draw(line);
    }

    /**
     *  Function checks if the pixel is empty - if it is black
     *
     */
    public boolean isEmpty (int x, int y) {
        return map.getRGB(x,y) == BLACK;
    }
}
