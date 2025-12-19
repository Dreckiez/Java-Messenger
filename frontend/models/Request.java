package models;

public class Request {
    private Long userId;
    private String name;
    private String avatarUrl;
    private String sentAt;

    public Request(String name, String avatarUrl, Long userId, String sendTime) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.userId = userId;
        sentAt = sendTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSendTime() {
        return sentAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
