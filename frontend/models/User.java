package models;

public class User {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatar;
    private String email;
    private String address;
    private String gender;
    private String bDay; // Ngày sinh (Format: yyyy-MM-dd)
    private String role;
    private String token;
    private String refreshToken;

    // --- Constructors ---
    public User() {
    }

    public User(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    // --- Helper Method ---
    public void setUserInfo(int id, String username, String avatar, String role, String address, String gender,
                            String birthDay, String email, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.bDay = birthDay;
        this.avatar = avatar;
        this.role = role;
    }

    // ================= GETTERS & SETTERS =================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter chuẩn cho field bDay
    public String getBDay() {
        return bDay;
    }

    public void setBDay(String bDay) {
        this.bDay = bDay;
    }
    
    // Alias: Phương thức phụ để tương thích nếu code cũ gọi getBirthDay()
    public String getBirthDay() {
        return bDay;
    }
    
    // Alias: Phương thức phụ để set
    public void setBirthDay(String birthDay) {
        this.bDay = birthDay;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}