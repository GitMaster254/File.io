package Controller;

import Database.UserDAO;
import Models.User;
import Service.EncryptionService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private CheckBox showPasswordCheckBox;

    @FXML
    private void initialize() {
        //handle view password field
        showPasswordCheckBox.setOnAction(event ->{
            if(showPasswordCheckBox.isSelected()){
                passwordField.setPromptText(passwordField.getText());
                passwordField.clear();
            } else {
            passwordField.setText(passwordField.getPromptText());
            passwordField.setPromptText("Password");
            }
        });
        // Handle login button click
        loginButton.setOnAction(_ -> login());

        // Navigate to registration page when "Register" is clicked
        registerLink.setOnAction(_ -> openRegisterPage());
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        //retrieve user form database
        User user = UserDAO.getUserByUsername(username);

        if (user != null) {
            //verify hashed password
            if(EncryptionService.verifyPassword(password, user.getPassword(), user.getSalt())){
                showAlert("Success", "Welcome," + user.getUsername() + "!");
                openChatPage();
            }else{
                showAlert("Login Failed", "Invalid username or password");
            }
            
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void openRegisterPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI/Register.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Register");
            stage.setScene(new Scene(fxmlLoader.load(), 400, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open registration page.");
        }
    }


    private void openChatPage(){
        try{
            FXMLLoader fxmlLoader= new FXMLLoader( getClass().getResource("/UI/ChatPage.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch(IOException e){
            e.printStackTrace();
            showAlert("Error", "Failed to open chat page");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
