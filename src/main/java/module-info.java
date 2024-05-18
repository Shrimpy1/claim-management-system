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
    exports rmit.furtherprog.claimmanagementsystem.database;
    exports rmit.furtherprog.claimmanagementsystem.data.model.customer;
    exports rmit.furtherprog.claimmanagementsystem.data.model.admin;
    exports rmit.furtherprog.claimmanagementsystem.data.model.prop;
    exports rmit.furtherprog.claimmanagementsystem.data.model.provider;
    exports rmit.furtherprog.claimmanagementsystem.data.model.util;
    exports rmit.furtherprog.claimmanagementsystem.service;
    exports rmit.furtherprog.claimmanagementsystem.exception;
    exports rmit.furtherprog.claimmanagementsystem.controller;
    opens rmit.furtherprog.claimmanagementsystem.controller to javafx.fxml;
}