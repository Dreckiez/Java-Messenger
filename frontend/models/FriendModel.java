package models;

import org.json.JSONObject;

public class FriendModel {
    private Long userId;
    private String username;
    private String fullName;
    private String madeFriendAt;
    private boolean isOnline;

    public FriendModel() {}

    public static FriendModel fromJson(JSONObject json) {
        FriendModel f = new FriendModel();
        f.userId = json.optLong("userId");
        f.username = json.optString("username");
        f.fullName = json.optString("firstName", "") + " " + json.optString("lastName", "");
        f.madeFriendAt = json.optString("madeFriendAt");
        f.isOnline = json.optBoolean("isOnline");
        return f;
    }

    // Getters
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public boolean isOnline() { return isOnline; }
    
    public String getMadeFriendAt() {
        if (madeFriendAt != null && madeFriendAt.contains("T")) {
            return madeFriendAt.replace("T", " ").substring(0, 16); // Format yyyy-MM-dd HH:mm
        }
        return madeFriendAt;
    }
}