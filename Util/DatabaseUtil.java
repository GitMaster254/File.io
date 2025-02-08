package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    // SQLite database URL (will create a file named chatapp.db in the project folder)
    private static final String DB_URL = "jdbc:sqlite:chatapp.db";

    // Get a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Initialize the database tables if they don't exist
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Create "users" table for registration/login
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username TEXT UNIQUE NOT NULL,"
                    + "password TEXT NOT NULL"
                    + ");";

            // Create "messages" table for chat history
            String createMessagesTable = "CREATE TABLE IF NOT EXISTS messages ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "sender TEXT NOT NULL,"
                    + "content TEXT,"
                    + "file_name TEXT,"
                    + "file_path TEXT,"
                    + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ");";

            Statement stmt = conn.createStatement();
            stmt.execute(createUsersTable);
            stmt.execute(createMessagesTable);
            System.out.println("Database initialized.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

