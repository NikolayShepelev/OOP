package org.investment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnection {
    private static PostgreSQLConnection instance;
    private Connection connection;

    private PostgreSQLConnection() {
        try {
            String url = "jdbc:postgresql://localhost:5432/investment_db";
            String user = "postgres";
            String password = "postpass";
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public static synchronized PostgreSQLConnection getInstance() {
        if (instance == null) {
            instance = new PostgreSQLConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}