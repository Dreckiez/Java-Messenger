package models;

public class NotificationWsResponse {
    private String type;
    private Integer count;
    private Long fromUserId;
    private String fromUsername;

    public String getType() {
        return type;
    };

    public Long getFromUserId() {
        return fromUserId;
    };

    public String getFromUsername() {
        return fromUsername;
    }
}
