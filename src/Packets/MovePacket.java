package Packets;

import Server.Model.Player.Turn;

/**
 * Created by maciej on 08.04.17.
 * Class representing moves of a particular player which is sent to the server
 */

//TODO: ujednolicenie pakietów i rozsyłanie różnych informacji
public class MovePacket extends Packet {


    private int player;
    private Turn turn;

    public MovePacket(int player, Turn event){
        this.player=player;
        this.turn =event;
    }

    public MovePacket(String string)
    {
        parse(string);
    }

    public MovePacket() {

    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public String toString() {
        StringBuffer result = new StringBuffer("M ");
        result.append(Integer.toString(player));
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
        //TODO: wyjątki jeśli się nie zgadzają dane
        String [] tab;
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
    //TODO equals
    public boolean equals(Object object) {
        MovePacket other = (MovePacket) object;
        if (player != other.player)
            return false;
        if (turn != other.turn)
            return false;

        return true;
    }
}
