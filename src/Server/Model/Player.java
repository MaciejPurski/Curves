package Server.Model;

/**
 * Created by maciej on 26.04.17.
 */
public class Player extends GameObject {
    // TODO random start position
    private static final int STARTING_X_1 = 200; //200
    private static final int STARTING_Y_1 = 200; //200
    private static final int STARTING_X_2 = 400; //400
    private static final int STARTING_Y_2 = 400; //400
    private static final int STARTING_X_3 = 200;
    private static final int STARTING_Y_3 = 400;
    private static final int STARTING_X_4 = 400;
    private static final int STARTING_Y_4 = 200;
    public enum GameColor {RED, GREEN, BLUE, YELLOW};
    public enum Turn {LEFT, RIGHT, NONE};
    private int ox, oy;
    private int speed;
    private Direction dir;
    private boolean isInPlay;
    private int thickness;

    int counter;
    private int index;
    private GameColor color;
    private Turn turn;

    public Player(int index) {
        this.index = index;
        init();

    }

    public void init() {
        switch (index) {
            case 0:
                color = GameColor.RED;
                dir=new Direction(0);
                setX(STARTING_X_1);
                setY(STARTING_Y_1);
                break;
            case 1:
                color = GameColor.GREEN;
                dir=new Direction(12);
                setX(STARTING_X_2);
                setY(STARTING_Y_2);
                break;
            case 2:
                color = GameColor.BLUE;
                dir=new Direction(24);
                setX(STARTING_X_3);
                setY(STARTING_Y_3);
                break;
            case 3:
                color = GameColor.YELLOW;
                dir=new Direction(36);
                setX(STARTING_X_4);
                setY(STARTING_Y_4);
                break;
        }

        ox = getX();
        oy = getY();

        isInPlay = false;
        speed = 5;
        thickness = 1;
        counter = 0;
        turn = Turn.NONE;

    }

    public void update() {

        if (!isInPlay)
            return;

        if (counter>=1) {
            if (turn == Turn.RIGHT)
                dir.increment();
            else if (turn == Turn.LEFT)
                dir.decrement();
            counter =0;
        }
        ox = getX();
        oy = getY();
        counter++;
        //TODO: Zaokrąglanie
        move((int)(speed*dir.getX()), (int)(speed*dir.getY()));

    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public boolean isInPlay() {
        return isInPlay;
    }

    public void setInPlay(boolean inPlay) {
        isInPlay = inPlay;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public GameColor getColor() {
        return color;
    }

    public void setColor(GameColor color) {
        this.color = color;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public int getSpeed() {

        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getOY() { return oy; }

    public int getOX() { return ox; }
    public int getIndex() { return index; }
}
