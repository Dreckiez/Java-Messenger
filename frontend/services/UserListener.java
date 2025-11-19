package services;

import models.User;

public interface UserListener {
    public void onUserUpdated(User user);
}