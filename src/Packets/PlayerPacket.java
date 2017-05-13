package Packets;


import Server.Model.Player;

/**
 * Created by maciej on 12.05.17.
 */
public class PlayerPacket extends Packet {
    private int index;
    private String name;
    private Player.GameColor color;


    public boolean equals (Object other) {
        return true;
    }

    public String toString () {
        return "";
    }

    public void parse (String string) {

    }

}

