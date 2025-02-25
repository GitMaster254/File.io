package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import Service.EncryptionService;
import Database.DBHelper;

public class RegisterController {

    @FXML
    private TextField usernameField, emailField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    public void handleRegister(ActionEvent event) {
        String name = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields are required");
            return;
        } if(!isValidEmail(email)) {
            showAlert("Error", "Invalid email format");
            return;
        } if(!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match");
            return;
        } if(!isValidPassword(password)){
            showAlert("Error", "Password must be at least 8 characters long and contain letters and numbers");
        }
        //Hash password with salt
        String salt = EncryptionService.generateSalt();
        String hashedPassword = EncryptionService.hashPassword(password, salt);

        //store in database
        if(registerUser(name, email, hashedPassword, salt)){
            showAlert("Success", "User registered successfully");
            goToLogin(event);
        } else{
            showAlert("Error", "An expected error occured. Please try again.");
        }
        
    }
    private boolean isValidPassword(String password){
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");
    }
    //validate email
    private boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return Pattern.matches(emailRegex, email);
    }

     private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }  
    
        private boolean registerUser(String name, String email, String hashedPassword, String salt) {
        String query = "INSERT INTO users (username, email, password, salt) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, salt);

            return pstmt.executeUpdate() > 0; // Returns true if insertion is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void goToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UI/login.fxml"));
            LoginController.getPrimaryStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
