package Service;

import java.util.*;

import Models.Message;

public class ChatService {
    
    private Map<String, List<Message>> chatData = new HashMap<>();

    public List<String> getActiveChats() {
        return new ArrayList<>(chatData.keySet());
    }

    public List<Message> getChatHistory(String chatId) {
        return chatData.getOrDefault(chatId, new ArrayList<>());
    }

    public void sendMessage(Message message) {
        chatData.computeIfAbsent("General", k -> new ArrayList<>()).add(message);
    }
}
