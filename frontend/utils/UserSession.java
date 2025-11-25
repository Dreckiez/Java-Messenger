package utils;

import java.util.ArrayList;
import java.util.List;

import models.User;
import services.UserListener;

public class UserSession {
    private static User currentUser;

    private static final List<UserListener> listeners = new ArrayList<>();

    private static void notifyListeners() {
        for (UserListener listener : listeners) {
            listener.onUserUpdated(currentUser);
        }
    }

    public static void addListener(UserListener listener) {
        listeners.add(listener);
    }

    public static void setUser(User user) {
        currentUser = user;
        notifyListeners();
    }

    public static User getUser() {
        return currentUser;
    }

    public static void setUserInfo(int id, String username, String avatar, String role, String address, String gender,
            String birthday, String email, String firstName, String lastName) {
        if (currentUser != null) {
            currentUser.setUserInfo(id, username, avatar, role, address, gender, birthday, email, firstName, lastName);
            notifyListeners();
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
