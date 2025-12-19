package models;

public class Friend {
    private Long userId;
    private String name;
    private String avatarUrl;
    private Boolean isOnline;

    public Friend(String name, String avatarUrl, Long userId, Boolean online) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.userId = userId;
        isOnline = online;
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

    public Boolean getOnline() {
        return isOnline;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
