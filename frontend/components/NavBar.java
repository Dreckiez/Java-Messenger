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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

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
    private JButton searchBtn;
    private JButton globalMsgBtn;
    private JButton friendBtn;
    private JButton requestBtn;
    private JButton blockedBtn;

    private ImageEditor editor;

    private NotificationDot requestDot;

    // 🔥 BIẾN LƯU TRẠNG THÁI (Mặc định là chatlist)
    private String currentActivePanel = "chatlist";

    private final Color ACTIVE_BG = new Color(226, 232, 240);
    private final Color DEFAULT_BG = new Color(245, 245, 245);

    public NavBar(HomeScreen home, CenterPanel center, NavPanel navPanel) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(60, 0));
        setBackground(DEFAULT_BG);

        UserSession.addListener(this);

        editor = new ImageEditor();
        avatarBtn = new JButton();
        requestDot = new NotificationDot();
        refreshAvatar(UserSession.getUser());

        avatarBtn.setFocusable(true);
        avatarBtn.setBorderPainted(false);
        avatarBtn.setContentAreaFilled(false);
        avatarBtn.setFocusPainted(false);
        avatarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        avatarBtn.addActionListener(e -> {
            center.showSettings();
            home.toggleInfoPanel(false);
            resetAllButtons();
            // Settings không nằm trong danh sách nút active chính,
            // nhưng ta có thể giữ currentActivePanel là cái cũ để khi quay lại thì đúng.
        });

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        JPanel centerButtons = new JPanel(new GridLayout(6, 1, 0, 10));
        centerButtons.setOpaque(false);
        centerButtons.setMaximumSize(new Dimension(50, 360));
        centerButtons.setPreferredSize(new Dimension(50, 360));

        chatBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/chat.png")));
        searchBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/search.png")));
        friendBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/friend.png")));

        requestBtn = new JButton(new ImageIcon(getClass().getClassLoader().getResource("assets/request.png")));
        requestBtn.setLayout(null);
        // requestBtn.setLayout(new OverlayLayout(requestBtn));
        // requestDot.setAlignmentX(1.0f); // RIGHT
        // requestDot.setAlignmentY(0.0f); // TOP
        requestDot.setBounds(32, 12, 10, 10);
        requestBtn.add(requestDot);
        requestDot.setVisible(false); // hidden by default

        globalMsgBtn = new JButton(
                new ImageIcon(getClass().getClassLoader().getResource("assets/searchmsgglobal.png")));

        ImageIcon blockedIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/block.png"));
        if (blockedIcon.getImage() == null) {
            BufferedImage initialAvatar = editor.createInitialAvatar("!", Color.RED, 40);
            blockedIcon = new ImageIcon(editor.scaleImage(initialAvatar, 24));
        }
        blockedBtn = new JButton(blockedIcon);

        StyleButton st = new StyleButton();
        st.styleButton(chatBtn);
        st.styleButton(searchBtn);
        st.styleButton(requestBtn);
        st.styleButton(friendBtn);
        st.styleButton(blockedBtn);
        st.styleButton(globalMsgBtn);

        // --- ACTION LISTENERS ---

        chatBtn.addActionListener(e -> {
            navPanel.showPanel("chatlist");
            center.showChat();
            home.toggleInfoPanel(true);
            setActiveButton("chatlist");
        });
        searchBtn.addActionListener(e -> {
            navPanel.showPanel("searchfriend");
            setActiveButton("searchfriend");
        });
        friendBtn.addActionListener(e -> {
            navPanel.showPanel("onlinefriend");
            setActiveButton("onlinefriend");
        });
        requestBtn.addActionListener(e -> {
            navPanel.showPanel("request");
            setActiveButton("request");
        });
        blockedBtn.addActionListener(e -> {
            navPanel.showPanel("blockedusers");
            setActiveButton("blockedusers");
        });

        // 🔥 LOGIC AUTO QUAY LẠI NÚT TRƯỚC
        globalMsgBtn.addActionListener(e -> {
            // 1. Highlight nút Global Search tạm thời
            resetAllButtons();
            highlightButton(globalMsgBtn);

            // 2. Mở Dialog (Code sẽ dừng ở dòng setVisible cho đến khi Dialog tắt)
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            MessageSearchDialog searchDialog = new MessageSearchDialog(
                    owner,
                    -1,
                    false,
                    selectedMsg -> {
                        // Nếu chọn tin nhắn -> Center Panel sẽ mở chat
                        // Lúc này logic bên Center có thể gọi setActiveButton("chatlist")
                        // nên ta không cần lo lắng lắm.
                        if (center != null) {
                            // center.openConversationFromSearch(selectedMsg);
                        }
                    });
            searchDialog.setVisible(true);

            // 3. 🔥 Dòng này chạy ngay sau khi Dialog tắt (Close hoặc Cancel)
            // Khôi phục lại trạng thái nút cũ
            setActiveButton(currentActivePanel);
        });

        centerButtons.add(chatBtn);
        centerButtons.add(searchBtn);
        centerButtons.add(requestBtn);
        centerButtons.add(friendBtn);
        centerButtons.add(blockedBtn);
        centerButtons.add(globalMsgBtn);

        centerWrapper.add(Box.createVerticalStrut(20));
        centerWrapper.add(centerButtons);
        centerWrapper.add(Box.createVerticalGlue());

        ImageIcon logoutIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/logout.png"));
        JButton logoutBtn = new JButton(new ImageIcon(editor.scaleImage(logoutIcon.getImage(), 24)));
        st.styleButton(logoutBtn);
        logoutBtn.addActionListener(e -> home.logout());

        add(avatarBtn, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(logoutBtn, BorderLayout.SOUTH);

        // Mặc định ban đầu
        highlightButton(chatBtn);
    }

    public void setActiveButton(String panelName) {
        // 🔥 Cập nhật biến lưu trạng thái
        // Chỉ lưu nếu panelName KHÔNG phải là globalmsg (vì globalmsg chỉ là tạm thời)
        if (!panelName.equals("globalmsg")) {
            this.currentActivePanel = panelName;
        }

        resetAllButtons();
        switch (panelName) {
            case "chatlist":
                highlightButton(chatBtn);
                break;
            case "searchfriend":
                highlightButton(searchBtn);
                break;
            case "onlinefriend":
                highlightButton(friendBtn);
                break;
            case "request":
                highlightButton(requestBtn);
                break;
            case "blockedusers":
                highlightButton(blockedBtn);
                break;
            case "globalmsg":
                highlightButton(globalMsgBtn);
                break;
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
        resetButton(globalMsgBtn);
        resetButton(friendBtn);
        resetButton(requestBtn);
        resetButton(blockedBtn);
    }

    private void resetButton(JButton btn) {
        if (btn != null) {
            btn.setContentAreaFilled(false);
            btn.setBackground(DEFAULT_BG);
            btn.repaint();
        }
    }

    @Override
    public void onUserUpdated(User user) {
        refreshAvatar(user);
    }

    public void showRequestDot() {
        requestDot.setVisible(true);
    }

    public void hideRequestDot() {
        requestDot.setVisible(false);
    }

    public String getCurrentActivePanel() {
        return currentActivePanel;
    }

    public void refreshAvatar(User user) {
        String avatarUrl = (user != null) ? user.getAvatar() : null;
        ImageLoader.loadImageAsync(avatarUrl, new ImageLoader.ImageLoadCallback() {
            @Override
            public void onLoaded(Image img) {
                if (avatarBtn != null) {
                    if (img != null) {
                        avatarBtn.setIcon(editor.makeCircularImage(img, 36));
                    } else {
                        String name = "?";
                        if (user != null) {
                            if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                                name = user.getFirstName();
                            } else if (user.getUsername() != null) {
                                name = user.getUsername();
                            }
                        }
                        BufferedImage placeholder = editor.createInitialAvatar(name, new Color(100, 116, 139), 36);
                        avatarBtn.setIcon(editor.makeCircularImage(placeholder, 36));
                    }
                    avatarBtn.repaint();
                    avatarBtn.revalidate();
                }
            }
        });
    }
}