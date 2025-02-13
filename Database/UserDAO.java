package Database;

import Models.User;
import Service.EncryptionService;

import java.sql.*;

public class UserDAO {
    // Register a new user
    public static boolean registerUser(String username, String password, String email) {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

                //Generate a salt and hash password
                String salt =EncryptionService.generateSalt();
                String hashedPassword = EncryptionService.hashPassword(password, salt);

             pstmt.setString(1, username);
             pstmt.setString(2, hashedPassword); 
             pstmt.setString(3,salt);
             pstmt.setString(4, email);

             int rowsInserted = pstmt.executeUpdate();
             return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    // Authenticate user (Login)
    public static User loginUser(String username, String enteredPassword) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){ 
            //retrive stored hash and salt
            String storedHash = rs.getString("password");
            String storedSalt = rs.getString("salt");

            //Hash the entered password using stored salt
            String enteredHash = EncryptionService.hashPassword(enteredPassword, storedSalt);

            //compare the hashes
            if(storedHash.equals(enteredHash)){
                //Authentication successful
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("salt")
                );
            }
        }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }

    public static User getUserByUsername(String username){
        String query = "SELECT * FROM users WHERE username = ?";

        try(Connection conn = DBHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"), //hashed password
                rs.getString("salt") //salt
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null; //user not found
    }
}

