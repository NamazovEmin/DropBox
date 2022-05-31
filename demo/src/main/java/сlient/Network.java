package сlient;

import Interface.MainController;
import server.CommonConstants;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Network {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final MainController controller;
    String fileAddress = System.getProperty("user.home");
    private byte[] buf;

    public Network(MainController mainController) throws IOException {
    this.controller = mainController;
    initializeNetwork();
    mainController.userPanel.getItems().clear();
    mainController.userPanel.getItems().addAll(getFiles(fileAddress));

    new Thread(() -> {
        buf = new byte[256];
        System.out.println("User can connected");
        System.out.println(System.getProperty("user.home"));
        try {
            while (true) {
                String messageFromServer = inputStream.readUTF();
                if (messageFromServer.equals(CommonConstants.SEND_LIST)) {
                    controller.serverPanel.getItems().clear();
                    int len = getInt();
                    for (int i = 0; i < len; i++) {
                        String file = inputStream.readUTF();
                        controller.serverPanel.getItems().add(file);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }).start();

    }



    private void initializeNetwork() throws IOException {
        socket = new Socket(CommonConstants.SERVER_ADDRESS, CommonConstants.SERVER_PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    private List<String> getFiles(String dir) {
        String[] list = new File(dir).list();
    assert list != null;
    return Arrays.asList(list);
}

    public int getInt() throws IOException {
        return inputStream.readInt();
}

    public void sendFile() throws IOException {
        outputStream.writeUTF("#filesend");
        String fileName = controller.userPanel.getSelectionModel().getSelectedItem();
        outputStream.writeUTF(fileName);
        File file =  Path.of(fileAddress).resolve(fileName).toFile();
        outputStream.writeLong(file.length());
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            while (fileInputStream.available() > 0) {
                int read = fileInputStream.read(buf);
                outputStream.write(buf, 0, read);
            }
        }
        outputStream.flush();
    }




}
