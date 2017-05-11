package Client.Model;
import Server.Model.GameObject;

import java.util.ArrayList;

/**
 * Created by maciej on 10.05.17.
 */
public class ClientModel {

    private int nPlayers;
    private ArrayList<ClientPlayer> players;
    private boolean gameInProgress;

    public ClientModel (int nPlayers) {
        this.nPlayers = nPlayers;
        players = new ArrayList<ClientPlayer> ();
        for(int i=0; i<nPlayers; i++) {
            players.add(new ClientPlayer());
        }
    }

    public void startGame() {
        gameInProgress = true;
    }

    public void updatePlayer (int index, int x, int y, boolean isInPlay) {
        players.get(index).setPosition(x, y);
        players.get(index).setInPlay(isInPlay);
    }

    void init(int nPlayers) {
        if (nPlayers > this.nPlayers) {
            while (nPlayers > this.nPlayers) {
                players.add(new ClientPlayer () );
            }

        }

        if (nPlayers < this.nPlayers) {
            while (nPlayers < this.nPlayers) {
                players.remove(players.size() - 1);
            }
        }

        this.nPlayers = nPlayers;
    }
}
