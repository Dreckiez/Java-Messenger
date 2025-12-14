package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame; // Import thêm
import javax.swing.JPanel;
import javax.swing.SwingUtilities; // Import thêm

import models.User;
import screens.HomeScreen;
import services.UserListener;
import utils.ImageEditor;
import utils.ImageLoader;
import utils.StyleButton;
import utils.UserSession;

public class NavBar extends JPanel implements UserListener {

    private JButton avatarBtn;
    
    // Các nút điều hướng
    private JButton chatBtn;
    private JButton searchBtn;       // Tìm bạn bè
    private JButton globalMsgBtn;    // 🔥 NEW: Tìm tin nhắn toàn cục
    private JButton friendBtn;
    private JButton requestBtn;
    private JButton blockedBtn;
    
    private ImageEditor editor;
    
    // Màu sắc
    private final Color ACTIVE_BG = new Color(226, 232, 240); 
    private final Color DEFAULT_BG = new Color(245, 245, 245); 

    public NavBar(HomeScreen home, CenterPanel center, NavPanel navPanel) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(60, 0)); 
        setBackground(DEFAULT_BG);

        UserSession.addListener(this);

        editor = new ImageEditor();
        avatarBtn = new JButton();
        refreshAvatar(UserSession.getUser());

        // Setup Avatar Button
        avatarBtn.setFocusable(true); 
        avatarBtn.setBorderPainted(false);
        avatarBtn.setContentAreaFilled(false);
        avatarBtn.setFocusPainted(false);
        avatarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
        avatarBtn.addActionListener(e -> {
            center.showSettings();
            home.toggleInfoPanel(false);
            resetAllButtons(); 
        });

        // Center Wrapper
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        // 🔥 UPDATE LAYOUT: Tăng từ 5 lên 6 dòng để chứa nút mới
        JPanel centerButtons = new JPanel(new GridLayout(6, 1, 0, 10)); 
        centerButtons.setOpaque(false);
        centerButtons.setMaximumSize(new Dimension(50, 360)); // Tăng chiều cao max lên xíu
        centerButtons.setPreferredSize(new Dimension(50, 360)); 

        // Init Buttons
        chatBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/chat.png")));
        searchBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/search.png")));
        friendBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/friend.png")));
        requestBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/request.png")));
        
        // 🔥 NEW BUTTON: Global Message Search
        globalMsgBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/searchmsgglobal.png")));

        // Block Button
        ImageIcon blockedIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/block.png"));
        if (blockedIcon.getImage() == null) {
            BufferedImage initialAvatar = editor.createInitialAvatar("!", Color.RED, 40);
            blockedIcon = new ImageIcon(editor.scaleImage(initialAvatar, 24));
        }
        blockedBtn = new JButton(blockedIcon);

        // Style Buttons
        StyleButton st = new StyleButton();
        st.styleButton(chatBtn);
        st.styleButton(searchBtn);
        st.styleButton(requestBtn);
        st.styleButton(friendBtn);
        st.styleButton(blockedBtn);
        st.styleButton(globalMsgBtn); // 🔥 Style nút mới
        // Action Listeners
        chatBtn.addActionListener(e -> {
            navPanel.showPanel("chatlist");
            center.showChat();
            home.toggleInfoPanel(true);
        });
        searchBtn.addActionListener(e -> navPanel.showPanel("searchfriend"));
        friendBtn.addActionListener(e -> navPanel.showPanel("onlinefriend"));
        requestBtn.addActionListener(e -> navPanel.showPanel("request"));
        blockedBtn.addActionListener(e -> navPanel.showPanel("blockedusers"));

        // 🔥 ACTION LISTENER CHO NÚT SEARCH TIN NHẮN TOÀN CỤC
        globalMsgBtn.addActionListener(e -> {
            // Lấy Frame cha để làm owner cho Dialog
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            
            // Khởi tạo Dialog Search với chế độ Global (chatId = -1)
            MessageSearchDialog searchDialog = new MessageSearchDialog(
                owner, 
                -1,     // -1: Global Search
                false,  // isGroup: false (không quan trọng khi global)
                selectedMsg -> {
                    // Callback khi người dùng click vào một tin nhắn kết quả
                    System.out.println("Global Search Clicked: " + selectedMsg.toString());
                }
            );
            searchDialog.setVisible(true);
            
            // (Tuỳ chọn) Highlight nút này hoặc Reset các nút khác
            // resetAllButtons();
            // highlightButton(globalMsgBtn);
        });

        // Add Buttons to Panel
        centerButtons.add(chatBtn);
        centerButtons.add(searchBtn);
        centerButtons.add(requestBtn);
        centerButtons.add(friendBtn);
        centerButtons.add(blockedBtn);
        centerButtons.add(globalMsgBtn); // 🔥 Thêm vào layout (Vị trí thứ 3)
        centerWrapper.add(Box.createVerticalStrut(20));
        centerWrapper.add(centerButtons);
        centerWrapper.add(Box.createVerticalGlue());

        // Logout Button
        ImageIcon logoutIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/logout.png"));
        JButton logoutBtn = new JButton(new ImageIcon(editor.scaleImage(logoutIcon.getImage(), 24)));
        st.styleButton(logoutBtn);
        logoutBtn.addActionListener(e -> home.logout());

        add(avatarBtn, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(logoutBtn, BorderLayout.SOUTH);
        
        // Mặc định highlight Chat
        highlightButton(chatBtn);
    }
    
    public void setActiveButton(String panelName) {
        resetAllButtons();
        switch (panelName) {
            case "chatlist": highlightButton(chatBtn); break;
            case "searchfriend": highlightButton(searchBtn); break;
            // case "globalmsg": highlightButton(globalMsgBtn); break; // Nếu bạn muốn nút này sáng lên
            case "onlinefriend": highlightButton(friendBtn); break;
            case "request": highlightButton(requestBtn); break;
            case "blockedusers": highlightButton(blockedBtn); break;
        }
    }

    private void highlightButton(JButton btn) {
        if (btn != null) {
            btn.setContentAreaFilled(true); 
            btn.setBackground(ACTIVE_BG);   
            btn.repaint();
        }
    }

    private void resetAllButtons() {
        resetButton(chatBtn);
        resetButton(searchBtn);
        resetButton(globalMsgBtn); // 🔥 Reset cả nút này
        resetButton(friendBtn);
        resetButton(requestBtn);
        resetButton(blockedBtn);
    }

    private void resetButton(JButton btn) {
        if (btn != null) {
            btn.setContentAreaFilled(false); // Trong suốt
            btn.setBackground(DEFAULT_BG);
            btn.repaint();
        }
    }

    @Override
    public void onUserUpdated(User user) {
        refreshAvatar(user);
    }

    public void refreshAvatar(User user) {
        String avatarUrl = (user != null) ? user.getAvatar() : null;
        ImageLoader.loadImageAsync(avatarUrl, new ImageLoader.ImageLoadCallback() {
            @Override
            public void onLoaded(Image img) {
                if (avatarBtn != null) {
                    avatarBtn.setIcon(editor.makeCircularImage(img, 36));
                    avatarBtn.repaint();
                    avatarBtn.revalidate();
                }
            }
        });
    }
}