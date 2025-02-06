package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import java.io.File;

public class ChatController {

    @FXML
    private ListView<String> chatListView;

    @FXML
    private TextArea messageInput;

    public void sendMessage(ActionEvent event) {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            chatListView.getItems().add("You: " + message);
            messageInput.clear();
        }
    }

    public void sendFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            chatListView.getItems().add("File Sent: " + selectedFile.getName());
        }
    }
}
