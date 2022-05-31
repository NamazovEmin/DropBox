package Interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EnterApplication extends Application {
    public static Stage frontStage;

    public static void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        frontStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(EnterApplication.class.getResource("Enter.fxml"));
        Scene sceneEnter = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("SaveBox - Enter");
        stage.setScene(sceneEnter);
        stage.show();
    }
}