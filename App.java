import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Util.DatabaseUtil;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseUtil.initializeDatabase();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/Register.fxml"));
            Scene scene = new Scene(loader.load());
            
            primaryStage.setTitle("Chat App");
            primaryStage.setScene(scene);
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
