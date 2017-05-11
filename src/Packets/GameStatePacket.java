package Packets;

import Server.Model.Model;
import Server.Model.Player;

import java.util.ArrayList;

/**
 * Created by maciej on 10.05.17.
 */
public class GameStatePacket extends Packet{

    private class PlayerStruct {

        public PlayerStruct() {}
        public boolean isInPlay;
        public int x, y;

        public boolean equals (Object object) {
            PlayerStruct other = (PlayerStruct) object;

            if ( x != other.x)
                return false;
            if (y != other.y)
                return false;
            if (isInPlay != other.isInPlay)
                return false;

            return true;
        }
    }
    private boolean gameInProgress;
    private int nPlayers;
    private ArrayList<PlayerStruct> players;


    public GameStatePacket(String string) {
        players = new ArrayList<PlayerStruct> ();
        parse(string);
    }

    public GameStatePacket(Model model) {
        players = new ArrayList<PlayerStruct> ();
        nPlayers = model.getnPlayers();

        gameInProgress = model.isGameInProgress();
        for (int i=0; i<model.getPlayers().size(); i++) {
            players.add(new PlayerStruct ());
            players.get(i).isInPlay = model.getPlayers().get(i).isInPlay();
            players.get(i).x = model.getPlayers().get(i).getX();
            players.get(i).y = model.getPlayers().get(i).getY();
        }

    }

    public void parse (String packet ) {
        String [] tab;
        tab = packet.split(" ");

        nPlayers = Integer.parseInt(tab[1]);

        if (tab[2].equals("1"))
            gameInProgress = true;
        else
            gameInProgress = false;

        int index = 4;
        for (int i=0; i<nPlayers; i++) {
            players.add(new PlayerStruct());
            if (tab[index].equals("1")) {
                players.get(i).isInPlay=true;
                index++;

                players.get(i).x = Integer.parseInt(tab[index]);
                index++;
                players.get(i).y = Integer.parseInt(tab[index]);
                index+=2;
            }
            else {
                players.get(i).isInPlay=false;
                index+=2;
            }

        }
    }

    public String toString() {

        StringBuffer buf = new StringBuffer ( "G ");
        buf.append(Integer.toString(nPlayers) + " ");
        if(gameInProgress)
            buf.append("1 ");
        else
            buf.append("0 ");

        for (int i=0; i<nPlayers; i++) {
            buf.append(Integer.toString(i) + " ");
            if(players.get(i).isInPlay) {
                buf.append("1 ");
                buf.append(Integer.toString(players.get(i).x) + " " + Integer.toString(players.get(i).y) + " ");
            }
            else
                buf.append("0 ");
        }

        return buf.toString();
    }


    public boolean equals(Object object) {
        GameStatePacket other = (GameStatePacket) object;
        if (gameInProgress != other.gameInProgress)
            return false;
        if (nPlayers != other.nPlayers)
            return false;

        if (players.size() != other.players.size())
            return false;

        for (int i=0; i<players.size(); i++) {
            if (!players.get(i).equals(other.players.get(i)))
                return false;
        }



        return true;
    }



}
