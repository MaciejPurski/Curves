package Packets;


import Server.Model.Turn;

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
        result.append(Integer.toString(player) + " ");
        result.append(turn.getValue()+ " ");

        return result.toString();
    }

    public void parse(String packet) {
        //TODO: wyjątki jeśli się nie zgadzają dane
        String [] tab;
        tab = packet.split(" ");
        player = Integer.parseInt(tab[1]);

        turn = Turn.fromInt(Integer.parseInt(tab[2]));

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
