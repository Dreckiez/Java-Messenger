package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.Contact;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionListener;
import javax.swing.plaf.basic.BasicButtonUI;

// 🔥 Import các lớp API và JSON cần thiết
import org.json.JSONObject;
import utils.ApiClient;
import utils.ApiUrl;
import utils.UserSession;

public class ContactItem extends BaseItem {

    private String isFriend = "none";
    private JButton actionButton;
    private Contact contact;
    
    // 🔥🔥 Biến để chứa Nội dung (CENTER + EAST), không chứa Avatar
    private JPanel mainContentPanel; 
    
    private final Color ME_BG_COLOR = new Color(236, 253, 245); 

    // --- MÀU SẮC ĐỒNG BỘ ---
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(148, 163, 184);
    private final Color COLOR_BLUE = new Color(59, 130, 246);
    private final Color COLOR_GREEN = new Color(16, 185, 129);
    private final Color COLOR_RED = new Color(239, 68, 68); 
    private final Color COLOR_YELLOW = new Color(245, 158, 11);
    private final Color BG_YELLOW_LIGHT = new Color(254, 243, 199);
    private boolean isExpanded = false;
    
    public ContactItem(Contact c) {
        super(c.getName(), c.getAvatarUrl());
        this.contact = c;
        this.isFriend = (c.getIsFriend() != null) ? c.getIsFriend().toLowerCase() : "none";
        initUI();
    }

    public ContactItem(String username, String avatarUrl, String isFriend) {
        super(username, avatarUrl);
        this.isFriend = (isFriend != null) ? isFriend.toLowerCase() : "none";
        initUI();
    }

