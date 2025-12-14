package components;

import javax.swing.*;
import models.Friend;
import utils.ApiClient;
import utils.ApiUrl;
import utils.UserSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class FriendItem extends BaseItem {
    private Friend friend;
    private boolean isOnline;
    
    // Callback xử lý sự kiện bên ngoài
    private ActionListener onRequestHandled; // Báo cho Parent xóa item khỏi UI (Unfriend/Block)
    private Runnable onNavigateToChat; // Chỉ chuyển tab (Create Group)
    private Consumer<ChatTarget> onOpenChat; // Chuyển tab và mở chat (Chat riêng)

    // --- MÀU SẮC ---
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color ONLINE_COLOR = new Color(34, 197, 94);  
    private final Color OFFLINE_COLOR = new Color(148, 163, 184); 
    
    private final Color BTN_BLUE = new Color(59, 130, 246);
    private final Color BTN_RED = new Color(239, 68, 68);
    private final Color BTN_RED_HOVER = new Color(220, 38, 38);
    private final Color BTN_BLUE_HOVER = new Color(37, 99, 235);

    public FriendItem(Friend f) {
        super(f.getName(), f.getAvatarUrl());
        this.friend = f;
        this.isOnline = (f.getOnline() != null) ? f.getOnline() : false;
        
        initUI();
    }

    // --- SETTERS ---
    public void setOnNavigateToChat(Runnable onNavigateToChat) {
        this.onNavigateToChat = onNavigateToChat;
    }
    
    public void setOnOpenChat(Consumer<ChatTarget> onOpenChat) {
        this.onOpenChat = onOpenChat;
    }

    public void setOnRequestHandled(ActionListener callback) {
        this.onRequestHandled = callback;
    }

    public Friend getFriend() { 
        return friend; 
    }

    // --- UI SETUP ---
    private void initUI() {
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        centerWrapper.add(createCenterPanel(), BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        actionPanel = createActionPanel();
        if (actionPanel != null) {
            JPanel actionWrapper = new JPanel(new BorderLayout());
            actionWrapper.setOpaque(false);
            actionWrapper.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0)); 
            JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
            separator.setForeground(new Color(226, 232, 240)); 
            actionWrapper.add(separator, BorderLayout.NORTH);
            actionWrapper.add(actionPanel, BorderLayout.CENTER);
            add(actionWrapper, BorderLayout.SOUTH);
            actionWrapper.setVisible(false);
            this.actionPanel = actionWrapper; 
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { setSelected(true); }
        });
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    }

    @Override
    protected JPanel createCenterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(TEXT_PRIMARY);
        JLabel statusDot = new JLabel("●");
        statusDot.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusDot.setForeground(isOnline ? ONLINE_COLOR : OFFLINE_COLOR);
        statusDot.setToolTipText(isOnline ? "Online" : "Offline");
        panel.add(nameLabel);
        panel.add(statusDot);
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(panel, BorderLayout.CENTER);
        return container;
    }

    @Override
    protected JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

        // 1. CHAT
        JButton chatBtn = createModernButton("Chat", BTN_BLUE, BTN_BLUE_HOVER);
        chatBtn.addActionListener(e -> openPrivateChat());
        // 2. GROUP
        JButton groupBtn = createModernButton("Group", BTN_BLUE, BTN_BLUE_HOVER);
        groupBtn.addActionListener(e -> {
            String groupName = JOptionPane.showInputDialog(this, "Enter group name:", "Create Group", JOptionPane.QUESTION_MESSAGE);
            if (groupName != null && !groupName.trim().isEmpty()) {
                createGroupChat(groupName.trim());
            }
        });

        // 3. UNFRIEND
        JButton unfriendBtn = createModernButton("Unfriend", BTN_RED, BTN_RED_HOVER);
        unfriendBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to remove " + friend.getName() + "?", "Confirm Unfriend", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) unfriendUser();
        });

        // 4. BLOCK
        JButton blockBtn = createModernButton("Block", BTN_RED, BTN_RED_HOVER);
        blockBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Block " + friend.getName() + "?\nThey will be removed from your friends list.", 
                "Confirm Block", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                blockUser(); 
            }
        });

        panel.add(chatBtn);
        panel.add(groupBtn);
        panel.add(unfriendBtn);
        panel.add(blockBtn);

        return panel;
    }

    // ========================================================================
    // 🔥 LOGIC OPEN CHAT (CALL API & NAVIGATE)
    // ========================================================================
    private void openPrivateChat() {
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                String url = ApiUrl.PRIVATE_CONVERSATION + "/" + friend.getUserId(); 
                return ApiClient.getJSON(url, token);
            }
            @Override
            protected void done() {
                try {
                    JSONObject response = get();
                    System.out.println("DEBUG [1] FriendItem API Response: " + response); // <--- IN RA

                    if (response != null && response.has("privateConversationId")) {
                        int chatId = response.getInt("privateConversationId");
                        
                        if (onOpenChat != null) {
                            System.out.println("DEBUG [2] FriendItem calling callback with ID: " + chatId); // <--- IN RA
                            onOpenChat.accept(new ChatTarget(chatId, "PRIVATE"));
                        } else {
                            System.err.println("ERROR [2] FriendItem: onOpenChat callback is NULL!"); // <--- QUAN TRỌNG
                        }
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        }.execute();
    }

    // ========================================================================
    // 🔥 LOGIC BLOCK USER (LAZY UPDATE)
    // ========================================================================
    private void blockUser() {
        if (onRequestHandled != null) {
            onRequestHandled.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "BLOCK"));
        } else {
            this.setVisible(false);
        }

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                String url = ApiUrl.BLOCK_USER + "/" + friend.getUserId(); 
                return ApiClient.postJSON(url, new JSONObject(), token);
            }

            @Override
            protected void done() {
                try {
                    JSONObject response = get();
                    if (response == null || response.optInt("httpStatus", 200) >= 300) {
                        String msg = response != null ? response.optString("message") : "Unknown Error";
                        FriendItem.this.setVisible(true); // Rollback
                        JOptionPane.showMessageDialog(FriendItem.this, "Failed to block: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        System.out.println("Blocked successfully.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    FriendItem.this.setVisible(true); // Rollback
                    JOptionPane.showMessageDialog(FriendItem.this, "Network Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    // ========================================================================
    // LOGIC UNFRIEND (LAZY UPDATE)
    // ========================================================================
    private void unfriendUser() {
        if (onRequestHandled != null) {
            onRequestHandled.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "UNFRIEND"));
        } else {
            this.setVisible(false);
        }

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                String url = ApiUrl.REMOVE_FRIEND + "/" + friend.getUserId(); 
                return ApiClient.deleteJSON(url, new JSONObject(), token);
            }

            @Override
            protected void done() {
                try {
                    JSONObject response = get();
                    if (response == null || response.optInt("httpStatus", 200) >= 300) {
                        String msg = response != null ? response.optString("message") : "Error";
                        FriendItem.this.setVisible(true); 
                        JOptionPane.showMessageDialog(FriendItem.this, "Failed to unfriend: " + msg);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    FriendItem.this.setVisible(true);
                }
            }
        }.execute();
    }

    // ========================================================================
    // LOGIC CREATE GROUP
    // ========================================================================
    private void createGroupChat(String groupName) {
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                String url = ApiUrl.GROUP_CONVERSATION; 
                JSONObject body = new JSONObject();
                body.put("groupName", groupName);
                JSONArray userIds = new JSONArray();
                userIds.put(friend.getUserId());
                body.put("userIds", userIds);
                return ApiClient.postJSON(url, body, token);
            }
            @Override
            protected void done() {
                try {
                    JSONObject response = get();
                    if (response != null && response.optInt("httpStatus", 200) < 300) {
                        JOptionPane.showMessageDialog(FriendItem.this, "Group created successfully!");
                        if (onNavigateToChat != null) onNavigateToChat.run();
                    } else {
                        String msg = response != null ? response.optString("message") : "Error";
                        JOptionPane.showMessageDialog(FriendItem.this, "Failed: " + msg);
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        }.execute();
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 34)); 
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent evt) { btn.setBackground(bgColor); }
        });
        return btn;
    }

    public static class ChatTarget {
        public final int id;
        public final String type;
        public ChatTarget(int id, String type) {
            this.id = id;
            this.type = type;
        }
    }
}