package Client.Model;

import Server.Model.Player;

/**
 * Created by maciej on 10.05.17.
 *
 */
public class ClientPlayer {
    private String name;
    private int x;
    private int y;
    private boolean isInPlay;
    private int thickness;
    private Player.GameColor color;
    private int ox, oy; // old coordinates needed in order to draw the player

    public int getY() {
        return y;
    }

    public boolean isInPlay() {
        return isInPlay;
    }

    public int getThickness() {
        return thickness;
    }

    public Player.GameColor getColor() {
        return color;
    }

    public int getOx() {
        return ox;
    }

    public int getOy() {
        return oy;
    }

    public int getX() {

        return x;
    }



    public ClientPlayer () {

        isInPlay = false;
    }

    public ClientPlayer (int thickness, Player.GameColor color, String name) {
        init(thickness, color, name);
    }

    public void init(int thickness, Player.GameColor color, String name) {

        this.thickness = thickness;
        this.color = color;
        this.name=name;
    }

    public void setPosition (int x, int y) {
        ox = this.x;
        oy = this.y;
        this.x = x;
        this.y = y;
    }


    public void setInPlay(boolean inPlay) {
        isInPlay = inPlay;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }



}
