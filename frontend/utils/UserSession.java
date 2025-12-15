package utils;

import java.util.ArrayList;
import java.util.List;

import models.User;
import services.UserListener;

public class UserSession {
    private static User currentUser;

    private static final List<UserListener> listeners = new ArrayList<>();

    // Phương thức nội bộ để chạy vòng lặp thông báo
    private static void notifyListeners() {
        for (UserListener listener : listeners) {
            listener.onUserUpdated(currentUser);
        }
    }

    // 🔥 [MỚI] Hàm public để các màn hình khác (như ProfileAvatar) gọi thủ công
    public static void fireUserUpdated() {
        notifyListeners();
    }

    public static void addListener(UserListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void setUser(User user) {
        currentUser = user;
        notifyListeners(); // Tự động thông báo khi set user mới (Login)
    }

    public static User getUser() {
        return currentUser;
    }

    public static void setUserInfo(int id, String username, String avatar, String role, String address, String gender,
            String birthday, String email, String firstName, String lastName) {
        if (currentUser != null) {
            currentUser.setUserInfo(id, username, avatar, role, address, gender, birthday, email, firstName, lastName);
            notifyListeners(); // Tự động thông báo khi update info qua hàm này
        }
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void clearSession() {
        currentUser = null;
        notifyListeners();
    }
}