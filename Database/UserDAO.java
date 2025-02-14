package Database;

import Models.User;
import Service.EncryptionService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Register a new user with username, hashed password, salt, and email
    public static boolean registerUser(String username, String hashedPassword, String salt, String email) {
        String query = "INSERT INTO users (username, password, salt, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, salt);
            pstmt.setString(4, email);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    // Validate credentials by comparing entered password with stored hash and salt
    public static boolean validateCredentials(String username, String enteredPassword) {
        String query = "SELECT password, salt FROM users WHERE username = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Retrieve stored hash and salt
                String storedHash = rs.getString("password");
                String storedSalt = rs.getString("salt");

                // Verify the entered password using the EncryptionService
                return EncryptionService.verifyPassword(enteredPassword, storedHash, storedSalt);
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    // Get a user by username
    public static User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"), // hashed password
                        rs.getString("salt") // salt
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // User not found
    }
}
