package com.example.cloud;

public class ShareFile implements CloudMessage {
    private final String fileName;
    private  String link;

    public ShareFile(String fileName) {
        this.fileName = fileName;
    }


    public String getFileName() {
        return fileName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
