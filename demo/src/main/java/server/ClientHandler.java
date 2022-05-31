package server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ClientHandler {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final String fileAddress = "server_files";


    public ClientHandler( Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        System.out.println(System.getProperty("server_files"));
        sendFileList(fileAddress);
        new Thread(() -> {
            byte[] buf = new byte[256];
            while (true) {
                try {
                    String messageFromClient = inputStream.readUTF();
                    if (messageFromClient.equals(CommonConstants.CLIENT_SEND_FILE)) {
                        String fileName = inputStream.readUTF();
                        long fileLenght = inputStream.readLong();
                        File file = Path.of(fileAddress).resolve(fileName).toFile();
                        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                            for (int i = 0; i < (fileLenght + 255) / 256; i++) {
                                int read = inputStream.read(buf);
                                fileOutputStream.write(buf, 0 , read);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private List<String> getFiles(String dir) {
        String[] list = new File(dir).list();
        assert list != null;
        return Arrays.asList(list);
    }

    private void sendFileList(String fileAddress) throws IOException {
        outputStream.writeUTF(CommonConstants.SEND_LIST);
        List<String> files = getFiles(fileAddress);
        outputStream.writeInt(files.size());
        for(String file : files) {
            outputStream.writeUTF(file);
        }
        outputStream.flush();
    }




}

