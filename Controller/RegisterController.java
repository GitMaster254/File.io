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

import Service.EncryptionService;
import Database.DBHelper;

public class RegisterController {

    @FXML
    private TextField nameField, emailField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    public void handleRegister(ActionEvent event) {
        String name = nameField.getText();
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
        } else {
            System.out.println("Registration Failed");
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
