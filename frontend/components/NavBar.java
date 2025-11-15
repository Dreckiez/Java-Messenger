package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import screens.HomeScreen;
import utils.StyleButton;
import utils.UserSession;

public class NavBar extends JPanel {
    public NavBar(HomeScreen home, CenterPanel center, NavPanel navPanel) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(70, 0));
        setBackground(new Color(245, 245, 245));

        // Create button with the avatar
        JButton avatarBtn = new JButton(new ImageIcon(UserSession.getUser().getAvatar()));
        avatarBtn.setFocusable(true); // Allow keyboard navigation
        avatarBtn.setBorderPainted(false);
        avatarBtn.setContentAreaFilled(false);
        avatarBtn.setFocusPainted(false); // Or keep true for accessibility
        avatarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Shows it's clickable

        avatarBtn.addActionListener(e -> {
            center.showSettings();
            home.toggleInfoPanel(false);
        });

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        JPanel centerButtons = new JPanel(new GridLayout(2, 1, 0, 0));
        centerButtons.setOpaque(false);
        centerButtons.setMaximumSize(new Dimension(70, 120)); // 50+50+10 for gap
        centerButtons.setPreferredSize(new Dimension(70, 120)); // 50+50+10 for gap

        JButton chatBtn = new JButton("💬");
        JButton searchBtn = new JButton("🔍");

        StyleButton st = new StyleButton();
        st.styleButton(chatBtn);
        st.styleButton(searchBtn);

        chatBtn.addActionListener(e -> {
            navPanel.showChat();
            center.showChat();
            home.toggleInfoPanel(true);
        });

        searchBtn.addActionListener(e -> {
            navPanel.showPanel("searchfriend");
        });

        centerButtons.add(chatBtn);
        centerButtons.add(searchBtn);

        // Add glue to push buttons to center
        centerWrapper.add(centerButtons);
        centerWrapper.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/logout.png")));
        st.styleButton(logoutBtn);

        logoutBtn.addActionListener(e -> {
            home.logout(); // Call logout on HomeScreen
        });

        add(avatarBtn, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(logoutBtn, BorderLayout.SOUTH);
    }
}
