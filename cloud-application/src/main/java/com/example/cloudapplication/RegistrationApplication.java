package com.example.cloudapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationApplication.class.getResource("Registration.fxml"));
        Scene sceneEnter = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("SaveBox - Registration");
        stage.setScene(sceneEnter);
        stage.show();
    }
}