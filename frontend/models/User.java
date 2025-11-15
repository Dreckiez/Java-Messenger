package models;

import java.awt.Image;

import javax.swing.ImageIcon;

import utils.CircleImage;

public class User {
    private Image avatar;
    private String username;

    public User() {
        avatar = new ImageIcon(getClass().getClassLoader().getResource("assets/wolf-howling.jpg")).getImage();
        CircleImage ci = new CircleImage();
        avatar = ci.makeCircularImage(avatar, 58).getImage();
        username = "Dreckiez";
    }

    public String getUsername() {
        return username;
    }

    public Image getAvatar() {
        return avatar;
    }
}
