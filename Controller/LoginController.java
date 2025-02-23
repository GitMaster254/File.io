package Controller;

import Database.UserDAO;
import Models.User;
import Service.EncryptionService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private static Stage primaryStage;
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

    public static void setPrimaryStage(Stage stage){
        primaryStage =stage;
    }
    public static Stage getPrimaryStage(){
        return primaryStage;
    }

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
        loadScene("/UI/Register.fxml", "Create Account");

    }


    private void openChatPage(){
        loadScene("/UI/ChatPage.fxml", "Chat Page");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

        @FXML
        public void loadScene(String fxmlFile, String title) {
        if (primaryStage == null) {
            System.err.println("Primary stage is not set. Please call setPrimaryStage() before attempting scene transitions.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
