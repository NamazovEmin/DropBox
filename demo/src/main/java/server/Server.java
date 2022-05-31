package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public Server(){
    try (ServerSocket server = new ServerSocket(CommonConstants.SERVER_PORT)) {
        while (true) {
            System.out.println("Server Start");
            System.out.println("Server wait for connect");
            Socket socket = server.accept();
            new ClientHandler(socket);
            System.out.println("User Connected");
        }
    } catch (IOException exception) {
        System.out.println("Ошибка в работе сервера");
        exception.printStackTrace();
    }
}
}
