package starter;

import Interface.EnterApplication;
import data.DataBase;
import server.Server;


public class Main {
    public static void main(String[] args){
        new Thread(Server::new).start();
        new Thread(EnterApplication::run).start();
        DataBase.getInstance();
    }
}
