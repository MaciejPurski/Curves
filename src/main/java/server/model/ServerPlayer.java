package server.model;

import util.GameColor;
import util.Player;
import util.Turn;

import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

/**
 * Class keeps player info with some extra information needed in order to calculate new position
 */

public class ServerPlayer extends Player {

    static private final int SPEED = 4;

    /**
     * The value on which angle changes each time
     */
    static private final double DIFF = 0.1;

    /**
     * Number of cycles for which the player should be visible
     * or invisible
     */
    static private int VISIBLE = 70;
    static private int INVISIBLE = 10;

    /**
     * Used to count if the player should be visible
     */
    private int counter;

    /**
     * Decides in which direction the player should turn
     */
    private Turn turn;
    private double angle;

    ServerPlayer(GameColor color, String name) {
        super(color, name);
    }

    /**
     * Method sets starting values of the player with random numbers
     */
    void init() {
        Random generator = new Random();
        int x = generator.nextInt(400) + 200;
        int y = generator.nextInt(400) + 200;
        angle = generator.nextDouble() * 2 * PI;

        super.init(x, y);
        angle = Math.random() * 2 * PI;

        counter = 0;
        turn = Turn.NONE;
    }


    /**
     * Method used to count new position of the player depending on its turn and current angle
     */

    void update() {
        if (!isInPlay())
            return;

        updateVisible();
        updateAngle();

        move((int) Math.round((SPEED * cos(angle))), (int) (Math.round(SPEED * sin(angle))));
    }

    /**
     * It is used to determine if the curve should be drawn or not.
     * This method changes player's visibility periodically
     */
    private void updateVisible() {
        if (counter > INVISIBLE && !isVisible()) {
            counter = 0;
            setVisible(true);
        }

        if (counter > VISIBLE && isVisible()) {
            counter = 0;
            setVisible(false);
        }

        counter++;
    }

    private void updateAngle() {
        if (turn == Turn.RIGHT)
            angle += DIFF;
        else if (turn == Turn.LEFT)
            angle -= DIFF;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

}
