package com.example.cloudapplication;

import javafx.scene.control.TextField;
import java.io.File;

public class NewDirClientController {

    public  TextField newDirName;

    public void makeNewDirClient() {
        new File(String.valueOf(MainController.homeDir.resolve(newDirName.getText()))).mkdirs();
    }
}
