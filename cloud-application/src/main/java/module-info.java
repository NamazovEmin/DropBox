module com.example.cloudapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.codec;
    requires com.example.model;
    requires lombok;

    opens com.example.cloudapplication to javafx.fxml;
    exports com.example.cloudapplication;
}