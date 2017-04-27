package Server.Model;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by maciej on 26.04.17.
 */
public class Model {
    private final int SIZE_X = 600;
    private final int SIZE_Y = 600;
    private BufferedImage map;
    private int nPlayers;
    private ArrayList<Player> players;
    private boolean gameInProgress;
    private Graphics2D painter;

    public Model(int nPlayers) {
        this.nPlayers = nPlayers;
        gameInProgress = false;
        players = new ArrayList<Player> ();
        for(int i=0; i<nPlayers; i++) {
            players.add(new Player(i));
        }

        map = new BufferedImage(SIZE_X, SIZE_Y, BufferedImage.TYPE_INT_RGB);
        painter = map.createGraphics();

    }

    public void update() {
        //TODO: Collision detection
        for ( Player it : players) {
            it.update();
            Line2D line = new Line2D.Float(new Point2D.Float(it.getOX(), it.getOY()), new Point2D.Float(it.getX(), it.getY()));
            painter.setColor(new Color(255, 0, 0));
            /*TODO: different thickness */
            painter.setStroke( new BasicStroke(6));
            painter.draw(line);
        }
    }

    public void testRGB() {
        System.out.println( map.getRGB(150, 150));
        ImageIcon ii = new ImageIcon(map);
        JOptionPane.showMessageDialog(null, ii);
    }

}
