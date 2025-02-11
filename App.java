import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Database.DatabaseInitializer;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseInitializer.initializeDatabase();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/Register.fxml"));
            Scene scene = new Scene(loader.load());
            
            primaryStage.setTitle("Chat App");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
