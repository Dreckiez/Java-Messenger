package models;

public class User {
    private int id;
    private String username;
    private String avatar;
    private String role;
    private String token;
    private String refreshToken;

    public User(String t, String rt) {
        token = t;
        refreshToken = rt;
    }

    public void setUserInfo(int id, String username, String avatar, String role) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }
}
