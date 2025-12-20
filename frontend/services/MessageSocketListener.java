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

            String conversationType = isGroup ? "GROUP" : "PRIVATE";

            chatList.updateConversationOnMessage(
                    messageChatId,
                    conversationType,
                    message.getContent(),
                    time);

        } else if (RealTimeAction.DELETE.name().equals(action)) {
            System.out.println("DEBUG: Message deleted from socket");

            boolean isGroup = (message.getGroupConversationId() != null && message.getGroupConversationId() > 0);
            long messageChatId = isGroup ? message.getGroupConversationId() : message.getPrivateConversationId();

            long currentOpenId = centerPanel.getCurrentChatId();
            String currentType = centerPanel.getCurrentChatType();
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

            if (isOpen) {
                // This calls the method we updated earlier which checks for "DELETE" action
                centerPanel.getChatPanel().addSocketMessage(message);
            }

            // Optional: You might want to update the sidebar preview text here if the last
            // message was deleted
            chatList.updateConversationOnMessage(
                    messageChatId,
                    isGroup ? "GROUP" : "PRIVATE",
                    "Message removed", // Or just leave it, updating is tricky without fetching new last msg
                    java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));

        } else if ("UPDATE".equals(action)) {
            String newData = message.getContent();
            if ("New members added.".equals(newData) || "A member has left the group.".equals(newData)) {
                // Just refresh the InfoPanel if open
                refreshInfoPanelIfOpen(message.getGroupConversationId());
                return;
            }
            System.out.println("DEBUG: Group Update Received for ID: " + message.getGroupConversationId());

            long groupId = message.getGroupConversationId();

            // Heuristic: If it starts with "http" or contains "/", it's likely an Avatar
            // URL.
            // Otherwise, it's a Name.
            boolean isAvatar = newData != null && (newData.startsWith("http") || newData.contains("/"));

            SwingUtilities.invokeLater(() -> {
                if (isAvatar) {
                    // 1. Update Sidebar Avatar
                    chatList.onGroupAvatarChanged(groupId, newData);

                    // 2. Update Header Avatar (only if we are currently looking at this group)
                    if (centerPanel.getCurrentChatId() == groupId
                            && "GROUP".equalsIgnoreCase(centerPanel.getCurrentChatType())) {
                        // Update header but keep current name
                        String currentName = centerPanel.getChatPanel().getNameLabel().getText();
                        centerPanel.getChatPanel().updateChatHeader(groupId, currentName, newData, false);
                    }
                } else {
                    // 1. Update Sidebar Name
                    chatList.onGroupNameChanged(groupId, newData);

                    // 2. Update Header Name (only if we are currently looking at this group)
                    if (centerPanel.getCurrentChatId() == groupId
                            && "GROUP".equalsIgnoreCase(centerPanel.getCurrentChatType())) {
                        // Update text directly for instant feel
                        centerPanel.getChatPanel().getNameLabel().setText(newData);

                        // Also refresh the icon placeholder (if no avatar exists)
                        String currentAvatar = centerPanel.getChatPanel().getCurrentPartnerAvatarUrl();
                        centerPanel.getChatPanel().updateChatHeader(groupId, newData, currentAvatar, true);
                    }
                }
            });
        } else if ("ADD_MEMBER".equals(action)) {
            System.out.println("DEBUG: I was added to Group " + message.getGroupConversationId());

            // Use your existing method. If the chat ID isn't in the list, it triggers
            // 'loadConversations()' automatically.
            String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            chatList.updateConversationOnMessage(
                    message.getGroupConversationId(),
                    "GROUP",
                    message.getContent(),
                    time);
            refreshInfoPanelIfOpen(message.getGroupConversationId());
        } else if ("KICK_MEMBER".equals(action)) {
            long groupId = message.getGroupConversationId();

            try {
                // Backend sends the Kicked User ID in the content field
                int kickedUserId = Integer.parseInt(message.getContent());
                int myId = utils.UserSession.getUser().getId();

                if (kickedUserId == myId) {
                    System.out.println("DEBUG: I was kicked from Group " + groupId);

                    // 1. Remove from Sidebar
                    chatList.onConversationDeleted(groupId);

                    // 2. Unsubscribe from Socket
                    utils.SocketManager.unsubscribeGroup(groupId);

                    // 3. Close Chat Panel if it is currently open
                    if (centerPanel.getCurrentChatId() == groupId
                            && "GROUP".equalsIgnoreCase(centerPanel.getCurrentChatType())) {
                        centerPanel.showWelcome();
                        javax.swing.JOptionPane.showMessageDialog(null, "You have been removed from this group.");
                    }
                } else {
                    // Someone else was kicked -> Update InfoPanel if open
                    if (centerPanel.getCurrentChatId() == groupId) {
                        System.out.println("DEBUG: User " + kickedUserId + " was kicked.");
                        refreshInfoPanelIfOpen(groupId);
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing kicked user ID: " + message.getContent());
            }
        } else if ("ROLE_CHANGE".equals(action)) {
            // Just refresh the list so everyone sees the new badges/buttons
            refreshInfoPanelIfOpen(message.getGroupConversationId());
        }
    }

    private static void refreshInfoPanelIfOpen(long groupId) {
        // Check if the user is currently looking at this group
        if (centerPanel != null &&
                centerPanel.getCurrentChatId() == groupId &&
                "GROUP".equalsIgnoreCase(centerPanel.getCurrentChatType())) {

            SwingUtilities.invokeLater(() -> {
                // This forces CenterPanel to call the API again and update InfoPanel
                centerPanel.reloadCurrentChat();
            });
        }
    }
}
