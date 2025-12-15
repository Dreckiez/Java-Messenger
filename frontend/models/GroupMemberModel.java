package models;

import org.json.JSONObject;

public class GroupMemberModel {
    private Long userId;
    private String username;
    private String fullName;
    private String avatarUrl;
    private boolean isOnline;
    private String groupRole; // MEMBER, ADMIN...
    private String joinedAt;

    public GroupMemberModel() {}

    public static GroupMemberModel fromJson(JSONObject json) {
        GroupMemberModel m = new GroupMemberModel();
        m.userId = json.optLong("userId");
        m.username = json.optString("username");
        m.fullName = json.optString("firstName", "") + " " + json.optString("lastName", "");
        m.avatarUrl = json.optString("avatarUrl");
        m.isOnline = json.optBoolean("isOnline");
        m.groupRole = json.optString("groupRole", "MEMBER");
        m.joinedAt = json.optString("joinedAt");
        return m;
    }

    // Getters
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public boolean isOnline() { return isOnline; }
    public String getGroupRole() { return groupRole; }
    
    public String getJoinedAt() {
        if (joinedAt != null && joinedAt.contains("T")) {
            return joinedAt.replace("T", " ").substring(0, 16);
        }
        return joinedAt;
    }
}