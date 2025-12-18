package models;

import org.json.JSONObject;

public class MessageWsResponse {
    private String realTimeAction; // "SEND" or "DELETE"
    private Long privateConversationMessageId;

    // SEND fields
    private Long userId; // The sender ID
    private String name;
    private String avatarUrl;
    private String content;
    private String sentAt;
    private String type; // "TEXT", "IMAGE", etc.

    // DELETE fields
    private Long privateConversationId;

    // Empty constructor
    public MessageWsResponse() {
    }

    // Constructor to parse from JSONObject (Safe way)
    public static MessageWsResponse fromJson(JSONObject json) {
        MessageWsResponse msg = new MessageWsResponse();
        msg.realTimeAction = json.optString("realTimeAction", "SEND");
        msg.privateConversationMessageId = json.optLong("privateConversationMessageId");

        // Sender Info
        msg.userId = json.optLong("userId");
        String fname = json.optString("firstName");
        String lname = json.optString("lastName");
        msg.name = fname + ' ' + lname;
        msg.avatarUrl = json.optString("avatarUrl");

        // Message Content
        msg.content = json.optString("content");
        msg.sentAt = json.optString("sentAt");
        msg.type = json.optString("type");

        // Delete info
        msg.privateConversationId = json.optLong("privateConversationId");

        return msg;
    }

    // Getters
    public String getRealTimeAction() {
        return realTimeAction;
    }

    public Long getPrivateConversationMessageId() {
        return privateConversationMessageId;
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
}
