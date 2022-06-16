package com.example.cloud;

public class NewDirServer implements CloudMessage {
    private final String dirName;

    public NewDirServer(String dirName) {
        this.dirName = dirName;
    }

    public String getDirName() {
        return dirName;
    }
}
