package Interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationApplication.class.getResource("Main.fxml"));
        Scene sceneMain = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("SaveBox - Hello user name");
        stage.setScene(sceneMain);
        stage.show();

    }
}
