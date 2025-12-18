package services;

import javax.swing.SwingUtilities;

import components.CenterPanel;
import components.ChatList;
import components.ChatPanel;
import components.NavPanel;
import models.MessageWsResponse;
import models.RealTimeAction;

public class MessageSocketListener {
    private static CenterPanel centerPanel;
    private static ChatList chatList;

    public static void init(CenterPanel cp, NavPanel navPanel) {
        centerPanel = cp;
        chatList = navPanel.getChatList();
    }

    public static void onMessage(MessageWsResponse message) {
        SwingUtilities.invokeLater(() -> handle(message));
    }

    private static void handle(MessageWsResponse message) {
        if (centerPanel == null || chatList == null)
            return;

        String action = message.getRealTimeAction();

        if (RealTimeAction.SEND.name().equals(action)) {
            System.out.println("DEBUG: New message from " + message.getName() + ": " + message.getContent());

            long activeChatId = centerPanel.getCurrentChatId();
            // Assuming private messages for now (check types if needed)
            if (activeChatId == message.getPrivateConversationId()) {
                centerPanel.getChatPanel().addSocketMessage(message);
            }

            // B. Update Sidebar List (Preview & Reorder)
            // Format time simply for preview
            String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            chatList.updateConversationOnMessage(
                    message.getPrivateConversationId(),
                    message.getContent(),
                    time);

        } else if (RealTimeAction.DELETE.name().equals(action)) {
            System.out.println("DEBUG: Message deleted: " + message.getPrivateConversationMessageId());

            // TODO: Remove the message bubble from the UI
            // if (currentChatPanel != null) {
            // currentChatPanel.removeMessageById(message.getPrivateConversationMessageId());
            // }
        }
    }
}
