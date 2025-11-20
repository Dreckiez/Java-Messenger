package services;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class AvatarService {

    private static final String DEFAULT_AVATAR_PATH = "/assets/default_person.png";

    public static Image loadAvatar(String avatarUrl) {
        try {
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                // Load from URL
                return new ImageIcon(URI.create(avatarUrl).toURL()).getImage();
            }
        } catch (IOException ignored) {
        }

        // Fallback → load default avatar from resources
        return new ImageIcon(AvatarService.class.getResource(DEFAULT_AVATAR_PATH)).getImage();
    }
}
