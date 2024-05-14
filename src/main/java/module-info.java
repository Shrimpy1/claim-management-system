module rmit.furtherprog.claimmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires software.amazon.awssdk.auth;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.services.s3;
    requires software.amazon.awssdk.core;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires javafx.swing;


    opens rmit.furtherprog.claimmanagementsystem to javafx.fxml;
    exports rmit.furtherprog.claimmanagementsystem;
    exports rmit.furtherprog.claimmanagementsystem.util;
    opens rmit.furtherprog.claimmanagementsystem.util to javafx.fxml;
}