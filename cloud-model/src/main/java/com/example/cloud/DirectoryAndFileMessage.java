package com.example.cloud;

import lombok.Data;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Data

public class DirectoryAndFileMessage implements CloudMessage {

    private final List<String> dirName = new ArrayList<>();
    private final List<Long> sizeList = new ArrayList<>();
    private final List<byte[]> dataList = new ArrayList<>();
    private final List<String > fileName = new ArrayList<>();

    public DirectoryAndFileMessage(List<Path> dirList,Path serverPath, List<Path> fileList) throws IOException {
        this.dirName.addAll(newDirList(dirList,serverPath));
        this.sizeList.addAll(newSizeList(fileList));
        this.dataList.addAll(newDataList(fileList));
        this.fileName.addAll(newFileNameList(fileList,serverPath));
    }

    private  List<String> newDirList(List<Path> dirList, Path serverPath) {
        List<String> test = new ArrayList<>();
        int a = serverPath.toString().length() + 1;
        for (Path path : dirList) {
            test.add(path.toString().substring(a));
        }
        return test;
    }

    private List<Long> newSizeList(List<Path> fileList) throws IOException {
        List<Long> test = new ArrayList<>();
        for (Path path : fileList) {
            test.add(Files.size(path));
        }
        return test;
    }

    private List<byte[]> newDataList(List<Path> fileList) throws IOException {
        List<byte[]> test = new ArrayList<>();
        for (Path path : fileList) {
            test.add(Files.readAllBytes(path));
        }
        return test;
    }

    private  List<String> newFileNameList(List<Path> fileList, Path serverPath) {
        List<String> test = new ArrayList<>();
        int a = serverPath.toString().length() + 1;
        for (Path path : fileList) {
            test.add(path.toString().substring(a));
        }
        return test;
    }
}
