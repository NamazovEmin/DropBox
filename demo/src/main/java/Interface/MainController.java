package Interface;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import сlient.Network;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {



    public ListView<String> userPanel;
    public ListView<String> serverPanel;
    public Button loadToServerButton;
    public Button loadFromServerButton;
    private Network network;

    public MainController(){

    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.network = new Network(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void loadToServer() throws IOException {
    network.sendFile();
    }

    public void loadFromServer() {

    }
}
