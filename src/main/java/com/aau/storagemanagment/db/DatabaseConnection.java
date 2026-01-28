package com.aau.storagemanagment.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:storage_manager.db";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL CHECK(role IN ('ADMIN', 'USER'))" +
                ");";

        String createFileModelTable = "CREATE TABLE IF NOT EXISTS file_model (" +
                "filepath TEXT PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "size INTEGER," +
                "created_date TEXT," +
                "category TEXT," +
                "owner_id INTEGER," +
                "FOREIGN KEY(owner_id) REFERENCES users(id)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createFileModelTable);
            
            // Default user insertion removed for First-Run Setup logic

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
