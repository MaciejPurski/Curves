package Server.Controller;

import Packets.GameStatePacket;
import Packets.MovePacket;
import Packets.Packet;
import Packets.PlayerPacket;
import Server.Model.Model;
import Server.Model.Player;

import java.io.IOException;
import java.util.ArrayList;


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
        model = new Model(nPlayers);
    }

    public void start ()
    {
        try {

            fillUsersList();
            server.start();
            gameLoop();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void gameLoop() {
        long lastTime = System.nanoTime();
        final double amountOfTicks = .0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        long timer = System.currentTimeMillis();
        model.startGame();

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                saveSignals();
                model.update();

                GameStatePacket toSend = new GameStatePacket(model);
                server.multicastSend(toSend.toString());


                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
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

    public void fillUsersList() throws IOException{
        for(int i=0; i<nPlayers; i ++) {
            String name = server.addUser();
            model.getPlayers().get(i).setName(name);
            for (Player it: model.getPlayers()) { // send all players data
                PlayerPacket packet = new PlayerPacket(it);
                System.out.println(packet.toString());
                server.multicastSend(packet.toString());

            }
        }
    }
}
