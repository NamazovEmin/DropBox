package com.example.cloudapplication;

import com.example.cloud.AllMyLinks;
import com.example.cloud.DeleteUserLinks;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LinkViewController implements Initializable {
    private final Network network = MainApplication.network;
    public ListView<String> allPathView;
    public TextField linkField;
    AllMyLinks file = MainController.linkFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allPathView.getItems().addAll(file.getPathList());

    }

    public void takeLink() {
        if (allPathView.getSelectionModel().getSelectedItem() != null) {
            ObservableList<Integer> a = allPathView.getSelectionModel().getSelectedIndices();
            linkField.clear();
            linkField.setText(file.getLinkList().get(a.get(0)));
        }
    }

    public void deleteLink() throws IOException {
        if (allPathView.getSelectionModel().getSelectedItem() != null) {
            ObservableList<Integer> a = allPathView.getSelectionModel().getSelectedIndices();
            int rowNumber = a.get(0);
            DeleteUserLinks deleteUserLinks = new DeleteUserLinks(false);
             deleteUserLinks.setLink(file.getLinkList().get(a.get(0)));
            network.write(deleteUserLinks);
            file.getLinkList().remove(rowNumber);
            file.getPathList().remove(rowNumber);
            Platform.runLater(() -> {
                allPathView.getItems().clear();
                allPathView.getItems().addAll(file.getPathList());
            });
        }
    }

    public void deleteAllLink() throws IOException {
            network.write(new DeleteUserLinks(true));
            file.getLinkList().clear();
            file.getPathList().clear();
        Platform.runLater(() -> allPathView.getItems().clear());
    }
}
