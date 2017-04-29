package Server.Controller;

import Packets.Packet;
import Server.Model.Model;

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

            server.fillUsersList();
            server.start();
            gameLoop();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void gameLoop() {
        long lastTime = System.nanoTime();
        final double amountOfTicks = 20.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        long timer = System.currentTimeMillis();
        model.startGame();

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {


                System.out.println(model.toString());
                server.multicastSend(model.toString());
                model.update();
                saveSignals();
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
            model.changeDirection(it.getPlayer(), it.getTurn());
        }
    }
}
