package com.example.cloudapplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class LinkViewApplication {

    @SneakyThrows

    public LinkViewApplication() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UsersLinks.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1000,400);
        Stage linksViewStage = new Stage();
        linksViewStage.setTitle("Your links");
        linksViewStage.setScene(scene);
        linksViewStage.initOwner(MainApplication.stage);
        linksViewStage.setX(MainApplication.stage.getX() + 200);
        linksViewStage.setY(MainApplication.stage.getY() + 100);
        linksViewStage.initModality(Modality.WINDOW_MODAL);
        linksViewStage.show();
    }
}
