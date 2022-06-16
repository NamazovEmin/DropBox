package com.example.cloudapplication;

import com.example.cloud.*;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController implements Initializable {

    public TextField clientPath;
    public TextField serverPath;
    public ListView<String> clientView;
    public ListView<String> serverView;
    public static AllMyLinks linkFile;
    static Path homeDir = Path.of(System.getProperty("user.home"));
    private final Network network = MainApplication.network;

    private void readLoop() {
        try {
            while (true) {
                CloudMessage message = network.read();
                System.out.println("here we work to");
                if (message instanceof ListFiles listFiles) {
                    Platform.runLater(() -> {
                        serverView.getItems().clear();
                        serverView.getItems().addAll(listFiles.getFiles());
                        serverPath.setText(listFiles.getPath());
                    });
                } else if (message instanceof FileMessage fileMessage) {
                    Path current = homeDir.resolve(fileMessage.getName());
                    Files.write(current, fileMessage.getData());
                    Platform.runLater(() -> {
                        clientView.getItems().clear();
                        clientView.getItems().addAll(getFiles(homeDir.toString()));
                    });
                } else if (message instanceof  ShareFile shareFile) {
                    Platform.runLater(() -> new NewLinkServerApplication(shareFile));
                } else if (message instanceof AllMyLinks allMyLinks) {
                    linkFile = allMyLinks;
                    Platform.runLater(LinkViewApplication::new);
                } else if (message instanceof DirectoryAndFileMessage directoryAndFileMessage) {
                    for (int i = 0; i < directoryAndFileMessage.getDirName().size(); i++) {
                        new File(String.valueOf(homeDir.resolve(directoryAndFileMessage.getDirName().get(i)))).mkdirs();
                    }
                    AtomicBoolean a = new AtomicBoolean();
                    for (int i = 0; i < directoryAndFileMessage.getFileName().size(); i++) {
                        a.set(true);
// хотел сделать подтверждение на замену файлов, но не смог понять, как поставить на ожидание.
// если это возможно, подскажите пожалуйста
//                        if (Files.exists(homeDir.resolve(directoryAndFileMessage.getFileName().get(i)))) {
//                            Platform.runLater(() -> {
//                                Thread newThread = Thread.currentThread();
//                                Stage stage = new Stage();
//                                Button button = new Button("нет");
//                                VBox vBox = new VBox(button);
//                                vBox.setAlignment(Pos.CENTER);
//                                FlowPane flowPane = new FlowPane();
//                                flowPane.getChildren().addAll(vBox);
//                                Scene scene = new Scene(flowPane, 170, 80);
//                                stage.setScene(scene);
//                                stage.setTitle("Shared File");
//                                stage.initOwner(MainApplication.stage);
//                                stage.setX(MainApplication.stage.getX() + 200);
//                                stage.setY(MainApplication.stage.getY() + 100);
//                                stage.initModality(Modality.WINDOW_MODAL);
//                                stage.show();
//
//                                button.setOnAction(actionEvent -> {
//                                    a.set(false);
//                                  Stage stage1 = (Stage) button.getScene().getWindow();
//                                  stage1.close();
//                                });
//                            });
//                        }
                        if (a.get()) {
                            Files.write(homeDir.resolve(directoryAndFileMessage.getFileName().get(i)),
                                    directoryAndFileMessage.getDataList().get(i));
                        }
                    }
                    Platform.runLater(() -> {
                        clientView.getItems().clear();
                        clientView.getItems().addAll(getFiles(homeDir.toString()));
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Connection lost");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            clientPath.setText(homeDir.toString());
            clientView.getItems().clear();
            clientView.getItems().addAll(getFiles(homeDir.toString()));
            Thread readThread = new Thread(this::readLoop);
            readThread.setDaemon(true);
            readThread.start();
            network.write(new NetworkDirectory(network.getLogin()));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private List<String> getFiles(String dir) {
        String[] list = new File(dir).list();
        assert list != null;
        return Arrays.asList(list);
    }

    public void upload() throws IOException {
        String file = clientView.getSelectionModel().getSelectedItem();
        Path testPath = homeDir.resolve(file);
        if (Files.isDirectory(homeDir.resolve(file))) {
            List<Path> fileList = new ArrayList<>();
            List<Path> dirList = new ArrayList<>();
            List<Path> buffList = new ArrayList<>();
            List<Path> buffDir = new ArrayList<>();
            buffDir.add(testPath);
            while (true) {
                for (Path path : buffDir) {
                    dirList.add(path);
                    if (Files.list(path) != null) {
                        buffList.addAll(Files.list(path).toList());
                    }
                }
                buffDir.clear();
                for (Path path : buffList) {
                    if (Files.isDirectory(path)) {
                        buffDir.add(path);
                    } else {
                        fileList.add(path);
                    }
                }
                buffList.clear();
                if (buffDir.size() == 0) {
                    break;
                }
            }
            network.write(new DirectoryAndFileMessage(dirList,homeDir,fileList));
        } else {
            network.write(new FileMessage(homeDir.resolve(file)));
        }
    }

    public void download() throws IOException {
        String file = serverView.getSelectionModel().getSelectedItem();
        network.write(new FileRequest(file));
    }

    public void pathInRequestClients(MouseEvent mouseEvent) {
        try {
            if ( mouseEvent.getClickCount() == 2 ){
                String fileName = clientView.getSelectionModel().getSelectedItem();
                if (Files.isDirectory(homeDir.resolve(fileName))){
                    homeDir = homeDir.resolve(fileName);
                    System.out.println(homeDir);
                    clientView.getItems().clear();
                    clientView.getItems().addAll(getFiles(homeDir.toString()));
                    clientPath.clear();
                    clientPath.setText(homeDir.toString());
                }
            }
        } catch (RuntimeException ignored){
        }
    }

    public void pathUpRequestClient() {
            if (homeDir.getParent() != null) {
                homeDir = homeDir.getParent();
                System.out.println(homeDir);
                clientView.getItems().clear();
                clientView.getItems().addAll(getFiles(homeDir.toString()));
                clientPath.clear();
                clientPath.setText(homeDir.toString());
            }
    }

    public void pathUpRequestServer() throws IOException {
        network.write(new PathUpRequest());
    }

    public void pathInRequestServer(MouseEvent mouseEvent) throws IOException {
        try {
            if (mouseEvent.getClickCount() == 2 ){
                String fileName = serverView.getSelectionModel().getSelectedItem();
                network.write(new PathInRequest(fileName));
             }
          }catch (RuntimeException ignored){
        }
    }

    public void goClientPath() {
        if (Files.isDirectory(Path.of(clientPath.getText()))) {
            homeDir = Path.of(clientPath.getText()).toAbsolutePath();
            clientView.getItems().clear();
            clientView.getItems().addAll(getFiles(homeDir.toString()));
            clientPath.clear();
            clientPath.setText(homeDir.toString());
        }
    }

    public void goServerPath() throws IOException {
        String file = serverPath.getText();
        network.write(new PathFindRequest(file));
    }


    public void newDirServer() {
        new NewDirServerApplication();
    }

    public void newDirClient() {
        Stage stage = new Stage();
        Platform.runLater(() -> new NewDirClientApplication(stage));
        stage.setOnCloseRequest(windowEvent -> {
            clientView.getItems().clear();
            clientView.getItems().addAll(getFiles(homeDir.toString()));
        });
    }

    public void shareFile() throws IOException {
        String fileName = serverView.getSelectionModel().getSelectedItem();
        network.write(new ShareFile(fileName));
    }

    public void downloadFromLink() {
        Platform.runLater(DownloadFromLinkApplication::new);
    }

    public void loadAllMyLinks() throws IOException {
        network.write(new AllMyLinks());
    }
}