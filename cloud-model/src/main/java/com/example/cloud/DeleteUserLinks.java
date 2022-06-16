package com.example.cloud;

public class DeleteUserLinks implements CloudMessage{

    private final Boolean all;
    private String link;

    public DeleteUserLinks(Boolean all) {
        this.all = all;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getAll() {
        return all;
    }

    public String getLink() {
        return link;
    }
}
