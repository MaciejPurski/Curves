package Client.Controller;

/**
 * Created by maciej on 22.03.17.
 */
import Client.Model.ClientModel;
import Util.Player;
import Util.Packet.GameStatePacket;
import Util.Packet.MovePacket;
import Util.Packet.Packet;
import Util.Packet.PlayerPacket;
import Util.Turn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.*;


public class ClientController implements Runnable{

    private int playerIndex;
    private ClientThread client;
    private ClientModel model;
    private boolean isPressedRight;
    private boolean isPressedLeft;
    private int drawCounter;
    private boolean isConnected;
    private Thread gameThread;
    @FXML
    private Canvas map;
    @FXML
    private VBox playersList;
    @FXML
    private Label text1,text2,text3,text4, text5, text6;
    @FXML
    private Circle circle1, circle2, circle3, circle4, circle5, circle6;
    @FXML
    private Button join, abandon;

    public ClientController() {
        try {
            isConnected = false;
            client = new ClientThread("Client");
            model = new ClientModel();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void init (ClientThread client, ClientModel model) {

        this.client = client;
        this.model = model;
    }

    public void run() {
        try {
            while (true) {
                gameLoop();
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void onKeyPressed(KeyEvent event) {

        switch (event.getCode()) {
            case LEFT:
                if (!isPressedLeft && !isPressedRight) {
                    client.sendData(new MovePacket(playerIndex, Turn.LEFT).toString());
                    isPressedLeft = true;
                }
                break;
            case RIGHT:
                if(!isPressedRight && !isPressedLeft){
                    client.sendData(new MovePacket(playerIndex, Turn.RIGHT).toString());
                    isPressedRight = true;
                }
                break;
            default:
                break;
        }

    }

    @FXML
    public void onKeyReleased(KeyEvent event) {
        isPressedRight = false;
        isPressedLeft = false;

        client.sendData(new MovePacket(playerIndex, Turn.NONE).toString());
    }

    @FXML
    void onJoinClick(MouseEvent event) {
        try {

            if (isConnected)
                return;

            Stage stage = new Stage();
            ((Node)event.getSource()).getScene().getWindow().setOnCloseRequest(e -> {
                //TODO set exit packet
                if(gameThread!= null)
                    gameThread.interrupt();
            });

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = (Parent) loader.load();
            LoginController login = loader.getController();
            login.init(client,this);
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
        }
            catch (Exception e) {
            e.printStackTrace();
            }


    }




    public void fillUsersList () throws IOException{


            Packet received = client.receiveMulticast();
            if (received instanceof PlayerPacket) { //information on new player
                PlayerPacket player = (PlayerPacket) received;
                for(PlayerPacket.PlayerInfo it: player.getPlayers())
                    model.addPlayer(it.color, it.name);
            }
            else
                throw new InvalidObjectException("Wrong packet received: " + received.toString());

    }

    public void gameLoop() throws IOException{
        long lastTime = System.nanoTime();
        final double amountOfTicks = 25.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        Packet received;
        GameStatePacket gameState;

        long timer = System.currentTimeMillis();

        received = client.receiveMulticast();

        if (received instanceof GameStatePacket) {
            gameState = (GameStatePacket) received;

            model.update(gameState);
            model.initPlayers();
            Platform.runLater ( () -> {
                initMap();
                drawModel();
            });

        }
        else {

            // game stopped
            setGameStop();
        }


        while (model.isGameInProgress()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {


                received = client.receiveMulticast();

                if (received instanceof GameStatePacket) {
                    gameState = (GameStatePacket) received;
                    model.update(gameState);
                    Platform.runLater ( () -> {
                        drawModel();
                    });
                }
                else {

                     // game stopped
                    setGameStop();
                }

                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }

            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }



    public void setPlayer(int index) {
        playerIndex = index;
    }

    public void initMap () {
        drawCounter = 0;

        map.setFocusTraversable(true);
        GraphicsContext gc = map.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        gc.fillRect(0,0,800,800);
    }

    public void drawModel() {

        GraphicsContext gc = map.getGraphicsContext2D();
        for (Player it : model.getPlayers()) {
            Color playerColor = it.getColor().toColor();


            if(it.isVisible()) {
                drawCounter = 0;

                gc.setFill(playerColor);
                gc.setStroke(playerColor);

                gc.setLineCap(StrokeLineCap.ROUND);
                gc.setLineJoin(StrokeLineJoin.ROUND);
                gc.setLineWidth(8);
                gc.strokeLine(it.getOx(), it.getOy(), it.getX()+2, it.getY()+2);
            }
                else {

                if(drawCounter>= 1) {
                    gc.setFill(Color.BLACK);

                }
                else {
                    gc.setFill(playerColor);
                    gc.setStroke(playerColor);
                }

                gc.fillRect(it.getOx(), it.getOy(), 2, 2);

                drawCounter ++;
            }
            gc.setFill(playerColor);
            gc.fillRect(it.getX(), it.getY(), 2, 2);
        }
    }

    public void setGameStop () {
        isConnected = false;
        model.setGameInProgress(false);
        initMap();
        GraphicsContext gc = map.getGraphicsContext2D();

        gc.setLineWidth(3);
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(30));
        gc.strokeText("Server stopped", 20, 20);
        model.reset();
        client.reset();
        isPressedLeft = false;
        isPressedRight = false;
        drawCounter=0;
        Thread.currentThread().interrupt();

    }

    public void showPlayers() {
        for (int i=0; i<model.getPlayers().size(); i++) {
            switch (i) {
                case 0:
                    text1.setText(model.getPlayers().get(i).getName());
                    circle1.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle1.setVisible(true);
                    break;
                case 1:
                    text2.setText(model.getPlayers().get(i).getName());
                    circle2.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle2.setVisible(true);
                    break;
                case 2:
                    text3.setText(model.getPlayers().get(i).getName());
                    circle3.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle3.setVisible(true);
                    break;
                case 3:
                    text4.setText(model.getPlayers().get(i).getName());
                    circle4.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle4.setVisible(true);
                    break;
                case 4:
                    text5.setText(model.getPlayers().get(i).getName());
                    circle5.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle5.setVisible(true);
                    break;
                case 5:
                    text6.setText(model.getPlayers().get(i).getName());
                    circle6.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle6.setVisible(true);
                    break;
            }
        }
    }


    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void start () {
        gameThread = new Thread (this);
        gameThread.start();
    }
}
