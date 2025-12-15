package models;

import org.json.JSONObject;

public class GroupChatModel {
    private Long groupConversationId;
    private String groupName;
    private Long ownerId;
    private String ownerUsername;
    private String createdAt;
    private int memberCount;
    private int adminCount;

    public GroupChatModel() {}

    public static GroupChatModel fromJson(JSONObject json) {
        GroupChatModel g = new GroupChatModel();
        g.groupConversationId = json.optLong("groupConversationId");
        g.groupName = json.optString("groupName");
        g.ownerId = json.optLong("ownerId");
        g.ownerUsername = json.optString("ownerUsername");
        g.createdAt = json.optString("createdAt");
        g.memberCount = json.optInt("memberCount");
        g.adminCount = json.optInt("adminCount");
        return g;
    }

    // Getters
    public Long getId() { return groupConversationId; }
    public String getGroupName() { return groupName; }
    public String getOwnerUsername() { return ownerUsername; }
    public int getMemberCount() { return memberCount; }
    public int getAdminCount() { return adminCount; }
    
    public String getCreatedAt() {
        if (createdAt != null && createdAt.contains("T")) {
            return createdAt.replace("T", " ").substring(0, 16);
        }
        return createdAt;
    }
}