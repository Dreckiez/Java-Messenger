package utils;

import java.util.ArrayList;
import java.util.List;

import services.ChatListener;

public class ChatSessionManager {
    private static final List<ChatListener> listeners = new ArrayList<>();

    public static void addListener(ChatListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void fireGroupNameChanged(long conversationId, String newName) {
        for (ChatListener listener : listeners) {
            listener.onGroupNameChanged(conversationId, newName);
        }
    }
    public static void fireConversationDeleted(long conversationId) {
        for (ChatListener listener : listeners) {
            listener.onConversationDeleted(conversationId);
        }
    }
    public static void fireGroupAvatarChanged(long conversationId, String newAvatarUrl) {
        for (ChatListener listener : listeners) {
            listener.onGroupAvatarChanged(conversationId, newAvatarUrl);
        }
    }
}