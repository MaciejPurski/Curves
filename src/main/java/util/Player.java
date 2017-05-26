package util;

/**
 * Class keeping basic information on the player which is used both by the client and
 * the server
 */
public class Player {

    private String name;
    private int x;
    private int y;

    /**
     * isInPlay changes to false if the player dies,
     * every new round changes to true again
     */
    private boolean isInPlay;

    /**
     * Determines if the curve should be drawn or not. Server periodically
     * changes this value
     */
    private boolean isVisible;

    /**
     * The value is changed if the player leaves the game and should not be drawn again
     */
    private boolean isConnected;
    private GameColor color;

    /**
     * old coordinates needed in order to draw the player
     */
    private int ox;
    private int oy;


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
     *
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
     *
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        ox = this.x;
        oy = this.y;
        this.x = x;
        this.y = y;
    }

    /**
     * Method used to move the object. It also saves the old coordinates
     *
     * @param dx
     * @param dy
     */
    protected void move(int dx, int dy) {
        ox = x;
        oy = y;
        x += dx;
        y += dy;
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

    public boolean isInPlay() {
        return isInPlay;
    }

    public void setColor(GameColor color) {
        this.color = color;
    }

    public GameColor getColor() {
        return color;
    }

    public int getOx() {
        return ox;
    }

    public int getOy() {
        return oy;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
