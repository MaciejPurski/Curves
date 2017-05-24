package Server.Model;

import Util.GameColor;
import Util.Player;
import Util.Turn;

import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

/**
 * Created by maciej on 26.04.17.
 * Class keeps player info with some extra information needed in order to calculate new position
 */

public class ServerPlayer extends Player {


    static private final int SPEED = 5;
    static private final double DIFF = 0.1;
    private int counter;

    private Turn turn;
    private double angle;

    public ServerPlayer(GameColor color, String name) {
        super(color, name);
    }
    /**
     * Method sets starting values of the player with random numbers
     */

    public void init() {
        Random generator = new Random();
        int x = generator.nextInt(400) + 200;
        int y = generator.nextInt(400) + 200;
        angle = generator.nextDouble()*2*PI;

        super.init(x,y);
        angle = Math.random() * 2*PI;


        counter = 0;
        turn = Turn.NONE;

    }


    /**
     * Method used to count new position of the player depending on its turn and current angle
     */

    public void update() {

        if (!isInPlay())
            return;

        updateVisible();
        updateAngle();


        move((int)Math.round((SPEED*cos(angle))), (int)(Math.round(SPEED*sin(angle))));

    }

    /**
     * It is used to determine if the curve should be drawn or not.
     */
    private void updateVisible() {
        if(counter>10 && !isVisible()) {
            counter =0;
            setVisible(true);
        }

        if(counter>50 && isVisible()) {
            counter =0;
            setVisible(false);
        }

        counter ++;
    }

    private void updateAngle() {
        if (turn == Turn.RIGHT)
            angle+=DIFF;
        else if (turn == Turn.LEFT)
            angle-=DIFF;
    }



    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

}
