package models;

public class Contact {
    private int id;
    private String name;
    private String avatar;
    private String isFriend;

    public Contact(int id, String name, String avatarUrl, String isFriend) {
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

    public String isFriend() {
        return isFriend;
    }

    public int getUserId() {
        return id;
    }
}
