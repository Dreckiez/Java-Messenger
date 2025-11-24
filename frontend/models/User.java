package models;

public class User {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatar;
    private String address;
    private String gender;
    private String bDay;
    private String role;
    private String token;
    private String refreshToken;

    public User(String t, String rt) {
        token = t;
        refreshToken = rt;
    }

    public void setUserInfo(int id, String username, String avatar, String role, String address, String gender,
            String birthDay) {
        this.id = id;
        this.username = username;
        // this.firstName = firstName;
        // this.lastName = lastName;
        this.address = address;
        this.gender = gender;
        this.bDay = birthDay;
        this.avatar = avatar;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFName() {
        return firstName;
    }

    public String getLName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getBDAY() {
        return bDay;
    }

    public String getGender() {
        return gender;
    }

    public int getUserId() {
        return id;
    }
}
