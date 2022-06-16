package com.example.cloudapplication;

import com.example.cloud.ShareFile;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;


public class NewLinkServerApplication {
    @SneakyThrows

    public NewLinkServerApplication(ShareFile shareFile) {
        String title = "Your file " + shareFile.getFileName() + " shared";
        TextField textField = new TextField();
        textField.setText(shareFile.getLink());
        Stage stage = new Stage();
        Label label = new Label(title);
        VBox vBox = new VBox(20,label,textField);
        vBox.setAlignment(Pos.CENTER);
        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(vBox);
        Scene scene = new Scene(flowPane, 170, 80);
        stage.setScene(scene);
        stage.setTitle("Shared File");
        stage.initOwner(MainApplication.stage);
        stage.setX(MainApplication.stage.getX() + 200);
        stage.setY(MainApplication.stage.getY() + 100);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }
}
