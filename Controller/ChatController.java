package Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import Models.Message;
import Networking.FileSender;
import Networking.TransferListener;
import Service.ChatService;
import Service.FileService;
// import Service.SessionManager;
import Util.EncryptionUtil;
import Models.FileMessage;
import javafx.geometry.Pos;

public class ChatController {

    @FXML private ListView<HBox> chatListView;  // Displays messages dynamically
    @FXML private ListView<Text> activeChatList; // Left panel with active chats
    @FXML private TextArea messageInput;
    @FXML private Button sendButton;
    @FXML private Button attachFileButton;

    private ChatService chatservice = new ChatService();
    private static final String serverIP = "10.0.0.1";
    private static final int port = 360;

    @FXML
    private void initialize(){
        loadActiveChats();
        sendButton.setOnAction(_ -> sendMessage());
        attachFileButton.setOnAction(_ -> sendFile(null));
    }

    //handle file download when clicked
    public void onFileClicked(FileMessage fileMessage) {
        //get the encrypted file from the storage dir
        File encryptedFile = FileService.getFile(fileMessage.getFileName() + ".enc");
        if (encryptedFile != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Decrypted File");
            fileChooser.setInitialFileName(fileMessage.getFileName());
            fileChooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*.*"));

            File saveLocation =fileChooser.showOpenDialog(chatListView.getScene().getWindow());
            if(saveLocation != null){
                try{
                    //decrypt using stream before saving
                    EncryptionUtil.decryptFile(encryptedFile, saveLocation);
                    System.out.println("File decrypted and saved to:" + saveLocation.getAbsolutePath());
                }catch(IOException e){
                    e.printStackTrace();
                    showError("Failed to save file");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                showError("Save location not selected!");
            }
        }

    }

    //error handling
    private void showError(String Error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(Error);
        alert.showAndWait();
    }


    private void loadActiveChats(){
        activeChatList.getItems().clear();
        chatservice.getActiveChats().forEach(chat ->{
            Text chatText = new Text(chat);
            chatText.setOnMouseClicked(_ -> loadChatHistory(chat));
            activeChatList.getItems().add(chatText);
        });
    }

    private void loadChatHistory(String chatId) {
        chatListView.getItems().clear();
        for (Message msg : chatservice.getChatHistory(chatId)) {
            addMessageToChat(msg);
        }
    }

    private void sendMessage() {
        String text = messageInput.getText().trim();
        if (!text.isEmpty()) {
            Message message = new Message("Me", text);
            chatservice.sendMessage(message);
            addMessageToChat(message);
            messageInput.clear();
        }
    }

    private void addMessageToChat(Message message) {
        TextFlow textFlow;
        if(message instanceof FileMessage){
            FileMessage fileMessage = (FileMessage) message;

            Text fileText = new Text("[File:" + fileMessage.getFileName() + "](Click to Download)");
            fileText.setFill(Color.BLUE);
            fileText.setUnderline(true);

            //click event to download file
            fileText.setOnMouseClicked(_ -> onFileClicked(fileMessage));

            textFlow = new TextFlow(fileText);
        }else{
            Text msgText = new Text(message.getSender() + ":" + message.getContent());
            msgText.setFill(message.getSender().equals("Me") ? Color.GREEN : Color.BLACK);
            textFlow = new TextFlow(msgText);
        }

        textFlow.setStyle("-fx-background-color: #e3e3e3; -fx-padding: 8px; -fx-background-radius: 10;");
        
        HBox messageBox = new HBox(textFlow);
        messageBox.setAlignment(message.getSender().equals("Me") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        chatListView.getItems().add(messageBox);
    }

    private void sendFile(File file) {
        try{
            Socket socket = new Socket(serverIP, port);
            OutputStream outputStream = socket.getOutputStream();

            FileSender.sendFile(file, outputStream,new TransferListener() {
                ProgressBar progressBar = new ProgressBar();
                @Override
                public void onProgress(int percentage){
                    Platform.runLater(() -> progressBar.setProgress(percentage / 100.0));
                }

                @Override
                public void onComplete(String message){
                    Platform.runLater(() -> showInfo(message));
                }

                @Override
                public void onError(String errorMessage){
                    Platform.runLater(() -> showError(errorMessage));
                }
            });
            socket.close();
        } catch(IOException e){
            showError("Failed to send file:" + e.getMessage());
        }
        
    }
    private void showInfo( String message){

    }

}