    private void initUI() {
        
        boolean isMe = (isFriend == null || isFriend.isEmpty());

        setLayout(new BorderLayout(15, 0)); // Gap 15px giữa Avatar và nội dung
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // BƯỚC 1: KHẮC PHỤC AVATAR - Đặt lại Avatar vào WEST
        if (getComponentCount() > 0) {
            Component avatarComponent = getComponent(0);
            super.remove(avatarComponent); 
            add(avatarComponent, BorderLayout.WEST); // Avatar ở WEST
        }
        
        // KHAI BÁO CHIỀU CAO MỚI (Tăng lên 80px)
        final int BASE_HEIGHT = 80;
        final int EXPANDED_HEIGHT = 130; 
        
        // Cố định chiều cao
        if (isExpanded && isFriend.equals("received")) {
            setMaximumSize(new Dimension(Integer.MAX_VALUE, EXPANDED_HEIGHT));
        } else {
            setMaximumSize(new Dimension(Integer.MAX_VALUE, BASE_HEIGHT));
        }
        
        setCursor(isMe ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // BƯỚC 2: TẠO PANEL CHÍNH VÀ CẬP NHẬT NỘI DUNG
        if (mainContentPanel == null) {
            mainContentPanel = new JPanel(new BorderLayout()); // Panel chứa CENTER và EAST
            mainContentPanel.setOpaque(false);
            add(mainContentPanel, BorderLayout.CENTER);
        } else {
            mainContentPanel.removeAll(); // Chỉ xóa nội dung, không xóa Avatar
        }
        
        // --- CENTER PANEL (Name & Status) ---
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        if (isMe) {
            centerWrapper.setBackground(ME_BG_COLOR);
            centerWrapper.setOpaque(true);
        }
        centerWrapper.add(createCenterPanel(), BorderLayout.CENTER);
        
        // --- RIGHT PANEL (Action Button / Status Label) ---
        JPanel rightPanel = createRightPanel(); 
        
        if (isMe) {
            rightPanel.setBackground(ME_BG_COLOR);
            rightPanel.setOpaque(true);
        }

        // Thêm nội dung vào mainContentPanel
        mainContentPanel.add(centerWrapper, BorderLayout.CENTER);
        mainContentPanel.add(rightPanel, BorderLayout.EAST); 

        // --- EVENT ---
        if (!isMe && isFriend.equals("received")) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    isExpanded = !isExpanded;
                    updateUIContent(); 
                }
            });
        }
        
        if (isMe) {
            setBackground(ME_BG_COLOR);
        }
    }
    
    // 🔥 HÀM CẬP NHẬT UI NHẸ NHÀNG
    private void updateUIContent() {
        mainContentPanel.removeAll();
        
        final int BASE_HEIGHT = 80;
        final int EXPANDED_HEIGHT = 130; 

        // --- CENTER PANEL (Name & Status) ---
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        // ... (Giữ màu nền nếu là Me) ...
        centerWrapper.add(createCenterPanel(), BorderLayout.CENTER);
        
        // --- RIGHT PANEL (Action Button / Status Label) ---
        JPanel rightPanel = createRightPanel(); 
        // ... (Giữ màu nền nếu là Me) ...
        
        mainContentPanel.add(centerWrapper, BorderLayout.CENTER);
        mainContentPanel.add(rightPanel, BorderLayout.EAST); 
        
        // Cập nhật kích thước sau khi cập nhật nội dung
        if (isExpanded && isFriend.equals("received")) {
            setMaximumSize(new Dimension(Integer.MAX_VALUE, EXPANDED_HEIGHT));
        } else {
            setMaximumSize(new Dimension(Integer.MAX_VALUE, BASE_HEIGHT));
        }

        revalidate();
        repaint();
    }
    
    @Override
    protected JPanel createActionPanel() {
        return null;
    }
    
    // 🔥🔥 HÀM CẬP NHẬT CHUỖI SENTAT TRONG CONTACT VÀ UI (Giữ nguyên)
    private void updateContactSentAt(String newSentAt) {
        if (this.contact != null && newSentAt != null) {
            this.contact.setSentAt(newSentAt);
        }
        updateStatus("sent"); 
    }
    
    // 🔥🔥 HÀM CẬP NHẬT TRẠNG THÁI UI (Nhẹ hơn)
    private void updateStatus(String newStatus) {
        this.isFriend = newStatus.toLowerCase();
        if (this.contact != null) {
            this.contact.setIsFriend(newStatus); 
        }
        updateUIContent();
    }
    
    // 🔥🔥 HÀM XỬ LÝ GỬI LỜI KẾT BẠN (Giữ nguyên)
    private void sendFriendRequest(int userId) {
        if (actionButton != null) actionButton.setEnabled(false);
        
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                String url = ApiUrl.FRIEND_REQUEST + "/" + userId;
                String token = UserSession.getUser().getToken();
                return ApiClient.postJSON(url, new JSONObject(), token); 
            }

            @Override
            protected void done() {
                if (actionButton != null) actionButton.setEnabled(true);
                try {
                    JSONObject response = get();
                    String newSentAt = response.optString("message", null); 
                    
                    if (newSentAt != null && newSentAt.contains("T")) {
                        final String finalNewSentAt = newSentAt;
                        SwingUtilities.invokeLater(() -> {
                            if (contact != null) {
                                contact.setSentAt(finalNewSentAt);
                            }
                            updateStatus("sent");
                        });
                        
                    } else {
                        String message = response.optString("message", "Error sending request.");
                        JOptionPane.showMessageDialog(null, message, "Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Connection error.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
        
    // 🔥🔥 HÀM XỬ LÝ CẬP NHẬT TRẠNG THÁI (Giữ nguyên)
    private void updateFriendRequest(int status, String newUIStatus) {
        if (actionButton != null) actionButton.setEnabled(false);

        int senderId = contact.getUserId();
        int receiverId = UserSession.getUser().getId();
        
        if (isFriend.equals("received")) {
            senderId = contact.getUserId();
            receiverId = UserSession.getUser().getId();
        } else if (isFriend.equals("sent")) {
            senderId = UserSession.getUser().getId();
            receiverId = contact.getUserId();
        }
        
        final int finalSenderId = senderId;
        final int finalReceiverId = receiverId;
        
        String contactSentAt = (contact != null && contact.getSentAt() != null) ? 
                                contact.getSentAt() : 
                                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        final String finalSentAt = contactSentAt;

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                String url = ApiUrl.UPDATE_FRIEND_REQUEST;
                String token = UserSession.getUser().getToken();
                JSONObject requestBody = new JSONObject();
                requestBody.put("senderId", finalSenderId);
                requestBody.put("receiverId", finalReceiverId);
                requestBody.put("status", status); // 1: Accept, 2: Reject, 3: Cancel
                requestBody.put("sentAt", finalSentAt); 
                
                return ApiClient.postJSON(url, requestBody, token);
            }

            @Override
            protected void done() {
                if (actionButton != null) actionButton.setEnabled(true);
                try {
                    JSONObject response = get();
                    String message = response.optString("message", "Error processing request.");

                    if (message.contains("successfully")) {
                        SwingUtilities.invokeLater(() -> updateStatus(newUIStatus));
                    } else {
                        JOptionPane.showMessageDialog(null, message, "Failed", JOptionPane.ERROR_MESSAGE);
                        SwingUtilities.invokeLater(() -> updateStatus(isFriend));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Connection error.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }


    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        
        // 🔥 CỐ ĐỊNH CHIỀU RỘNG PANEL HÀNH ĐỘNG
        panel.setPreferredSize(new Dimension(75, 80)); 
        panel.setMaximumSize(new Dimension(75, Integer.MAX_VALUE));
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        if (isFriend == null || isFriend.isEmpty() || isFriend.equals("friend")) {
            panel.add(Box.createVerticalGlue());
            return panel;
        }

        // ---------- SENT (ĐÃ GỬI) -> HIỆN NÚT CANCEL ----------
        if (isFriend.equals("sent")) {
            actionButton = createPillButton("Cancel", COLOR_RED, new Color(254, 202, 202));
            actionButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
            actionButton.addActionListener(e -> updateFriendRequest(3, "none")); 
            panel.add(actionButton);
        }

        // ---------- NONE (Add Button) ----------
        else if (isFriend.equals("none")) {
            actionButton = createPillButton("Add", COLOR_BLUE, new Color(37, 99, 235));
            actionButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
            actionButton.addActionListener(e -> sendFriendRequest(contact.getUserId()));
            panel.add(actionButton);
        }

        // ---------- RECEIVED (Expand/Collapse Logic) ----------
        else if (isFriend.equals("received")) {
            
            if (isExpanded) {
                // Khi mở rộng, hiển thị 2 nút dọc
                JButton acceptBtn = createPillButton("Accept", COLOR_GREEN, new Color(22, 163, 74));
                JButton rejectBtn = createPillButton("Reject", COLOR_RED, new Color(254, 202, 202));

                acceptBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                rejectBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

                acceptBtn.addActionListener(e -> updateFriendRequest(1, "friend"));
                rejectBtn.addActionListener(e -> updateFriendRequest(2, "none"));

                panel.add(acceptBtn);
                panel.add(Box.createVerticalStrut(6));
                panel.add(rejectBtn);
            } 
            // Thông báo "Tap to respond" đã chuyển sang Center Panel
        }

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    @Override
    protected JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 4));
        panel.setOpaque(false);

        JLabel name = new JLabel(username);
        name.setFont(new Font("Segoe UI", Font.BOLD, 15));
        name.setForeground(TEXT_PRIMARY);

        JLabel status = new JLabel();
        status.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        status.setForeground(TEXT_SECONDARY);
        
        switch (isFriend) {
            case "friend":
                status.setText("Friend");
                status.setForeground(COLOR_GREEN);
                break;
            case "none":
                status.setText("Click to send request");
                break;
            case "sent":
                status.setText("Request sent");
                status.setForeground(COLOR_YELLOW);
                break;
                
            case "received":
                // 🔥 HIỂN THỊ TRẠNG THÁI Ở HÀNG DƯỚI (CENTER PANEL)
                if (!isExpanded) {
                    status.setText("New friend request (Tap to respond)");
                    status.setForeground(COLOR_RED);
                    status.setFont(new Font("Segoe UI", Font.ITALIC, 13));
                } else {
                    status.setText("Ready to respond");
                    status.setForeground(COLOR_BLUE);
                }
                break;
                
            default:
                status.setText("Status unknown");
                break;
        }

        if (isFriend == null || isFriend.isEmpty()) {
            panel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.weightx = 1.0;
            panel.add(name, gbc);
        } else {
            panel.add(name);
            panel.add(status);
        }

        return panel;
    }

    private JButton createPillButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Logic màu chữ cho các nút nền đậm
        button.setForeground(Color.WHITE); 
        
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Logic kích thước tối thiểu
        int preferredWidth = 30; 
        
        button.setPreferredSize(new Dimension(preferredWidth, 30));

        button.setUI(new CustomButtonUI(baseColor, hoverColor));
        return button;
    }

    public JButton getActionButton() { return actionButton; }

    
    // --- CÁC CLASS HELPER VÀ HÀM KẾT THÚC CỦA FILE ---

    static class CustomButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        private final Color baseColor;
        private final Color hoverColor;

        public CustomButtonUI(Color base, Color hover) {
            this.baseColor = base;
            this.hoverColor = hover;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            JButton button = (JButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color paintColor = baseColor;
            if (button.getModel().isRollover() && button.isEnabled()) {
                paintColor = hoverColor;
            }
            
            g2.setColor(paintColor);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), c.getHeight(), c.getHeight());
            
            super.paint(g2, c);
            g2.dispose();
        }
        
        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
            // Không vẽ gì
        }
    }
}