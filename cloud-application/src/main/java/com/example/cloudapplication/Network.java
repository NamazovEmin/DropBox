package com.example.cloudapplication;

import com.example.cloud.CloudMessage;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {

    private final String name;
    private final String surname;
    private final Long teNumber;
    private final String email;
    private final String login;
    private final String password;
    private final ObjectDecoderInputStream is;
    private final ObjectEncoderOutputStream os;

    public Network(int port, String name, String surname, Long teNumber, String email, String login, String password) throws IOException {
        System.out.println("new User");
        this.surname = surname;
        this.name = name;
        this.teNumber = teNumber;
        this.email = email;
        this.login = login;
        this.password = password;
        Socket socket = new Socket("localhost", port);
        os = new ObjectEncoderOutputStream(socket.getOutputStream());
        is = new ObjectDecoderInputStream(socket.getInputStream());
    }

    public CloudMessage read() throws IOException, ClassNotFoundException {
        return (CloudMessage) is.readObject();
    }

    public void write(CloudMessage msg) throws IOException {
        os.writeObject(msg);
        os.flush();
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }
}
