package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.UserSession;
import utils.ImageEditor;
import utils.ImageLoader;

public class ChatPanel extends JPanel {
    // --- UI Components ---
    private JPanel messagesPanel;
    private JTextField inputField;
    private JLabel nameLabel; 
    private JLabel headerAvatarLabel;
    private JButton infoBtn;
    
    // --- State & Logic ---
    private ImageEditor imageEditor;
    private Runnable onToggleInfo; 
    private boolean isInfoActive = false;
    
    // 🔥🔥 BIẾN TOKEN QUYỀN LỰC NHẤT: Token của lần request cuối cùng
    // Mỗi lần bấm chuyển tab, biến này sẽ thay đổi.
    private long lastHeaderRequestToken = 0; 
    
    // Lưu URL avatar đối tác để dùng làm fallback cho tin nhắn
    private String currentPartnerAvatarUrl = null; 
    private long currentChatId = -1; // Vẫn giữ để load tin nhắn

    // --- Colors ---
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color TEXT_COLOR = new Color(31, 41, 55);
    private final Color TIME_COLOR = new Color(156, 163, 175);
    private final Color ACTIVE_INFO_BG = new Color(219, 234, 254);

    public ChatPanel() {
        this.imageEditor = new ImageEditor();
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        JPanel headerPanel = createHeaderPanel();
        
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(BG_COLOR);
        messagesPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); 

