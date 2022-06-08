package com.example.cloud;

public class NetworkDirectory implements CloudMessage {
    private final String login;

    public NetworkDirectory(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
