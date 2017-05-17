package Client.Model;
import Packets.GameStatePacket;
import Server.Model.GameColor;
import Server.Model.PlayerServer;

import java.util.ArrayList;

/**
 * Created by maciej on 10.05.17.
 */
public class ClientModel {



    private int nPlayers;
    private ArrayList<Player> players;
    private boolean gameInProgress;

    public ClientModel () {
        nPlayers=0;
        players = new ArrayList<Player> ();
        gameInProgress = false;
    }



    public void addPlayer(GameColor color, String name) {
        nPlayers++;
        players.add(new Player(color, name));

    }
    /**
     *Function used when starting new game
     */

    public void initPlayers() {
        for(Player it: players)
            it.init(it.getX(), it.getY());

    }

    public void update (GameStatePacket packet) {
        gameInProgress = packet.isGameInProgress();
        for (int i=0; i<nPlayers; i++) {
            players.get(i).setInPlay(packet.getPlayers().get(i).isInPlay);
            players.get(i).setVisible(packet.getPlayers().get(i).isVisible);
            if (players.get(i).isInPlay()) {
                players.get(i).setPosition(packet.getPlayers().get(i).x, packet.getPlayers().get(i).y);
            }
        }
    }


    public int getnPlayers() {
        return nPlayers;
    }

    public void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }

}
