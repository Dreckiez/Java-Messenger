package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener; // Import ActionListener
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import models.User;
import screens.HomeScreen;
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

        avatarBtn.setFocusable(true); 
        avatarBtn.setBorderPainted(false);
        avatarBtn.setContentAreaFilled(false);
        avatarBtn.setFocusPainted(false);
        avatarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 

        avatarBtn.addActionListener(e -> {
            center.showSettings();
            home.toggleInfoPanel(false);
        });

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        // 🔥 Thay đổi GridLayout từ 4 thành 5 để chứa nút Blocked Users mới
        JPanel centerButtons = new JPanel(new GridLayout(5, 1, 0, 0));
        centerButtons.setOpaque(false);
        centerButtons.setMaximumSize(new Dimension(70, 300)); // 5 * 50 = 250 (Adjusted max size)
        centerButtons.setPreferredSize(new Dimension(70, 300)); 

        JButton chatBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/chat.png")));
        JButton searchBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/search.png")));
        JButton friendBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/friend.png")));
        JButton requestBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/request.png")));
        
        // 🔥 TẠO NÚT MỚI CHO BLOCKED USERS
        // Sử dụng icon khóa/chặn
        ImageIcon blockedIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/block.png")); // Giả định bạn có icon này
        if (blockedIcon.getImage() == null) {
            // 🔥🔥 ĐÃ SỬA: XÓA BỎ .getImage() 🔥🔥
            BufferedImage initialAvatar = editor.createInitialAvatar("B", Color.BLACK, 32);
            blockedIcon = new ImageIcon(editor.scaleImage(initialAvatar, 36));
        }
        JButton blockedBtn = new JButton(blockedIcon);


        StyleButton st = new StyleButton();
        st.styleButton(chatBtn);
        st.styleButton(searchBtn);
        st.styleButton(requestBtn);
        st.styleButton(friendBtn);
        st.styleButton(blockedBtn); // Áp dụng style cho nút mới

        chatBtn.addActionListener(e -> {
            navPanel.showPanel("chatlist");
            center.showChat();
            home.toggleInfoPanel(true);
        });

        searchBtn.addActionListener(e -> {
            navPanel.showPanel("searchfriend");
        });

        friendBtn.addActionListener(e -> {
            navPanel.showPanel("onlinefriend");
        });

        requestBtn.addActionListener(e -> {
            navPanel.showPanel("request");
        });
        
        // 🔥 GÁN ACTION CHO NÚT BLOCKED USERS
        blockedBtn.addActionListener(e -> {
            navPanel.showPanel("blockedusers");
        });


        centerButtons.add(chatBtn);
        centerButtons.add(searchBtn);
        centerButtons.add(requestBtn);
        centerButtons.add(friendBtn);
        centerButtons.add(blockedBtn); // 🔥 THÊM NÚT MỚI VÀO LAYOUT

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
    
    // ... (onUserUpdated và refreshAvatar giữ nguyên) ...
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