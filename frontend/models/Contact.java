package models;

public class Contact {
    private String name;
    private String avatar;
    private boolean isFriend;

    public Contact(String name, String avatarUrl, boolean isFriend) {
        this.name = name;
        this.avatar = avatarUrl;
        this.isFriend = isFriend;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatar;
    }

    public Boolean isFriend() {
        return isFriend;
    }
}
