package Client.Model;
import Client.ClientApp;
import Packets.GameStatePacket;
import Server.Model.GameObject;
import Server.Model.Player;

import java.util.ArrayList;

/**
 * Created by maciej on 10.05.17.
 */
public class ClientModel {



    private int nPlayers;
    private ArrayList<ClientPlayer> players;
    private boolean gameInProgress;

    public ClientModel () {
        nPlayers=0;
        players = new ArrayList<ClientPlayer> ();
        gameInProgress = false;
    }

    public void startGame() {
        gameInProgress = true;
    }

    public void updatePlayer (int index, int x, int y, boolean isInPlay) {
        players.get(index).setPosition(x, y);
        players.get(index).setInPlay(isInPlay);
    }

    public void addPlayer(int thickness, Player.GameColor color, String name) {
        nPlayers++;
        players.add(new ClientPlayer (thickness, color, name));

    }
    /**
     *Function used when starting new game
     */

    public void initPlayers() {
        for(ClientPlayer it: players)
            it.setStartingPosition();

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

    public ArrayList<ClientPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<ClientPlayer> players) {
        this.players = players;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }

}
