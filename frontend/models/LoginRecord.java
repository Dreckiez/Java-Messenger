package models;

import org.json.JSONObject;

public class LoginRecord {
    private String signedInAt;
    private boolean isSuccessful;

    public LoginRecord(String signedInAt, boolean isSuccessful) {
        this.signedInAt = signedInAt;
        this.isSuccessful = isSuccessful;
    }

    public static LoginRecord fromJson(JSONObject json) {
        return new LoginRecord(
            json.optString("signedInAt", ""),
            json.optBoolean("isSuccessful", false)
        );
    }

    public String getSignedInAt() {
        // Format lại ngày cho đẹp: 2025-12-08T14:32:58... -> 2025-12-08 14:32
        if (signedInAt != null && signedInAt.contains("T")) {
            return signedInAt.replace("T", " ").substring(0, 19);
        }
        return signedInAt;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}