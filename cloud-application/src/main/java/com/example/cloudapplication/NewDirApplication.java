package com.example.cloudapplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class NewDirApplication{

    @SneakyThrows
    public NewDirApplication() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationApplication.class.getResource("NewDir.fxml"));
        Scene sceneEnter = new Scene(fxmlLoader.load());
        stage.setScene(sceneEnter);
        stage.show();
    }
}