package server.view;

import javafx.scene.control.Button;
import server.controller.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import util.Controller;


public class ServerController implements Controller {

    private GameController gameController;
    @FXML
    private TextArea textArea;
    @FXML
    private Slider slider;
    @FXML
    private Button start, stop;


    public ServerController() {
        gameController = new GameController(this);
    }

    @FXML
    void onStartPressed(MouseEvent event) {

        start.setDisable(true);
        stop.setDisable(false);

        // stop the game when stop is pressed
        ((Node) event.getSource()).getScene().getWindow().setOnCloseRequest(e -> {
            if (!gameController.isConnected())
                System.exit(0);

            gameController.setGameStop();
        });

        //read users number from the slider
        int nPlayers = (int) slider.getValue();
        gameController.setNPlayers(nPlayers);

        textArea.appendText("Game initialized with " + nPlayers + " players. Waiting for players to log in...\n");
        new Thread(() -> gameController.initConnection()).start();
    }

    @FXML
    void onStopPressed() {
        start.setDisable(false);
        stop.setDisable(true);
        gameController.setGameStop();
    }

    /**
     * Method used to add a message to logs list on the screen.
     * It is called from different threads
     *
     * @param string
     */
    public synchronized void appendLogs(String string) {
        if (string.isEmpty())
            return;
        Platform.runLater(() ->
                textArea.appendText(string));
    }

    public void allowStart() {
        start.setDisable(false);
        stop.setDisable(true);
    }

    public void setPort(int port) {
        gameController.getSocket().setServerPort(port);
    }

}
