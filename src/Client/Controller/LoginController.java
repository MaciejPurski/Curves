package Client.Controller;

import Client.Model.ClientModel;
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
    private ClientModel model;

    public LoginController () throws IOException{
        client = new ClientThread("Client");
        model = new ClientModel ();
    }

    @FXML
    public void onButtonClick (ActionEvent event) throws Exception{
        //TODO porządek i zmiana nazw oraz idiotoodporność
        System.out.println("IpAddress: " + ipAddress.getText() + " port: "
                + port.getText() + " name: " + name.getText());
        client.connect(ipAddress.getText(), Integer.parseInt(port.getText()), name.getText());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Game.fxml"));
        Parent root = loader.load();
        ClientController controller = loader.getController();
        controller.init(client, model);
        controller.setPlayer(name.getText());

        Scene home_page_scene = new Scene(root);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        app_stage.setScene(home_page_scene);
        app_stage.setOnCloseRequest(e-> {
            app_stage.close();
            System.out.println("Closed");
                }
            );
        app_stage.setResizable(false);
        app_stage.centerOnScreen();
        app_stage.show();
        System.out.println("done");
        controller.start();







    }


    public void init (ClientThread client) {
        this.client = client;
    }
}
