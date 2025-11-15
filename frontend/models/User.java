package models;

import java.awt.Image;

import javax.swing.ImageIcon;

import utils.ImageEditor;

public class User {
    private Image avatar;
    private String username;

    public User() {
        avatar = new ImageIcon(getClass().getClassLoader().getResource("assets/wolf-howling.jpg")).getImage();
        ImageEditor imgEdt = new ImageEditor();
        avatar = imgEdt.makeCircularImage(avatar, 58).getImage();
        username = "Dreckiez";
    }

    public String getUsername() {
        return username;
    }

    public Image getAvatar() {
        return avatar;
    }
}
