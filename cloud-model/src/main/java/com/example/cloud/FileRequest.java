package com.example.cloud;

import lombok.Data;

@Data
public class FileRequest implements CloudMessage {

    private final String name;

}
