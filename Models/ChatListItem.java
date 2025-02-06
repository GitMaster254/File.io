package Models;
public class ChatListItem {
    private String chatId;
    private String lastMessage;

    public ChatListItem(String chatId, String lastMessage) {
        this.chatId = chatId;
        this.lastMessage = lastMessage;
    }

    public String getChatId() {
        return chatId;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
