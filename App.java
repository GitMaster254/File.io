import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Controller.LoginController;
import Database.DatabaseInitializer;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseInitializer.initializeDatabase();

        try {
            LoginController.setPrimaryStage(primaryStage);
            
            primaryStage.setTitle("Chat App");
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/UI/login.fxml"))));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
