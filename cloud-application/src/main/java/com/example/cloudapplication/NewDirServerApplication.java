package com.example.cloudapplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class NewDirServerApplication {

    @SneakyThrows
    public NewDirServerApplication() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewDirServer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),200,124);
        Stage linksViewStage = new Stage();
        linksViewStage.setTitle("Make new directory");
        linksViewStage.setScene(scene);
        linksViewStage.initOwner(MainApplication.stage);
        linksViewStage.setX(MainApplication.stage.getX() + 200);
        linksViewStage.setY(MainApplication.stage.getY() + 100);
        linksViewStage.initModality(Modality.WINDOW_MODAL);
        linksViewStage.show();
    }
}