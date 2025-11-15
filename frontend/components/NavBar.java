package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.events.MouseEvent;

import screens.HomeScreen;
import utils.UserSession;

public class NavBar extends JPanel {
    public NavBar(HomeScreen home, CenterPanel center, NavPanel navPanel) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(70, 0));
        setBackground(new Color(245, 245, 245));

        // Top: Avatar button
        // JLabel avatar = new JLabel(new ImageIcon(UserSession.getUser().getAvatar()));
        // JButton avatarBtn = new JButton(avatar);
        // avatarBtn.setFocusable(false);
        // avatarBtn.setBackground(new Color(240, 240, 240));

        // avatarBtn.addActionListener(e -> {
        // center.showSettings();
        // home.toggleInfoPanel(false);
        // });

        // avatar.addMouseListener(new MouseAdapter() {
        // public void mouseClicked(MouseEvent e) {
        // center.showSettings();
        // home.toggleInfoPanel(false);
        // }
        // });

        // Create the circular avatar image
        ImageIcon avatarIcon = new ImageIcon(UserSession.getUser().getAvatar());

        // Create button with the avatar
        JButton avatarBtn = new JButton(avatarIcon);
        avatarBtn.setFocusable(true); // Allow keyboard navigation
        avatarBtn.setBorderPainted(false);
        avatarBtn.setContentAreaFilled(false);
        avatarBtn.setFocusPainted(false); // Or keep true for accessibility
        avatarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Shows it's clickable

        // Optional: Add hover effect
        avatarBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                avatarBtn.setBorderPainted(true);
                // Or change opacity, add highlight, etc.
            }

            public void mouseExited(MouseEvent e) {
                avatarBtn.setBorderPainted(false);
            }
        });

        avatarBtn.addActionListener(e -> {
            center.showSettings();
            home.toggleInfoPanel(false);
        });

        // Center: Chat & Search buttons
        JPanel centerButtons = new JPanel(new GridLayout(2, 1, 0, 10));
        centerButtons.setOpaque(false);

        JButton chatBtn = new JButton("💬");
        JButton searchBtn = new JButton("🔍");

        chatBtn.addActionListener(e -> {
            navPanel.showChat();
            center.showChat();
            home.toggleInfoPanel(true);
        });

        searchBtn.addActionListener(e -> {
            navPanel.showPanel("searchfriend");
            home.toggleInfoPanel(false);
        });

        centerButtons.add(chatBtn);
        centerButtons.add(searchBtn);

        add(avatarBtn, BorderLayout.NORTH);
        // add(avatar, BorderLayout.NORTH);
        add(centerButtons, BorderLayout.CENTER);
    }
}
