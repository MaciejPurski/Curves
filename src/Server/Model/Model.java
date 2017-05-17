package Server.Model;


import java.util.ArrayList;

/**
 * Created by maciej on 26.04.17.
 */


public class Model {

    private Map map;
    private ArrayList<PlayerServer> players;
    private boolean gameInProgress;

    public Model() {
        gameInProgress = false;
        players = new ArrayList<PlayerServer> ();
        map = new Map ();
    }


    /**
     * Method called on each game beginning
     */

    public void init () {
        map.init();
        gameInProgress = true;
        for(PlayerServer it : players) {
            it.init();
        }
    }

    public void update() {

        updatePlayers();

        checkCollisions();

        updateMap();



        updateGameInProgress();

    }

    public void addPlayer (GameColor color, String name) {
        players.add(new PlayerServer(color, name));
    }

    /**
     * Method checks if the new player position is out of borders or if it interferes already existing line
     */

    private void checkCollisions(){
        for (PlayerServer it : players) {
            if(it.isInPlay() && (it.getX()<=0 || it.getY() <= 0 || it.getX()>=Map.SIZE_X || it.getY()>=Map.SIZE_Y ||
                    !map.isEmpty(it.getX(),it.getY()) )) {//collision detected
                it.setInPlay(false);
                System.out.printf("PlayerStruct name %s knocked out due to collision (%d, %d)\n", it.getName(), it.getX(), it.getY());
            }
        }
    }

    /**
     * If no player is in game it changes gameInProgress to null
     */
    private void updateGameInProgress() {
        int counter = 0;
        for (PlayerServer it: players) {
            if (it.isInPlay())
                counter++;
        }

        if (counter == 0)
            gameInProgress = false;
    }


    public String toString() {
        StringBuffer buf = new StringBuffer ( Integer.toString(players.size()) + " ");
        if(gameInProgress)
            buf.append("1 ");
        else
            buf.append("0 ");

        for (int i=0; i<players.size(); i++) {
            buf.append(Integer.toString(i) + " ");
            if(players.get(i).isInPlay()) {
                buf.append("1 ");
                buf.append(Integer.toString(players.get(i).getX()) + " " + Integer.toString(players.get(i).getY()) + " ");
            }
            else
                buf.append("0 ");
        }

        return buf.toString();
    }


    private void updatePlayers() {
        for ( PlayerServer it : players) {
            if(it.isInPlay()) {
                it.update();
            }
        }
    }

    /**
     * Method draws all the curves on the map
     */

    private void updateMap() {
        for (PlayerServer it: players) {
            if(it.isInPlay() && it.isVisible()) {
                map.putLine(it.getOx(), it.getOy(), it.getX(), it.getY());
            }
        }
    }


    public void changeDirection(int playerIndex, Turn nturn) {
        players.get(playerIndex).setTurn(nturn);
    }


    public ArrayList<PlayerServer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerServer> players) {
        this.players = players;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }

}
