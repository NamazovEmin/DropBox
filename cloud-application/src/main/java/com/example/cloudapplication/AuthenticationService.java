package com.example.cloudapplication;

import com.example.cloud.CloudMessage;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AuthenticationService {

    private final ObjectDecoderInputStream is;
    private final ObjectEncoderOutputStream os;
    private final Socket socket;

    public AuthenticationService(int port) throws IOException {
        socket = new Socket("localhost", port);
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
    public void close() throws IOException {
        os.close();
        is.close();
        socket.close();
    }
}

