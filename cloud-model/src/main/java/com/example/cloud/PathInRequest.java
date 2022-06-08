package com.example.cloud;

import lombok.Data;

@Data
public class PathInRequest implements CloudMessage {
    private String fileName;

    public PathInRequest(String fileName){
        this.fileName = fileName;
    }
}