        JScrollPane scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_COLOR);
        styleScrollBar(scrollPane);

        JPanel inputContainer = createInputPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputContainer, BorderLayout.SOUTH);
    }

    // =========================================================================
    // 🔥🔥🔥 HÀM UPDATE HEADER SỬ DỤNG TOKEN (SỬA LẠI HOÀN TOÀN)
    // =========================================================================
    
    // Hàm này tương thích với cả cách gọi cũ và mới
    public void updateChatHeader(String name, String avatarUrl, boolean isRefreshOnly) {
        // Chúng ta không cần chatId để chống race condition nữa, dùng Token tốt hơn.
        // Nhưng vẫn cập nhật chatId nội bộ nếu cần.
        updateChatHeaderState(-1, name, avatarUrl, isRefreshOnly);
    }
    
    public void updateChatHeader(long chatId, String name, String avatarUrl, boolean isRefreshOnly) {
        updateChatHeaderState(chatId, name, avatarUrl, isRefreshOnly);
    }

    private void updateChatHeaderState(long chatId, String name, String avatarUrl, boolean isRefreshOnly) {
        // 1. TẠO TOKEN MỚI CHO LẦN BẤM NÀY (Dấu thời gian Nano giây)
        long currentToken = System.nanoTime();
        this.lastHeaderRequestToken = currentToken; // Cập nhật "Con dấu" hiện tại

        // Cập nhật biến thành viên
        if (chatId != -1) this.currentChatId = chatId;
        this.nameLabel.setText(name);
        this.currentPartnerAvatarUrl = avatarUrl; 

        // 2. RESET VỀ PLACEHOLDER NGAY LẬP TỨC
        // Bắt buộc phải làm điều này để xóa ảnh của người cũ
        if (!isRefreshOnly) {
            headerAvatarLabel.setIcon(null); // Xóa sạch icon cũ
            headerAvatarLabel.setIcon(createAvatar(name, 40)); // Đặt placeholder
            headerAvatarLabel.repaint();
        }

        System.out.println("DEBUG CHAT: Request Avatar for '" + name + "' | Token: " + currentToken);

        // 3. LOAD ẢNH MỚI (NẾU CÓ)
        if (avatarUrl != null && !avatarUrl.isEmpty() && !"null".equals(avatarUrl)) {
            
            ImageLoader.loadImageAsync(avatarUrl, img -> {
                
                // 🔥🔥🔥 CHECK TOKEN: BƯỚC QUAN TRỌNG NHẤT
                // Nếu token hiện tại của class (lastHeaderRequestToken) KHÁC VỚI token của request này (currentToken)
                // -> Nghĩa là người dùng đã bấm sang chat khác rồi.
                if (ChatPanel.this.lastHeaderRequestToken != currentToken) {
                    System.out.println("⛔ ABORT: Ảnh cũ về trễ, bỏ qua. (Token mismatch)");
                    return; 
                }

                if (img != null) {
                    SwingUtilities.invokeLater(() -> {
                        // Check lại lần cuối trên luồng UI
                        if (ChatPanel.this.lastHeaderRequestToken == currentToken) {
                            ImageIcon icon = imageEditor.makeCircularImage(img, 40);
                            headerAvatarLabel.setIcon(icon);
                            headerAvatarLabel.repaint();
                            System.out.println("✅ SUCCESS: Đã cập nhật Avatar cho " + name);
                        }
                    });
                }
            });
            
        } else if (isRefreshOnly) {
            // Trường hợp refresh mà user xóa avatar -> về placeholder
            headerAvatarLabel.setIcon(createAvatar(name, 40));
        }
        
        // 4. Reset tin nhắn
        if (!isRefreshOnly) {
            messagesPanel.removeAll();
            messagesPanel.repaint();
        }
    }

    // =========================================================================
    //                            CÁC HÀM KHÁC (GIỮ NGUYÊN)
    // =========================================================================

    public void loadMessages(JSONArray messages, String partnerName) {
        loadMessages(this.currentChatId, messages, partnerName);
    }

    public void loadMessages(long chatId, JSONArray messages, String partnerName) {
        this.currentChatId = chatId;
        messagesPanel.removeAll();
        int myId = UserSession.getUser().getId(); 

        for (int i = messages.length() - 1; i >= 0; i--) {
            JSONObject msg = messages.getJSONObject(i);
            
            String content = msg.optString("content", "");
            int senderId = msg.optInt("senderId", -1);
            String rawTime = msg.optString("sentAt", "");
            String senderAvatarUrl = msg.optString("senderAvatar", null); 
            String senderDisplayName = msg.optString("senderName", partnerName);

            boolean isMe = (senderId == myId);
            String displayTime = formatTime(rawTime);
            
            // Fallback avatar
            if (!isMe && (senderAvatarUrl == null || senderAvatarUrl.isEmpty() || "null".equals(senderAvatarUrl))) {
                senderAvatarUrl = this.currentPartnerAvatarUrl;
            }

            addMessage(content, displayTime, (isMe ? "You" : senderDisplayName), isMe, senderAvatarUrl);
        }

        messagesPanel.revalidate();
        messagesPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
            if (scrollPane != null) {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        });
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20)); 

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        userInfoPanel.setOpaque(false);
        
        headerAvatarLabel = new JLabel();
        headerAvatarLabel.setPreferredSize(new Dimension(40, 40));
        headerAvatarLabel.setIcon(createAvatar("?", 40)); 

        nameLabel = new JLabel("Select a chat");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_COLOR);

        userInfoPanel.add(headerAvatarLabel);
        userInfoPanel.add(nameLabel);
        
        infoBtn = new JButton("ℹ️"); 
        infoBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        infoBtn.setBorderPainted(false);
        infoBtn.setContentAreaFilled(false);
        infoBtn.setFocusPainted(false);
        infoBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        infoBtn.setPreferredSize(new Dimension(40, 40));
        
        infoBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isInfoActive) {
                    g2.setColor(ACTIVE_INFO_BG);
                    g2.fillOval(0, 0, c.getWidth(), c.getHeight());
                }
                super.paint(g2, c);
                g2.dispose();
            }
        });
        
        infoBtn.addActionListener(e -> {
            toggleInfoState();
            if (onToggleInfo != null) onToggleInfo.run();
        });

        headerPanel.add(userInfoPanel, BorderLayout.WEST);
        headerPanel.add(infoBtn, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputContainer = new JPanel(new BorderLayout());
        inputContainer.setBackground(BG_COLOR);
        inputContainer.setBorder(new EmptyBorder(15, 20, 20, 20));

        RoundedPanel inputBar = new RoundedPanel(30, Color.WHITE);
        inputBar.setLayout(new BorderLayout());
        inputBar.setBorder(new EmptyBorder(5, 15, 5, 15));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(null);
        inputField.setBackground(Color.WHITE);
        
        setupPlaceholder(inputField, "Type a message and press Enter...");

        inputField.addActionListener(e -> sendMessage());

        inputBar.add(inputField, BorderLayout.CENTER);
        inputContainer.add(inputBar, BorderLayout.CENTER);
        return inputContainer;
    }

    public void setChatUser(String user) {
        updateChatHeaderState(-1, user, null, false);
    }
    
    public void setOnToggleInfo(Runnable onToggleInfo) { this.onToggleInfo = onToggleInfo; }
    
    public void setInfoActive(boolean isActive) {
        this.isInfoActive = isActive;
        infoBtn.repaint();
    }
    
    private void toggleInfoState() {
        this.isInfoActive = !this.isInfoActive;
        infoBtn.repaint();
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty() && !msg.equals("Type a message and press Enter...")) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            addMessage(msg, time, "You", true, null);
            inputField.setText("");
        }
    }

    private void addMessage(String message, String time, String senderName, boolean isMe, String avatarUrl) {
        // 🔥 FIX 1: Dùng Anonymous Class để KHÓA chiều cao tối đa bằng chiều cao ưu thích.
        // Điều này ngăn chặn BoxLayout kéo dãn tin nhắn khi danh sách còn trống.
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height); 
            }
        };
        wrapper.setOpaque(false);
        
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setOpaque(false);

        if (!isMe) {
            JLabel nameLbl = new JLabel(senderName);
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            nameLbl.setForeground(Color.GRAY);
            // Giảm padding tên
            nameLbl.setBorder(new EmptyBorder(0, 4, 1, 0));
            nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT); 
            contentBox.add(nameLbl);
        }

        MessageBubble bubble = new MessageBubble(message, isMe);
        bubble.setAlignmentX(isMe ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
        contentBox.add(bubble);

        JLabel timeLbl = new JLabel(time);
        timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLbl.setForeground(TIME_COLOR);
        // Giảm padding thời gian
        timeLbl.setBorder(new EmptyBorder(1, 2, 0, 2));
        
        if (isMe) {
            timeLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
            wrapper.add(contentBox, BorderLayout.EAST);
        } else {
            timeLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JPanel incomingContainer = new JPanel(new BorderLayout(8, 0)); 
            incomingContainer.setOpaque(false);
            
            JLabel avatarLabel = new JLabel();
            avatarLabel.setPreferredSize(new Dimension(32, 32));
            avatarLabel.setIcon(createAvatar(senderName, 32));

            if (avatarUrl != null && !avatarUrl.isEmpty() && !"null".equals(avatarUrl)) {
                ImageLoader.loadImageAsync(avatarUrl, img -> {
                    if (img != null) {
                        SwingUtilities.invokeLater(() -> {
                            if (avatarLabel.isShowing()) {
                                avatarLabel.setIcon(imageEditor.makeCircularImage(img, 32));
                                avatarLabel.repaint();
                            }
                        });
                    }
                });
            }
            
            // Xử lý vị trí Avatar: Đẩy lên trên cùng (NORTH) để không ảnh hưởng chiều cao
            JPanel avatarWrapper = new JPanel(new BorderLayout());
            avatarWrapper.setOpaque(false);
            avatarWrapper.add(avatarLabel, BorderLayout.NORTH);
            // Padding top 18px để ngang hàng với bong bóng chat (bỏ qua tên)
            // Nếu có tên (senderName), bạn có thể cần tăng giảm số 18 này
            avatarWrapper.setBorder(new EmptyBorder(isMe ? 0 : 18, 0, 0, 0)); 

            incomingContainer.add(avatarWrapper, BorderLayout.WEST);
            incomingContainer.add(contentBox, BorderLayout.CENTER);
            
            wrapper.add(incomingContainer, BorderLayout.WEST);
        }
        
        contentBox.add(timeLbl);
        messagesPanel.add(wrapper);
        
        // 🔥 FIX 2: Giảm khoảng cách giữa các tin nhắn xuống cố định 4px
        messagesPanel.add(Box.createVerticalStrut(4)); 
    }

    private String formatTime(String isoTime) {
        if (isoTime == null || isoTime.isEmpty()) return "";
        try {
            LocalDateTime dt = LocalDateTime.parse(isoTime);
            return dt.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) { return ""; }
    }

    private ImageIcon createAvatar(String name, int size) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(209, 213, 219)); 
        g2d.fillOval(0, 0, size, size);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, size / 2)); 
        String initial = (name != null && !name.isEmpty()) ? name.substring(0, 1).toUpperCase() : "?";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(initial, (size - fm.stringWidth(initial)) / 2, ((size - fm.getHeight()) / 2) + fm.getAscent());
        g2d.dispose();
        return new ImageIcon(img);
    }

    private void setupPlaceholder(JTextField field, String text) {
        field.setText(text);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(text)) { field.setText(""); field.setForeground(TEXT_COLOR); }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) { field.setText(text); field.setForeground(Color.GRAY); }
            }
        });
    }

    private void styleScrollBar(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200); this.trackColor = BG_COLOR;
            }
            @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton jbutton = new JButton(); jbutton.setPreferredSize(new Dimension(0, 0)); return jbutton;
            }
        });
    }

    // --- Inner Classes ---
    class RoundedPanel extends JPanel {
        private int radius; private Color backgroundColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius; this.backgroundColor = bgColor; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(new Color(230,230,230));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            super.paintComponent(g);
        }
    }

    class MessageBubble extends JPanel {
        private JTextArea textArea; 
        private boolean isMe; 
        private final int MAX_WIDTH = 300; 
        
        public MessageBubble(String message, boolean isMe) {
            this.isMe = isMe; 
            setLayout(new BorderLayout()); 
            setOpaque(false);
            
            textArea = new JTextArea(message);
            textArea.setWrapStyleWord(true); 
            textArea.setLineWrap(true); 
            textArea.setOpaque(false);
            textArea.setEditable(false); 
            textArea.setFocusable(false);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setForeground(isMe ? Color.WHITE : TEXT_COLOR);
            
            // 🔥 QUAN TRỌNG: Xóa margin mặc định để tính toán size chuẩn xác
            textArea.setMargin(new Insets(0,0,0,0)); 
            
            textArea.setSize(new Dimension(MAX_WIDTH, Short.MAX_VALUE));
            
            Dimension textSize = textArea.getPreferredSize();
            int bubbleWidth = Math.min(textSize.width + 24, MAX_WIDTH + 24);
            
            // Tính lại chiều cao với width đã chốt
            textArea.setSize(new Dimension(bubbleWidth - 24, Short.MAX_VALUE)); 
            int bubbleHeight = textArea.getPreferredSize().height + 12; // Padding trên dưới tổng 12px

            setPreferredSize(new Dimension(bubbleWidth, bubbleHeight));
            setMaximumSize(new Dimension(bubbleWidth, bubbleHeight));
            
            // Padding nội bộ bong bóng (6px trên/dưới, 12px trái/phải)
            setBorder(new EmptyBorder(6, 12, 6, 12));
            add(textArea, BorderLayout.CENTER);
        }
        
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isMe ? PRIMARY_COLOR : Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            if (!isMe) {
                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            }
            super.paintComponent(g);
        }
    }
}