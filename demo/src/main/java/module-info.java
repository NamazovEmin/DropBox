module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens Interface to javafx.fxml;
    opens starter to javafx.fxml;
    exports Interface;
}