package com.example.cloudapplication;

import com.example.cloud.NewDirServer;
import javafx.scene.control.TextField;
import java.io.IOException;

public class NewDirServerController {

    private final Network network = MainApplication.network;
    public TextField newDirName;

    public void makeNewDirServer() throws IOException {
        if (newDirName.getText()!= null) {
            network.write(new NewDirServer(newDirName.getText()));
            newDirName.clear();
        }
    }
}
