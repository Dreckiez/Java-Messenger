package models;

import org.json.JSONObject;

public class MessageWsResponse {
    private String realTimeAction; // "SEND" or "DELETE"

    private Long privateConversationMessageId;
    private Long groupConversationMessageId;

    // SEND fields
    private Long userId; // The sender ID
    private String name;
    private String avatarUrl;
    private String content;
    private String sentAt;
    private String type; // "TEXT", "IMAGE", etc.

    // DELETE fields
    private Long privateConversationId;
    private Long groupConversationId;
    private boolean isAll;

    // Empty constructor
    public MessageWsResponse() {
    }

    // Constructor to parse from JSONObject (Safe way)
    public static MessageWsResponse fromJson(JSONObject json) {
        MessageWsResponse msg = new MessageWsResponse();
        msg.realTimeAction = json.optString("realTimeAction", "SEND");
        msg.privateConversationMessageId = json.optLong("privateConversationMessageId");
        msg.groupConversationMessageId = json.optLong("groupConversationMessageId", -1);

        // Sender Info

        msg.avatarUrl = json.optString("avatarUrl");

        // Message Content
        msg.content = json.optString("content");
        msg.sentAt = json.optString("sentAt");
        msg.type = json.optString("type");
        msg.isAll = json.optBoolean("isAll", false);

        if (json.has("senderName")) {
            msg.name = json.optString("senderName");
        } else if (json.has("firstName")) {
            String fname = json.optString("firstName");
            String lname = json.optString("lastName");
            msg.name = (fname + " " + lname).trim();
        } else {
            msg.name = "System";
        }

        if (json.has("senderId")) {
            msg.userId = json.optLong("senderId");
        } else {
            msg.userId = json.optLong("userId");
        }

        // Delete info
        msg.privateConversationId = json.optLong("privateConversationId", -1L);
        msg.groupConversationId = json.optLong("groupConversationId", -1L);

        return msg;
    }

    public boolean isAll() {
        return isAll;
    }

    // Getters
    public String getRealTimeAction() {
        return realTimeAction;
    }

    public Long getPrivateConversationMessageId() {
        return privateConversationMessageId;
    }

    public Long getGroupConversationMessageId() {
        return groupConversationMessageId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getContent() {
        return content;
    }

    public String getSentAt() {
        return sentAt;
    }

    public String getType() {
        return type;
    }

    public Long getPrivateConversationId() {
        return privateConversationId;
    }

    public Long getGroupConversationId() {
        return groupConversationId;
    }
}
