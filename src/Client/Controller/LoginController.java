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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by maciej on 14.05.17.
 */
public class LoginController {

    @FXML
    private TextField port, name, ipAddress;

    private ClientThread client;
    private ClientController controller;

    public LoginController () throws IOException{

    }

    @FXML
    public void onButtonClick (ActionEvent event) throws Exception{
        //TODO porządek i zmiana nazw oraz idiotoodporność
        System.out.println("IpAddress: " + ipAddress.getText() + " port: "
                + port.getText() + " name: " + name.getText());
        client.connect(ipAddress.getText(), Integer.parseInt(port.getText()), name.getText());
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                    Platform.runLater(() ->{
                        try {
                            //an event with a button maybe
                            System.out.println("before filling up");
                            controller.fillUsersList();
                            System.out.println("after filling up");
                            controller.initMap();
                            while (true) {
                                controller.gameLoop();
                                if (Thread.currentThread().isInterrupted()) {
                                    break;
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    return null;
             }
        };


        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        Stage dialog = (Stage)((Node)event.getTarget()).getScene().getWindow();
        dialog.close();
    }






    public void init (ClientThread client, ClientController controller) {
        this.client = client;
        this.controller = controller;
    }
}
