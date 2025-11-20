package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import models.User;
import screens.HomeScreen;
import services.AvatarService;
import services.UserListener;
import utils.ImageEditor;
import utils.ImageLoader;
import utils.StyleButton;
import utils.UserSession;

public class NavBar extends JPanel implements UserListener {

    private JButton avatarBtn;
    private ImageEditor editor;

    public NavBar(HomeScreen home, CenterPanel center, NavPanel navPanel) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(50, 0));
        setBackground(new Color(245, 245, 245));

        UserSession.addListener(this);

        editor = new ImageEditor();
        avatarBtn = new JButton();

        refreshAvatar(UserSession.getUser());

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

        JButton chatBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/chat.png")));
        JButton searchBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/search.png")));

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

        // Scale it down
        ImageIcon logoutIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/logout.png"));

        // Create button with scaled icon
        JButton logoutBtn = new JButton(new ImageIcon(editor.scaleImage(logoutIcon.getImage(), 24)));

        st.styleButton(logoutBtn);

        logoutBtn.addActionListener(e -> {
            home.logout(); // Call logout on HomeScreen
        });

        add(avatarBtn, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(logoutBtn, BorderLayout.SOUTH);
    }

    @Override
    public void onUserUpdated(User user) {
        refreshAvatar(user);
    }

    public void refreshAvatar(User user) {
        String avatarUrl = (user != null) ? user.getAvatar() : null;

        // Use your ImageLoader to fetch in background
        ImageLoader.loadImageAsync(avatarUrl, new ImageLoader.ImageLoadCallback() {
            @Override
            public void onLoaded(Image img) {
                // This runs on the UI thread (EDT) because ImageLoader calls callback in done()
                if (avatarBtn != null) {
                    avatarBtn.setIcon(editor.makeCircularImage(img, 36));
                    avatarBtn.repaint();
                    avatarBtn.revalidate();
                }
            }
        });
    }
}
