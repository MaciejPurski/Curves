package Server.Controller;

import Packets.GameStatePacket;
import Packets.MovePacket;
import Packets.Packet;
import Packets.PlayerPacket;
import Server.Model.GameColor;
import Server.Model.Model;
import Server.Model.PlayerServer;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


/**
 * Created by maciej on 26.04.17.
 *
 */
public class ServerController {

    private int nPlayers;
    private ServerThread server;
    private Model model;


    public ServerController(int nPlayers) throws IOException{
        this.nPlayers=nPlayers;
        server = new ServerThread("Server", nPlayers);
        model = new Model();
    }

    public void start ()
    {
        try {
            fillUsersList();
            sendPlayersInfo();
            server.start();
            while (true) {
                gameLoop();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void gameLoop() throws Exception {

        final double amountOfTicks = 25.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;


        model.init();
        //first packet

        GameStatePacket toSend = new GameStatePacket(model);
        server.multicastSend(toSend.toString());

        sleep(3000);

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        while (model.isGameInProgress()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                saveSignals();
                model.update();

                toSend = new GameStatePacket(model);
                server.multicastSend(toSend.toString());
                System.out.println(toSend.toString());


                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }

        sleep(3000);
    }

    public void saveSignals() {
        ArrayList<Packet> tab = server.getPackets();

        for(Packet it: tab) {
            if (it instanceof MovePacket) {
                MovePacket received = (MovePacket) it;
                model.changeDirection(received.getPlayer(), received.getTurn());
            }
            //TODO inne pakiety

        }
    }

    /**
     * Method called when the server is started. It waits for players to log in. The number of players is specified when the server is started
     * @throws IOException
     */

    private void fillUsersList() throws IOException{
        for(int i=0; i<nPlayers; i ++) {

            String name = server.addUser(i);
            model.addPlayer(GameColor.fromInt(i), name);

        }
    }

    /**
     * Method sends to all the players info on other players
     */

    private void sendPlayersInfo() throws Exception{
        for (int i=0; i<model.getPlayers().size() ; i++) { // send all players data
            PlayerPacket packet = new PlayerPacket(model.getPlayers().get(i), i);
            System.out.println(packet.toString());
            server.multicastSend(packet.toString());
            sleep(500);
        }
    }
}
