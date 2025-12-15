package models;

import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserRecordOnlineModel {
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private boolean isOnline;
    private String joinedAt;
    private int activityCount;
    private String lastOnlineAt;
    private Long totalOnlineSeconds;

    // --- Constructor ---
    public UserRecordOnlineModel() {}

    // --- Static Factory Method (Thêm mới để parse JSON) ---
    public static UserRecordOnlineModel fromJson(JSONObject json) {
        UserRecordOnlineModel u = new UserRecordOnlineModel();
        u.userId = json.optInt("userId");
        u.username = json.optString("username");
        u.firstName = json.optString("firstName");
        u.lastName = json.optString("lastName");
        u.avatarUrl = json.optString("avatarUrl");
        u.isOnline = json.optBoolean("isOnline");
        u.joinedAt = json.optString("joinedAt");
        u.activityCount = json.optInt("activityCount");
        u.lastOnlineAt = json.optString("lastOnlineAt");
        // Xử lý số liệu Long an toàn
        if (json.has("totalOnlineSeconds") && !json.isNull("totalOnlineSeconds")) {
            u.totalOnlineSeconds = json.getLong("totalOnlineSeconds");
        }
        return u;
    }

    // --- Getters & Formatters (Giữ nguyên như cũ) ---
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public boolean isOnline() { return isOnline; }
    public int getActivityCount() { return activityCount; }
    
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    public String getFormattedJoinedAt() {
        if (joinedAt == null || joinedAt.isEmpty()) return "-";
        try {
            // Cắt chuỗi nếu có milli/micro seconds quá dài để tránh lỗi parse đơn giản
            String cleanDate = joinedAt;
            if(cleanDate.length() > 19) cleanDate = cleanDate.substring(0, 19); 
            LocalDateTime date = LocalDateTime.parse(cleanDate);
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) { return joinedAt; }
    }

    public String getFormattedTotalTime() {
        if (totalOnlineSeconds == null) return "-";
        long mins = totalOnlineSeconds / 60;
        long hours = mins / 60;
        if (hours > 0) return hours + "h " + (mins % 60) + "m";
        return mins + "m";
    }

    public String getFormattedLastOnline() {
        if (lastOnlineAt == null || lastOnlineAt.isEmpty()) return "N/A";
        try {
            // Cắt chuỗi nếu quá dài (ví dụ có nanoseconds) để tránh lỗi parse
            String cleanDate = lastOnlineAt;
            if (cleanDate.length() > 19) cleanDate = cleanDate.substring(0, 19);
            
            LocalDateTime date = LocalDateTime.parse(cleanDate);
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (Exception e) {
            return lastOnlineAt; // Trả về chuỗi gốc nếu lỗi
        }
    }
}