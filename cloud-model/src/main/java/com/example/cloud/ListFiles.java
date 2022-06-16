package com.example.cloud;

import lombok.Data;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ListFiles implements CloudMessage {
    private String name;
    private final String path;
    private final List<String> files;

    public ListFiles(Path path) throws IOException {
        files = Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
        this.path = path.toAbsolutePath().toString();
    }

    public void setName(String name) {
        this.name = name;
    }
}
