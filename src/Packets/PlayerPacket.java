package Packets;


import Server.Model.GameColor;
import Server.Model.PlayerServer;

/**
 * Created by maciej on 12.05.17.
 */
public class PlayerPacket extends Packet {

    private int index;
    private String name;
    private GameColor color;

    public PlayerPacket(PlayerServer player, int index) {
        this.index = index;
        name = player.getName();
        color = player.getColor();
    }

    public PlayerPacket(String string) {
        parse(string);
    }

    public boolean equals (Object object) {
        PlayerPacket other = (PlayerPacket) object;

        if (other.index != index)
            return false;
        if (!other.name.equals(name))
            return false;
        if (other.color != color)
            return false;

        return true;
    }

    public String toString () {

        StringBuffer result = new StringBuffer("P ");
        result.append(Integer.toString(index) + " ");
        result.append(Integer.toString(color.getValue()) + " ");
        result.append(name);

        return result.toString();

    }

    public void parse (String string) {
        String [] tab;
        tab = string.split(" ");
        index = Integer.parseInt(tab[1]);
        color = GameColor.fromInt(Integer.parseInt(tab[2]));
        name = tab[3];

    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameColor getColor() {
        return color;
    }

    public void setColor(GameColor color) {
        this.color = color;
    }


}

