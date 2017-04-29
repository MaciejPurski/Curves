package Packets;

import Server.Model.Player.Turn;

/**
 * Created by maciej on 08.04.17.
 * Class representing moves of a particular player which is sent to the server
 */

//TODO: ujednolicenie pakietów i rozsyłanie różnych informacji
public class Packet {


    private int player;
    private Turn turn;

    public Packet(int player, Turn event){
        this.player=player;
        this.turn =event;
    }

    public Packet (String string)
    {
        parse(string);
    }

    public Packet () {

    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public String toString() {
        StringBuffer result = new StringBuffer(Integer.toString(player));
        switch (turn) {
            case RIGHT:
                result.append(" R");
                break;
            case LEFT:
                result.append(" L");
                break;
            case NONE:
                result.append(" N");
                break;
        }

        return result.toString();
    }

    public void parse(String packet) {
        String [] tab = new String [2];
        tab = packet.split(" ");
        player = Integer.parseInt(tab[0]);

        if (tab[1].equals("R"))
            turn = Turn.RIGHT;
        else if (tab[1].equals("L"))
            turn = Turn.LEFT;
        else if (tab[1].equals("N"))
            turn = Turn.NONE;

    }

    public Turn getTurn() {
        return turn;
    }
}
