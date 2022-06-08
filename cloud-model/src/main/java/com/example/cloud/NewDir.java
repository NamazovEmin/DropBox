package com.example.cloud;

public class NewDir implements CloudMessage {
    private final String dirName;

    public NewDir(String dirName) {
        this.dirName = dirName;
    }

    public String getDirName() {
        return dirName;
    }
}
