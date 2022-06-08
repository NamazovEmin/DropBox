package com.example.cloudapplication;

public class StartNetwork {
    public static void main(String[] args) {
        new Thread(() -> new EnterApplication().run()).start();
    }
}
