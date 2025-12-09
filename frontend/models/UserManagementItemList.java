package models;

import org.json.JSONObject;

public class UserManagementItemList {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String joinedAt;
    private boolean isActive;
    private int friendCount;
    private String address;
    private String role;
    private String gender;
    // Có thể bỏ bớt các trường không hiện lên bảng như password, address... nếu muốn class nhẹ hơn
    
    public UserManagementItemList() {
    }

    // 👇 Hàm factory quan trọng để convert JSON
    public static UserManagementItemList fromJson(JSONObject json) {
        UserManagementItemList u = new UserManagementItemList();
        if (json == null) return u;

        u.setUserId(json.optLong("userId"));
        u.setUsername(json.optString("username"));
        u.setFirstName(json.optString("firstName"));
        u.setLastName(json.optString("lastName"));
        u.setEmail(json.optString("email"));
        u.setJoinedAt(json.optString("joinedAt"));
        u.setActive(json.optBoolean("isActive"));
        u.setFriendCount(json.optInt("friendCount"));
        u.setAddress(json.optString("address"));
        u.setRole(json.optString("role"));
        u.setGender(json.optString("gender"));
        return u;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getJoinedAt() { return joinedAt; }
    public void setJoinedAt(String joinedAt) { this.joinedAt = joinedAt; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public int getFriendCount() { return friendCount; }
    public void setFriendCount(int friendCount) { this.friendCount = friendCount; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}