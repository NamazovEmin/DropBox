package com.example.cloud;

import lombok.Data;
import java.util.List;

@Data
public class AllMyLinks implements CloudMessage{

    private List<String> linkList;
    private List<String> pathList;

    public AllMyLinks(){

    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    public void setLinkList(List<String> linkList) {
        this.linkList = linkList;
    }

    public List<String> getLinkList() {
        return linkList;
    }

    public List<String> getPathList() {
        return pathList;
    }
}
