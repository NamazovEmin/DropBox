package com.example.cloud;

import lombok.Data;

@Data
public class Auth implements CloudMessage{

    private String name;
    private String surname;
    private Long teNumber;
    private String email;
    private final String login;
    private final String password;
    private boolean access;

    public Auth(String login, String password, boolean access) {
        this.login = login;
        this.password = password;
        this.access = access;
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setTeNumber(Long teNumber) {
        this.teNumber = teNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Long getTeNumber() {
        return teNumber;
    }

    public String getEmail() {
        return email;
    }

}
