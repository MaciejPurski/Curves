package Server.Model;


import java.util.ArrayList;

/**
 * Created by maciej on 26.04.17.
 */
public class Model {

    private Map map;
    private int nPlayers;
    private ArrayList<Player> players;
    private boolean gameInProgress;

    public Model(int nPlayers) {
        this.nPlayers = nPlayers;
        gameInProgress = false;
        players = new ArrayList<Player> ();
        for(int i=0; i<nPlayers; i++) {
            players.add(new Player(i));
        }

        map = new Map ();
    }

    public void init () {
        map.init();
        for(Player it : players)
            it.init();
    }

    public void startGame() {
        gameInProgress = true;
        for ( Player it : players) {
            it.setInPlay(true);
        }
    }



    public void update() {

        for ( Player it : players) {
            if(it.isInPlay()) {
                it.update();
            }
            //TODO : different thickness
        }

        checkCollisions();

        for (Player it: players) {
            if(it.isInPlay()) {
                map.putLine(it.getOX(), it.getOY(), it.getX(), it.getY());
            }
        }
    }


    private void checkCollisions(){
        for (Player it : players) {
            if(it.isInPlay() && (it.getX()<=0 || it.getY() <= 0 || it.getX()>=Map.SIZE_X || it.getY()>=Map.SIZE_Y || !map.isEmpty(it.getX(),it.getY()) )) {//collision detected
                it.setInPlay(false);
                System.out.printf("PlayerStruct number %d is knocked out due to collision (%d, %d)\n", it.getIndex(), it.getX(), it.getY());
            }
        }
    }


    public String toString() {
        StringBuffer buf = new StringBuffer ( Integer.toString(nPlayers) + " ");
        if(gameInProgress)
            buf.append("1 ");
        else
            buf.append("0 ");

        for (int i=0; i<nPlayers; i++) {
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


    public void changeDirection(int playerIndex, Player.Turn nturn) {
        players.get(playerIndex).setTurn(nturn);
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
