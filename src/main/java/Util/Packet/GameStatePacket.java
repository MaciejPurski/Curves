package Util.Packet;

import Server.Model.Model;

import java.util.ArrayList;

/**
 * Created by maciej on 10.05.17.
 * This packet is used to send information about current positions, visibility and state of all the players
 */
public class GameStatePacket extends Packet{

    /**
     * Structure used to keep informations on one player
     */
    public class PlayerStruct {

        public PlayerStruct() {}
        public boolean isInPlay, isVisible;
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


    public GameStatePacket(String string) throws NumberFormatException{
        players = new ArrayList<PlayerStruct> ();
        parse(string);
    }

    /**
     * Creates a packet directly from Server.Model
     * @param model game state kept in the Server.Model object
     */

    public GameStatePacket(Model model) {
        players = new ArrayList<> ();
        nPlayers = model.getPlayers().size();

        gameInProgress = model.isGameInProgress();
        for (int i=0; i<model.getPlayers().size(); i++) {
            players.add(new PlayerStruct ());
            players.get(i).isInPlay = model.getPlayers().get(i).isInPlay();
            players.get(i).isVisible = model.getPlayers().get(i).isVisible();
            players.get(i).x = model.getPlayers().get(i).getX();
            players.get(i).y = model.getPlayers().get(i).getY();
        }

    }

    /**
     *
     * @param packet
     * @throws NumberFormatException if the message was incorrectly coded
     */

    public void parse (String packet ) throws NumberFormatException {
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
                if (tab[index].equals("1"))
                    players.get(i).isVisible=true;
                else
                    players.get(i).isVisible=false;
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
                if(players.get(i).isVisible)
                    buf.append("1 ");
                else
                    buf.append("0 ");

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


    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }

    public int getnPlayers() {
        return nPlayers;
    }

    public void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    public ArrayList<PlayerStruct> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerStruct> players) {
        this.players = players;
    }
}