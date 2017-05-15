package Server.Model;

import static java.lang.Math.PI;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

/**
 * Created by maciej on 26.04.17.
 */
public class Player extends GameObject {
    // TODO random start position
    public enum GameColor {
        RED(0), GREEN(1), BLUE(2), YELLOW(3);
        private int value;
        GameColor(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        public static GameColor fromInt(int i) {
            for (GameColor b : GameColor .values()) {
                if (b.getValue() == i) { return b; }
            }
            return null;
        }
    }

    public enum Turn {
        LEFT(0), RIGHT(1), NONE(2);
        private int value;
        Turn(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        public static Turn fromInt(int i) {
            for (Turn b : Turn.values()) {
                if (b.getValue() == i) { return b; }
            }
            return null;
        }
    }
    private static final int STARTING_X_1 = 200; //200
    private static final int STARTING_Y_1 = 200; //200
    private static final int STARTING_X_2 = 400; //400
    private static final int STARTING_Y_2 = 400; //400
    private static final int STARTING_X_3 = 200;
    private static final int STARTING_Y_3 = 400;
    private static final int STARTING_X_4 = 400;
    private static final int STARTING_Y_4 = 200;
    private int ox, oy;
    private int speed;
    private Direction dir;
    private boolean isInPlay;
    private int thickness;
    private String name;

    private int counter;
    private int index;
    private GameColor color;
    private Turn turn;
    private double angle;
    private double diff;

    public Player(int index) {
        this.index = index;
        init();

    }

    public void init() {
        switch (index) {
            case 0:
                color = GameColor.RED;
                angle = 0;
                setX(STARTING_X_1);
                setY(STARTING_Y_1);
                break;
            case 1:
                color = GameColor.BLUE;
                angle = PI;
                setX(STARTING_X_2);
                setY(STARTING_Y_2);
                break;
            case 2:
                color = GameColor.GREEN;
                angle = PI/2;
                setX(STARTING_X_3);
                setY(STARTING_Y_3);
                break;
            case 3:
                color = GameColor.YELLOW;
                angle = -PI/2;
                setX(STARTING_X_4);
                setY(STARTING_Y_4);
                break;
        }

        ox = getX();
        oy = getY();

        isInPlay = false;
        speed = 4;
        thickness = 1;
        counter = 0;
        turn = Turn.NONE;
        diff = PI/40;

    }

    public void update() {

        if (!isInPlay)
            return;


            if (turn == Turn.RIGHT)
                angle+=diff;
                //dir.increment();
            else if (turn == Turn.LEFT)
                angle-=diff;
                //dir.decrement();


        ox = getX();
        oy = getY();

        //TODO: Zaokrąglanie
        move((int)(speed*cos(angle)), (int)(speed*sin(angle)));

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
