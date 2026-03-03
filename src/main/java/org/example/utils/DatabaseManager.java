package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        // Private constructor for singleton
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = createConnection();
            }
        } catch (SQLException e) {
            logger.error("Error checking connection status: {}", e.getMessage());
        }
        return connection;
    }

    private Connection createConnection() {
        try {
            String driver = config.getDbDriver();
            String url = config.getDbUrl();
            String username = config.getDbUsername();
            String password = config.getDbPassword();

            logger.info("Loading database driver: {}", driver);
            Class.forName(driver);

            logger.info("Connecting to database: {}", url);
            connection = DriverManager.getConnection(url, username, password);

            logger.info("Database connection established successfully");
            return connection;

        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found: {}", e.getMessage());
            throw new RuntimeException("Database driver not found", e);
        } catch (SQLException e) {
            logger.error("Failed to connect to database: {}", e.getMessage());
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing database connection: {}", e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
