package Util.Packet;


import Util.GameColor;
import Server.Model.ServerPlayer;

import java.util.ArrayList;

/**
 * Created by maciej on 12.05.17.
 * This packet is sent by a server to all players when the game is being initialized. It contains list of all players, their names and colors
 */
public class PlayerPacket extends Packet {

    /**
     * Structure used to keep information on one player
     */
    public class PlayerInfo {
        public String name;
        public GameColor color;

        public PlayerInfo (String name, GameColor color) {
            this.name = name;
            this.color = color;
        }
    }

    private ArrayList<PlayerInfo> players;

    /**
     *
     * @param playersServer List of players from server model
     */
    public PlayerPacket(ArrayList<ServerPlayer> playersServer) {
        players = new ArrayList<> ();
        for (ServerPlayer it: playersServer) {
            players.add(new PlayerInfo(it.getName(), it.getColor()));
        }
    }

    public PlayerPacket(String string) throws NumberFormatException {
        parse(string);
    }

    public boolean equals (Object object) {
        PlayerPacket other = (PlayerPacket) object;

        if (players.size() != other.players.size())
            return false;
        for (int i=0 ; i<players.size(); i++) {

            if (!other.players.get(i).name.equals(players.get(i).name))
                return false;
            if (other.players.get(i).color!=players.get(i).color)
                return false;
        }

        return true;
    }

    public String toString () {

        StringBuffer result = new StringBuffer("P ");
        result.append(Integer.toString(players.size()) + " ");
        for (PlayerInfo it: players) {
            result.append(it.name + " ");
            result.append(Integer.toString(it.color.getValue()) + " ");
        }

        return result.toString();

    }

    /**
     *
     * @param string
     * @throws NumberFormatException thrown if the information was incorrectly coded
     */

    public void parse (String string) throws NumberFormatException{
        String [] tab;
        tab = string.split(" ");
        int nPlayers = Integer.parseInt(tab[1]);
        players = new ArrayList<> ();
        int index = 2;
        for(int i = 0; i<nPlayers; i++) {
            players.add(new PlayerInfo(tab[index], GameColor.fromInt(Integer.parseInt(tab[index+1]))));
            if (players.get(i).color == null)
                throw new NumberFormatException("Wrong enum GameColor value");
            index+=2;
        }
    }

    public ArrayList<PlayerInfo> getPlayers () {
        return players;
    }

}

