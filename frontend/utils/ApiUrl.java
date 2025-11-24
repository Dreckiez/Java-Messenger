package utils;

public class ApiUrl {
    public static final String BASE = "http://localhost:8080/api/";
    public static final String LOGIN = BASE + "chat/auth/signin";
    public static final String REGISTER = BASE + "chat/auth/signup";
    public static final String MYPROFILE = BASE + "chat/user/profile";
    public static final String SEARCH = BASE + "chat/user/search";
    public static final String FRIENDREQUESTLIST = BASE + "chat/user/friend/list-request-received";
    public static final String FRIENDLIST = BASE + "chat/user/friend/list";
    public static final String DECIDE_FRIEND_REQUEST = BASE + "chat/user/friend/update-status";
    public static final String FORGOT_PASSWORD = BASE + "chat/auth/reset-password";
}
