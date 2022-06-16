package com.example.cloud;

import lombok.Data;

@Data
public class FileRequestByLink implements CloudMessage{
    private final String link;

    public FileRequestByLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
