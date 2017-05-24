package Util;

/**
 * Created by maciej on 10.05.17.
 *
 */
public class Player {

    private String name;
    private int x;
    private int y;
    private boolean isInPlay;
    private boolean isVisible;
    private boolean isConnected;
    private GameColor color;
    private int ox, oy; // old coordinates needed in order to draw the player


    public Player() {

        isInPlay = false;
        isVisible = true;
    }

    /**
     * This constructor is called when a new player is added to the list
     */

    public Player(GameColor color, String name) {
        isInPlay = false;
        isVisible = true;
        isConnected = true;
        this.color = color;
        this.name = name;
    }


    /**
     * Set starting positions of the player just before the game starts
     * @param x x - coordinate
     * @param y y - coordinate
     */

    public void init(int x, int y) {
        isInPlay = true;
        isVisible = true;
        this.x = x;
        this.y = y;
        ox = x;
        oy = y;
    }

    /**
     * Sets a new position of the player when updating
     * @param x
     * @param y
     */

    public void setPosition (int x, int y) {
        ox = this.x;
        oy = this.y;
        this.x = x;
        this.y = y;
    }

    /**
     * Function used to move the object. It also remembers the old coordinates
     * @param dx
     * @param dy
     */

    public void move (int dx, int dy) {
        ox = x;
        oy = y;
        x+=dx;
        y+=dy;
    }


    public void setInPlay(boolean inPlay) {
        isInPlay = inPlay;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getY() {
        return y;
    }

    public boolean isInPlay() {
        return isInPlay;
    }


    public void setColor(GameColor color) { this.color = color;}

    public GameColor getColor() {
        return color;
    }

    public int getOx() {
        return ox;
    }

    public int getOy() {
        return oy;
    }

    public int getX() { return x; }

    public String getName() { return name; }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
