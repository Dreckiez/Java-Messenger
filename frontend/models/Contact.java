package models;

public class Contact {
    private int id;
    private String name;
    private String avatar;
    private String isFriend;
    private String sentAt;

    public Contact(int id, String name, String avatarUrl, String isFriend, String sentAt) {
        this.id = id;
        this.name = name;
        this.avatar = avatarUrl;
        this.isFriend = isFriend;
        this.sentAt = sentAt;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatar;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public int getUserId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getSentAt() {
        return this.sentAt; // Trả về chuỗi timestamp ISO8601 từ backend
    }
}
