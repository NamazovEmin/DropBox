package com.example.cloudapplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class DownloadFromLinkApplication {

    @SneakyThrows
    public DownloadFromLinkApplication() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DownloadFromLink.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage linksViewStage = new Stage();
        linksViewStage.setTitle("LoadFromLink");
        linksViewStage.setScene(scene);
        linksViewStage.initOwner(MainApplication.stage);
        linksViewStage.setX(MainApplication.stage.getX() + 200);
        linksViewStage.setY(MainApplication.stage.getY() + 100);
        linksViewStage.initModality(Modality.WINDOW_MODAL);
        linksViewStage.show();
}
}
