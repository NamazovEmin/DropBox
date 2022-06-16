package com.example.cloudapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    public static Network network;
    public static Stage stage;
    public MainApplication(Network network) {
        MainApplication.network = network;
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {
        Button button = new Button();
        button.setVisible(false);
        StackPane root = new StackPane();
        root.getChildren().add(button);
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Welcome to SaveBox " + network.getSurname() + " " + network.getName());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}