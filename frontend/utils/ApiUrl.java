package utils;

public class ApiUrl {
    public static final String BASE = "http://localhost:8080/api/";
    public static final String LOGIN = BASE + "chat/auth/signin";
    public static final String REGISTER = BASE + "chat/auth/signup";
    public static final String MYPROFILE = BASE + "chat/user/profile";
    public static final String SEARCH = BASE + "chat/user/search";
    public static final String FRIENDREQUESTLIST = BASE + "chat/user/friend/list-request-received";
    public static final String FRIENDLIST = BASE + "chat/user/friend/list";
    public static final String REMOVE_FRIEND = BASE + "chat/user/friend/remove";
    public static final String DECIDE_FRIEND_REQUEST = BASE + "chat/user/friend/update-status";
    public static final String FORGOT_PASSWORD = BASE + "chat/auth/reset-password";
    public static final String ADMIN_GET_USERS = BASE + "chat/admin/get-user";
    public static final String BLOCK_USER = BASE + "chat/user/block/request";
    public static final String FRIEND_REQUEST = BASE + "chat/user/friend/request";
    public static final String UPDATE_FRIEND_REQUEST = BASE + "chat/user/friend/update-status";
    public static final String BLOCK_LIST = BASE + "chat/user/block/list";
    public static final String REMOVE_BLOCK = BASE + "chat/user/block/remove-block";
    public static final String REPORT_USER = BASE + "chat/user/report";
    public static final String PRIVATE_CONVERSATION = BASE + "chat/user/private-conversations";
    public static final String GROUP_CONVERSATION = BASE + "chat/user/group-conversations";
}
