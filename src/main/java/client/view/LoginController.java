package client.view;

import client.controller.Controller;
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

public class LoginController {

    @FXML
    private TextField port, name, ipAddress;

    private Controller controller;
    private ClientController view;
    @FXML
    private Label label;
    @FXML
    private VBox vbox;

    public LoginController() throws IOException {
    }

    @FXML
    public void onButtonClick(ActionEvent event) throws Exception {
        vbox.setVisible(false);
        label.setVisible(true);

        //check arguments
        try {
            Integer.parseInt(port.getText());   //parsing to check if it is numerical
            String address = ipAddress.getText();

            if (!checkIp(address)) {
                label.setText("Invalid ip address\nUse 0.0.0.0 format");
                return;
            }

        } catch (NumberFormatException ex) {
            label.setText("Non-numerical characters used \nin ip address or port");
            return;
        }

        label.setText("Waiting for server response");

        //establish connection

        new Thread(() -> {
            try {
                int index = controller.getSocket().connect(ipAddress.getText(), Integer.parseInt(port.getText()), name.getText());
                controller.setPlayer(index);

                Platform.runLater(() -> label.setText("Connected.\n Waiting for other players..."));
                controller.fillUsersList();

                Stage dialog = (Stage) ((Node) event.getTarget()).getScene().getWindow();
                Platform.runLater(() -> {
                            view.initMap();
                            view.showPlayers(controller.getModel());
                            dialog.close();
                        }
                );
                view.allowAbandon();
                controller.startGame();
            } catch (SocketTimeoutException exS) {
                controller.getSocket().leave();
                Platform.runLater(() -> label.setText("Connection timeout. \nserver unreachable"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();


    }


    /**
     * Used to pass arguments from other controller
     *
     * @param view
     * @param controller
     */
    public void init(ClientController view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }

    /**
     * Method checks if ip format is correct (only one acceptable is 0.0.0.0)
     *
     * @param address
     * @return
     * @throws NumberFormatException thrown if in the address is a non-numerical character
     */
    private boolean checkIp(String address) throws NumberFormatException {
        String[] tab;

        tab = address.split("\\.");
        System.out.println(tab.length);
        if (tab.length != 4)
            return false;

        for (String it : tab) {
            int tmp = Integer.parseInt(it);
            if (tmp < 0 || tmp > 255)
                return false;
        }
        return true;
    }
}
