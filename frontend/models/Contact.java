package models;

public class Contact {
    private int id;
    private String name;
    private String avatar;
    private boolean isFriend;

    public Contact(int id, String name, String avatarUrl, boolean isFriend) {
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

    public int getUserId() {
        return id;
    }
}
