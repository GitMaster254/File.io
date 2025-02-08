package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String DATABASE_URL = "jdbc:sqlite:chat_app.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
             
            // Enable foreign keys (optional)
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Create users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT UNIQUE NOT NULL, "
                    + "password TEXT NOT NULL, "
                    + "email TEXT UNIQUE);";
            stmt.execute(createUsersTable);

            // Create messages table
            String createMessagesTable = "CREATE TABLE IF NOT EXISTS messages ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "sender TEXT NOT NULL, "
                    + "receiver TEXT NOT NULL, "
                    + "content TEXT, "
                    + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);";
            stmt.execute(createMessagesTable);

            // Create files table
            String createFilesTable = "CREATE TABLE IF NOT EXISTS files ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "sender TEXT NOT NULL, "
                    + "receiver TEXT NOT NULL, "
                    + "file_name TEXT NOT NULL, "
                    + "file_path TEXT NOT NULL, "
                    + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);";
            stmt.execute(createFilesTable);

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
}
