package com.example.cloud;

import lombok.Data;

@Data
public class Reg implements CloudMessage{
    private final String name;
    private final String surname;
    private final Long teNumber;
    private final String email;
    private final String login;
    private final String password;
    private Boolean reg;

    public Reg(String name, String surname, Long teNumber, String email, String login, String password, boolean reg) {
        this.name = name;
        this.surname = surname;
        this.teNumber = teNumber;
        this.email = email;
        this.login = login;
        this.password = password;
        this.reg = reg;
    }


    public String getName() {
        return name;
    }

    public Boolean getReg() {
        return reg;
    }

    public void setReg(Boolean reg) {
        this.reg = reg;
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

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
