package com.example.cloudapplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class NewDirClientApplication {
    @SneakyThrows
    public NewDirClientApplication(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewDirClient.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),200,124);
        stage.setTitle("Make new directory");
        stage.setScene(scene);
        stage.initOwner(MainApplication.stage);
        stage.setX(MainApplication.stage.getX() + 200);
        stage.setY(MainApplication.stage.getY() + 100);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }
}
