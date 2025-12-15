package models;

import org.json.JSONObject;

public class LoginRecord {
    private Long id;
    private Long userId;
    private String username;
    private String fullName;
    private String signedInAt;
    private boolean isSuccessful;

    public static LoginRecord fromJson(JSONObject json) {
        LoginRecord r = new LoginRecord();
        r.id = json.optLong("recordSignInId");
        r.userId = json.has("userId") && !json.isNull("userId") ? json.getLong("userId") : null;
        r.username = json.optString("username", "Unknown");
        r.fullName = json.optString("fullName", "");
        r.signedInAt = json.optString("signedInAt");
        r.isSuccessful = json.optBoolean("isSuccessful");
        return r;
    }

    public String getFormattedTime() {
        if (signedInAt != null && signedInAt.contains("T")) {
            // Cắt bỏ phần mili giây thừa: 2025-12-10T07:54:03.451174 -> 2025-12-10 07:54:03
            return signedInAt.replace("T", " ").split("\\.")[0];
        }
        return signedInAt;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public boolean isSuccessful() { return isSuccessful; }
}