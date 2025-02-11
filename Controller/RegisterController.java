package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

import Service.EncryptionService;
import Database.DBHelper;

public class RegisterController {

    @FXML
    private TextField usernameField, emailField;

    @FXML 
    private PasswordField passwordField, confirmPasswordField;

    @FXML private Button registerButton ;
    @FXML private Hyperlink loginLink;

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
        }
        //Hash password with salt
        String salt = EncryptionService.generateSalt();
        String hashedPassword = EncryptionService.hashPassword(password, salt);

        //store in database
        if(registerUser(name, email, hashedPassword, salt)){
            showAlert("Success", "User registered successfully");
            goToLogin(event);
        } else{
            showAlert("Error", "Registration failed! Try again.");
        }
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
        String query = "INSERT INTO users (name, email, password, salt) VALUES (?, ?, ?, ?)";
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/login.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
