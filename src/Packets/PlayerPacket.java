package Packets;


import Server.Model.Player;

/**
 * Created by maciej on 12.05.17.
 */
public class PlayerPacket extends Packet {

    private int index;
    private String name;
    private Player.GameColor color;

    public PlayerPacket(Player player) {
        index = player.getIndex();
        name = player.getName();
        color = player.getColor();
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
        //TODO color
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
        color = Player.GameColor.fromInt(Integer.parseInt(tab[2]));
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

    public Player.GameColor getColor() {
        return color;
    }

    public void setColor(Player.GameColor color) {
        this.color = color;
    }


}

