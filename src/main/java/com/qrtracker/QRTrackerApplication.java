package com.qrtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.qrtracker.database.DatabaseManager;

@SpringBootApplication
public class QRTrackerApplication {

    public static void main(String[] args) {

        DatabaseManager.initializeDatabase();

        SpringApplication.run(QRTrackerApplication.class, args);
    }
}