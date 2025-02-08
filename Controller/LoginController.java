package Controller;

import Database.UserDAO;
import Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private void initialize() {
        // Handle login button click
        loginButton.setOnAction(_ -> login());

        // Navigate to registration page when "Register" is clicked
        registerLink.setOnAction(_ -> openRegisterPage());
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        User user = UserDAO.loginUser(username, password);
        if (user != null) {
            showAlert("Success", "Welcome, " + user.getUsername() + "!");
            //Redirect to the main chat page
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void loadChatPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/ChatPage.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/Register.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
