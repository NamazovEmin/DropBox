package com.example.server.db;

import com.example.cloud.AllMyLinks;
import com.example.cloud.Auth;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final String userNameDB = "root";
    private static final String passwordDB = "Diabolick369";
    private static final String url = "jdbc:mysql://localhost:3306/dropbox";
    private static DataBase instance = null;
    private static Connection conn;
    public static Statement stmt;
    public static PreparedStatement pstmt;


    public DataBase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,userNameDB,passwordDB);
            System.out.println("DataBase Connected");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DataBase getInstance(){
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public static boolean insertNewUserInDataBase(String name, String surName, long telNumber, String email, String login, String password){
        String sql = "INSERT INTO users (login, password, name, surname, telnumber, email) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, surName);
            pstmt.setString(5, String.valueOf(telNumber));
            pstmt.setString(6, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Auth  selectFromUsersByLogin(String login, String password,Auth auth) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                if (rs.getString("password").equals(password)){
                    auth.setName(rs.getString("name"));
                    auth.setSurname(rs.getString("surname"));
                    auth.setTeNumber(Long.parseLong(rs.getString("telnumber")));
                    auth.setEmail(rs.getString("email"));
                    auth.setAccess(true);
                    return auth;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auth;
    }

    public static boolean checkLink(StringBuilder fileLink) {
        String link = fileLink.toString();
        String sql = "SELECT * FROM links WHERE link = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, link);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                if (rs.getString("link").equals(link)){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }

    public static boolean insertLink(String login, String fileLink, String path) {
        String sql = "INSERT INTO links (link, path, users_id)" +
                "VALUES (?, ?, " +
                "(select id " +
                "from users " +
                "where login = ?))";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fileLink);
            pstmt.setString(2, path);
            pstmt.setString(3, login);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Path findLink(String link) {
        String sql = "SELECT * FROM links WHERE link = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, link);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                if (rs.getString("link").equals(link)){
                    return Path.of(rs.getString("path"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static AllMyLinks allLinksByLogin(String login, AllMyLinks allMyLinks) {

        String sql = "select * from links where users_id = " +
                "(select id from users where login = ?)";
        try {
            pstmt  = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            List<String> link = new ArrayList<>();
            List<String> path = new ArrayList<>();
            while (rs.next()) {
                link.add(rs.getString("link"));
                path.add(rs.getString("path"));
            }
            allMyLinks.setPathList(path);
            allMyLinks.setLinkList(link);
            return allMyLinks;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void deleteCurrentLink(String link) {
        String sql = "delete from links where link = ? " +
                "limit 1";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, link);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void deleteAllLink(String login) {
        String sql = "delete from links where users_id = " +
                "(select id from users where login = ?)" +
                "limit 1000";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}