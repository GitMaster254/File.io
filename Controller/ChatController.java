package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import java.io.File;
import Models.Message;
import Service.ChatService;
import Service.FileService;
import Service.SessionManager;
import Models.FileMessage;
import javafx.geometry.Pos;

public class ChatController {

    @FXML private ListView<HBox> chatListView;  // Displays messages dynamically
    @FXML private ListView<Text> activeChatList; // Left panel with active chats
    @FXML private TextArea messageInput;
    @FXML private Button sendButton;
    @FXML private Button attachFileButton;

    private ChatService chatservice = new ChatService();

    @FXML
    private void initialize(){
        loadActiveChats();
        sendButton.setOnAction(_ -> sendMessage());
        attachFileButton.setOnAction(_ -> sendFile());
    }

    //handle file download when clicked
    public void onFileClicked(FileMessage fileMessage) {
        //get the file from the storage dir
        File receivedFile = FileService.getFile(fileMessage.getFileName());
        if (receivedFile != null) {
            System.err.println("Downloading:" + receivedFile.getAbsolutePath());

            //open the file in the default application
            openFile(receivedFile);
        }else{
            System.err.println("File not found");
        }
    }

    //helper method to open a file(windows, macOS , LInux support)
    private void openFile(File file) {
        try{
            if(file.exists()){
                java.awt.Desktop.getDesktop().open(file);
            }else{
                System.err.println("File does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Text msgText = new Text(message.getSender() + ": " + message.getContent());
        msgText.setFill(message.getSender().equals("Me") ? Color.GREEN : Color.BLACK);
        TextFlow textFlow = new TextFlow(msgText);
        textFlow.setStyle("-fx-background-color: #e3e3e3; -fx-padding: 8px; -fx-background-radius: 10;");
        
        HBox messageBox = new HBox(textFlow);
        messageBox.setAlignment(message.getSender().equals("Me") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        chatListView.getItems().add(messageBox);
    }

    private void sendFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Send");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String sender = SessionManager.getLoggedInUser();
            String receiver = getSelectedReceiver();

            if(sender == null || receiver == null){
                System.err.println("Users not logged in");
                return;
            }

            FileMessage filemessage = FileService.shareFile(file, sender);

            if(filemessage != null){
                chatservice.sendMessage(filemessage);
                addMessageToChat(filemessage);
            } else {
                System.err.println("Failed to share file");
            }
        }
    }

    private String getSelectedReceiver() {
        Text selectedChat = activeChatList.getSelectionModel().getSelectedItem();
        return selectedChat != null ? selectedChat.getText() : null;
    }
}