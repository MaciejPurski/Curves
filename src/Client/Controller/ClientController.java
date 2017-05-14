package Client.Controller;

/**
 * Created by maciej on 22.03.17.
 */
import Client.Model.ClientModel;
import Packets.GameStatePacket;
import Packets.Packet;
import Packets.PlayerPacket;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientController extends Thread{

    private int playerIndex;
    private String playerName;
    private ClientThread client;
    private ClientModel model;

    public ClientController() {
        super("ClientThread");
    }

    public void init (ClientThread client, ClientModel model) {

        this.client = client;
        this.model = model;
    }

    public void run() {
        try {

            fillUsersList();
            //client.start();
            //gameLoop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void fillUsersList () throws IOException{
        while (true) {
            Packet received = client.receive();
            if (received instanceof PlayerPacket) { //information on new player
                PlayerPacket player = (PlayerPacket) received;
                if (player.getIndex() >= model.getnPlayers()) { // if it is a new player
                    model.addPlayer(2, player.getColor(), player.getName());
                    if (player.getName().equals(playerName)) {
                        playerIndex = player.getIndex();
                    }
                }
            }
            else if (received instanceof GameStatePacket) {
                GameStatePacket gameState = (GameStatePacket) received;
                model.update(gameState);
                return;
            }

            else
                throw new InvalidObjectException("Wrong packet received");

        }
    }

    public void gameLoop() throws IOException{
        long lastTime = System.nanoTime();
        final double amountOfTicks = 20.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        Packet received;
        GameStatePacket gameState;

        long timer = System.currentTimeMillis();
        model.startGame();

        while (model.isGameInProgress()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {


                received = client.receive();

                if (received instanceof GameStatePacket) {
                    gameState = (GameStatePacket) received;
                    model.update(gameState);
                }

                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
    }

    //TODO player name to the model

    public void setPlayer(String string) {
        playerName = string;
    }
}
