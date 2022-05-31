package сlient;

public class User {
    public String login;
    public String password;
    public String email;
    public String name;
    public String surName;
    public int telNumber;

    public User (String login , String password, String email, String name, String surName, int telNumber ){
        this.login = login;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surName = surName;
        this.telNumber = telNumber;
    }

}
