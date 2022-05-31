package data;

import java.sql.*;

public class DataBase {
    private static final String userNameDB = "root";
    private static final String passwordDB = "Diabolick369";
    private static final String url = "jdbc:mysql://localhost:3306/dropbox";
    private static DataBase instance;
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
        String sql = "INSERT INTO users (login, password, name, surname, telnumber, email)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
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

    public static String selectFromUsersByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}