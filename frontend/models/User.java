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
        avatar = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/96/Gordon_Ramsay_%28cropped%29.jpg/250px-Gordon_Ramsay_%28cropped%29.jpg";
    }

    public void setUserInfo(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }
}
