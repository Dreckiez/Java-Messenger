package services;

import javax.swing.SwingUtilities;

import components.CenterPanel;
import components.ChatList;
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

            boolean isGroup = (message.getGroupConversationId() != null && message.getGroupConversationId() > 0);

            long messageChatId = isGroup ? message.getGroupConversationId() : message.getPrivateConversationId();

            long currentOpenId = centerPanel.getCurrentChatId();
            String currentType = centerPanel.getCurrentChatType(); // "GROUP" or "PRIVATE"
            boolean isOpen = false;

            if (isGroup) {
                if ("GROUP".equalsIgnoreCase(currentType) && currentOpenId == messageChatId) {
                    isOpen = true;
                }
            } else {
                if ("PRIVATE".equalsIgnoreCase(currentType) && currentOpenId == messageChatId) {
                    isOpen = true;
                }
            }

            int myId = utils.UserSession.getUser().getId(); // Assuming this returns int or long
            Long senderId = message.getUserId();

            // Compare primitive long values to avoid Integer vs Long object mismatch
            boolean isMe = (senderId != null && senderId.longValue() == myId);

            // Only add to ChatPanel if it's NOT me (to avoid double bubble)
            if (isOpen && !isMe) {
                centerPanel.getChatPanel().addSocketMessage(message);
            }

            String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

            chatList.updateConversationOnMessage(
                    messageChatId,
                    message.getContent(),
                    time);

        } else if (RealTimeAction.DELETE.name().equals(action)) {
            System.out.println("DEBUG: Message deleted: " + message.getPrivateConversationMessageId());

        }
    }
}
