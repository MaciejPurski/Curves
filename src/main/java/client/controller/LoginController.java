package client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by maciej on 14.05.17.
 */
public class LoginController {

    @FXML
    private TextField port, name, ipAddress;

    private ClientSocket client;
    private ClientController controller;
    @FXML
    private Label label;
    @FXML
    private VBox vbox;


    public LoginController () throws IOException{

    }

    @FXML
    public void onButtonClick (ActionEvent event) throws Exception{

        System.out.println("IpAddress: " + ipAddress.getText() + " port: "
                + port.getText() + " name: " + name.getText());
        vbox.setVisible(false);
        label.setVisible(true);

        //check arguments

        try {
            int portNumber = Integer.parseInt(port.getText());
            String address = ipAddress.getText();

            if ( !checkIp(address)) {
                label.setText("Invalid ip address\nUse 0.0.0.0 format");
                return;
            }

        }
        catch (NumberFormatException ex) {
            label.setText("Non-numerical characters used \nin ip address or port");
            return;
        }

        label.setText("Waiting for server response");

        //establish connection

        new Thread (() -> {
            try {
                int index = client.connect(ipAddress.getText(), Integer.parseInt(port.getText()), name.getText());
                controller.setPlayer(index);

                Platform.runLater(() -> label.setText("Connected.\n Waiting for other players..."));
                controller.fillUsersList();

                Stage dialog = (Stage) ((Node) event.getTarget()).getScene().getWindow();
                Platform.runLater( () -> {
                            controller.initMap();
                            controller.showPlayers();
                            dialog.close();
                        }
                );
                controller.setConnected(true);
                controller.start();
            }
            catch (SocketTimeoutException exS) {
                Platform.runLater(() -> label.setText("Connection timeout. \nserver unreachable"));
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
    }).start();


    }


    /**
     * Used to pass arguments from other controller
     * @param client
     * @param controller
     */

    public void init (ClientSocket client, ClientController controller) {
        this.client = client;
        this.controller = controller;
    }

    /**
     * Method checks if ip format is correct (only one acceptable is 0.0.0.0)
     * @param address
     * @return
     * @throws NumberFormatException thrown if in the address is a non-numerical character
     */
    private boolean checkIp (String address) throws NumberFormatException{
        String[] tab ;

        tab = address.split("\\.");
        System.out.println(tab.length);
        if (tab.length != 4)
            return false;

        for (String it: tab)
        {
            int tmp = Integer.parseInt(it);
            if (tmp <0 || tmp >255 )
                return false;
        }

        return true;
    }
}
