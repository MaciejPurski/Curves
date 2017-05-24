package Util.Packet;


import Util.Turn;

/**
 * Created by maciej on 08.04.17.
 * Class representing key pressed or released by a particular player which is sent to the server
 */


public class MovePacket extends Packet {


    private int player;
    private Turn turn;

    public MovePacket(int player, Turn event){
        this.player=player;
        this.turn =event;
    }

    public MovePacket(String string) throws NumberFormatException
    {
        parse(string);
    }


    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public String toString() {
        StringBuffer result = new StringBuffer("M ");
        result.append(Integer.toString(player) + " ");
        result.append(turn.getValue()+ " ");

        return result.toString();
    }

    /**
     *
     * @param packet
     * @throws NumberFormatException thrown if the message was incorrectly coded
     */

    public void parse(String packet) throws NumberFormatException{
        String [] tab;
        tab = packet.split(" ");
        player = Integer.parseInt(tab[1]);
        turn = Turn.fromInt(Integer.parseInt(tab[2]));

        if (turn == null)
            throw new NumberFormatException("Wrong Turn enum value");

    }

    public Turn getTurn() {
        return turn;
    }

    public boolean equals(Object object) {
        MovePacket other = (MovePacket) object;
        if (player != other.player)
            return false;
        if (turn != other.turn)
            return false;

        return true;
    }
}
