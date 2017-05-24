package Client.Controller;

import Client.Model.ClientModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.FutureTask;

/**
 * Created by maciej on 14.05.17.
 */
public class LoginController {

    @FXML
    private TextField port, name, ipAddress;

    private ClientThread client;
    private ClientController controller;
    @FXML
    private Label label;
    @FXML
    private VBox vbox;


    public LoginController () throws IOException{

    }

    @FXML
    public void onButtonClick (ActionEvent event) throws Exception{
        //TODO porządek i zmiana nazw oraz idiotoodporność
        System.out.println("IpAddress: " + ipAddress.getText() + " port: "
                + port.getText() + " name: " + name.getText());
        vbox.setVisible(false);
        label.setText("Waiting for server response");
        label.setVisible(true);



        new Thread (() -> {


            try {

                int index = client.connect(ipAddress.getText(), Integer.parseInt(port.getText()), name.getText());
                controller.setPlayer(index);
                controller.setConnected(true);
                Platform.runLater(() -> label.setText("Connected.\n Waiting for other players..."));
                controller.fillUsersList();

                Stage dialog = (Stage) ((Node) event.getTarget()).getScene().getWindow();
                Platform.runLater( () -> {
                            controller.initMap();
                            controller.showPlayers();
                            dialog.close();
                        }
                );

                controller.start();

            }
            catch (SocketTimeoutException exS) {
                Platform.runLater(() -> label.setText("Connection timeout. Server unreachable"));
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
    }).start();


    }






    public void init (ClientThread client, ClientController controller) {
        this.client = client;
        this.controller = controller;
    }
}
