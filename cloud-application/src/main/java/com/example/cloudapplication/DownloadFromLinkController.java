package com.example.cloudapplication;

import com.example.cloud.FileRequestByLink;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DownloadFromLinkController {

    public TextField downLoadLink;
    private final Network network = MainApplication.network;

    public void downloadFileFromLink() throws IOException {
        if (downLoadLink.getText() != null) {
            network.write(new FileRequestByLink(downLoadLink.getText()));
            downLoadLink.clear();
        }
    }
}
