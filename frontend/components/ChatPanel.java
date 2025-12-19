package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.UserSession;
import utils.ApiUrl;
import utils.ImageEditor;
import utils.ImageLoader;

public class ChatPanel extends JPanel {

    private String currentChatType;

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

    // 🔥🔥 BIẾN TOKEN QUYỀN LỰC NHẤT
    private long lastHeaderRequestToken = 0;

    // Pagination logic
    private boolean isLoadingHistory = false;
    private boolean hasMoreMessages = true;

    // Lưu URL avatar đối tác
    private String currentPartnerAvatarUrl = null;
    private long currentChatId = -1;

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

        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            if (!e.getValueIsAdjusting() && e.getValue() == 0) {
                if (!isLoadingHistory && hasMoreMessages && currentChatId != -1) {
                    loadMoreHistory();
                }
            }
        });

        JPanel inputContainer = createInputPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputContainer, BorderLayout.SOUTH);
    }

    // 🔥🔥🔥 HÀM CLEAR CHAT MỚI BỔ SUNG 🔥🔥🔥
    public void clearChat() {
        // 1. Reset Token để hủy các request load ảnh cũ đang treo
        this.lastHeaderRequestToken = System.nanoTime();

        // 2. Reset ID cuộc trò chuyện
        this.currentChatId = -1;
        this.currentPartnerAvatarUrl = null;

        // 3. Xóa tin nhắn
        if (messagesPanel != null) {
            messagesPanel.removeAll();
            messagesPanel.revalidate();
            messagesPanel.repaint();
        }

        // 4. Xóa nội dung nhập liệu
        if (inputField != null) {
            setupPlaceholder(inputField, "Type a message and press Enter...");
        }

        // 5. Reset Header về trạng thái mặc định
        if (nameLabel != null)
            nameLabel.setText("Select a chat");
        if (headerAvatarLabel != null) {
            headerAvatarLabel.setIcon(createAvatar("?", 40));
            headerAvatarLabel.repaint();
        }

        // 6. Reset nút Info
        this.isInfoActive = false;
        if (infoBtn != null)
            infoBtn.repaint();
    }

    // =========================================================================
    // 🔥 CÁC HÀM CŨ GIỮ NGUYÊN BÊN DƯỚI
    // =========================================================================

    public void updateChatHeader(String name, String avatarUrl, boolean isRefreshOnly) {
        updateChatHeaderState(-1, name, avatarUrl, isRefreshOnly);
    }

    public void updateChatHeader(long chatId, String name, String avatarUrl, boolean isRefreshOnly) {
        updateChatHeaderState(chatId, name, avatarUrl, isRefreshOnly);
    }

    private void updateChatHeaderState(long chatId, String name, String avatarUrl, boolean isRefreshOnly) {
        long currentToken = System.nanoTime();
        this.lastHeaderRequestToken = currentToken;

        if (chatId != -1)
            this.currentChatId = chatId;
        this.nameLabel.setText(name);
        this.currentPartnerAvatarUrl = avatarUrl;

        if (!isRefreshOnly) {
            headerAvatarLabel.setIcon(null);
            headerAvatarLabel.setIcon(createAvatar(name, 40));
            headerAvatarLabel.repaint();
        }

        if (avatarUrl != null && !avatarUrl.isEmpty() && !"null".equals(avatarUrl)) {
            ImageLoader.loadImageAsync(avatarUrl, img -> {
                if (ChatPanel.this.lastHeaderRequestToken != currentToken)
                    return;

                if (img != null) {
                    SwingUtilities.invokeLater(() -> {
                        if (ChatPanel.this.lastHeaderRequestToken == currentToken) {
                            ImageIcon icon = imageEditor.makeCircularImage(img, 40);
                            headerAvatarLabel.setIcon(icon);
                            headerAvatarLabel.repaint();
                        }
                    });
                }
            });

        } else if (isRefreshOnly) {
            headerAvatarLabel.setIcon(createAvatar(name, 40));
        }

        if (!isRefreshOnly) {
            messagesPanel.removeAll();
            messagesPanel.repaint();
        }
    }

    public void loadMessages(JSONArray messages, String partnerName) {
        loadMessages(this.currentChatId, messages, partnerName);
    }

    public void loadMessages(long chatId, JSONArray messages, String partnerName) {
        if (this.currentChatId != chatId)
            return;

        this.isLoadingHistory = true;

        this.currentChatId = chatId;
        messagesPanel.removeAll();
        int myId = UserSession.getUser().getId();

        this.hasMoreMessages = true;

        for (int i = messages.length() - 1; i >= 0; i--) {
            JSONObject msg = messages.getJSONObject(i);

            String content = msg.optString("content", "");
            int senderId = msg.optInt("senderId", -1);
            String rawTime = msg.optString("sentAt", "");
            String senderAvatarUrl = msg.optString("senderAvatar", null);
            String senderDisplayName = msg.optString("senderName", partnerName);

            long msgId = msg.optLong("groupConversationMessageId", -1);
            if (msgId == -1)
                msgId = msg.optLong("privateConversationMessageId", -1);

            boolean isMe = (senderId == myId);
            String displayTime = formatTime(rawTime);

            if (!isMe && (senderAvatarUrl == null || senderAvatarUrl.isEmpty() || "null".equals(senderAvatarUrl))) {
                senderAvatarUrl = this.currentPartnerAvatarUrl;
            }

            addMessage(msgId, content, displayTime, (isMe ? "You" : senderDisplayName), isMe, senderAvatarUrl);
        }

        messagesPanel.revalidate();
        messagesPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            scrollToBottom();

            SwingUtilities.invokeLater(() -> {
                this.isLoadingHistory = false;
            });
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
            if (onToggleInfo != null)
                onToggleInfo.run();
        });

        headerPanel.add(userInfoPanel, BorderLayout.WEST);
        headerPanel.add(infoBtn, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputContainer = new JPanel(new BorderLayout());
        inputContainer.setBackground(BG_COLOR);

        inputContainer.setBorder(new EmptyBorder(15, 20, 20, 20));

        // Increase the radius slightly for a smoother look
        RoundedPanel inputBar = new RoundedPanel(35, Color.WHITE);
        inputBar.setLayout(new BorderLayout());

        inputBar.setBorder(new EmptyBorder(10, 15, 10, 15));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(null);
        inputField.setBackground(Color.WHITE);

        inputField.setCaretColor(Color.BLACK);

        setupPlaceholder(inputField, "Type a message and press Enter...");

        inputField.addActionListener(e -> sendMessage());

        inputBar.add(inputField, BorderLayout.CENTER);
        inputContainer.add(inputBar, BorderLayout.CENTER);
        return inputContainer;
    }

    public void setChatUser(String user) {
        updateChatHeaderState(-1, user, null, false);
    }

    public void setOnToggleInfo(Runnable onToggleInfo) {
        this.onToggleInfo = onToggleInfo;
    }

    public void setInfoActive(boolean isActive) {
        this.isInfoActive = isActive;
        infoBtn.repaint();
    }

    private void toggleInfoState() {
        this.isInfoActive = !this.isInfoActive;
        infoBtn.repaint();
    }

    public void setCurrentChatType(String type) {
        this.currentChatType = type;
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();

        // Validation
        if (msg.isEmpty() || msg.equals("Type a message and press Enter...") || this.currentChatId == -1) {
            return;
        }

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        addMessage(0, msg, time, "You", true, null);

        scrollToBottom();

        inputField.setText("");

        long chatId = this.currentChatId;
        String token = UserSession.getUser().getToken();
        String content = msg;

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Create JSON Payload
                    JSONObject json = new JSONObject();
                    json.put("content", content);
                    json.put("type", 0);

                    String url;
                    if ("GROUP".equalsIgnoreCase(currentChatType)) {
                        url = ApiUrl.GROUP_CONVERSATION + "/" + chatId + "/group-conversation-messages";
                    } else {
                        url = ApiUrl.PRIVATE_CONVERSATION + "/" + chatId + "/private-conversation-messages";
                    }

                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() != 200) {
                        System.err.println("❌ Failed to send message. Code: " + response.statusCode());
                        System.err.println("Response: " + response.body());
                    } else {
                        System.out.println("✅ Message sent to DB!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("❌ Error sending message: " + e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    public void addSocketMessage(models.MessageWsResponse msg) {
        // 1. Format time
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        if (msg.getSentAt() != null) {
            // Parse time from server if needed, or just use current time for responsiveness
            time = formatTime(msg.getSentAt());
        }

        // 2. Determine sender
        boolean isMe = (msg.getUserId() == utils.UserSession.getUser().getId());
        String senderName = msg.getName();
        String avatarUrl = msg.getAvatarUrl();

        long msgId = (msg.getGroupConversationMessageId() != null && msg.getGroupConversationMessageId() > 0)
                ? msg.getGroupConversationMessageId()
                : msg.getPrivateConversationMessageId();

        // 3. Add to UI
        addMessage(msgId, msg.getContent(), time, senderName, isMe, avatarUrl);

        // 4. Scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
            if (scrollPane != null) {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        });
    }

    private void addMessage(long messageId, String message, String time, String senderName, boolean isMe,
            String avatarUrl) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height);
            }
        };
        wrapper.setOpaque(false);

        wrapper.putClientProperty("msgId", messageId);

        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setOpaque(false);

        if (!isMe) {
            JLabel nameLbl = new JLabel(senderName);
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            nameLbl.setForeground(Color.GRAY);
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

            JPanel avatarWrapper = new JPanel(new BorderLayout());
            avatarWrapper.setOpaque(false);
            avatarWrapper.add(avatarLabel, BorderLayout.NORTH);
            avatarWrapper.setBorder(new EmptyBorder(isMe ? 0 : 18, 0, 0, 0));

            incomingContainer.add(avatarWrapper, BorderLayout.WEST);
            incomingContainer.add(contentBox, BorderLayout.CENTER);

            wrapper.add(incomingContainer, BorderLayout.WEST);
        }

        contentBox.add(timeLbl);
        messagesPanel.add(wrapper);
        messagesPanel.add(Box.createVerticalStrut(4));
    }

    private String formatTime(String rawTime) {
        if (rawTime == null || rawTime.isEmpty() || "null".equals(rawTime))
            return "";
        try {
            // 🔥 Fix for Spring Boot Date Array format [yyyy, MM, dd, HH, mm, ss]
            if (rawTime.startsWith("[")) {
                String[] parts = rawTime.replace("[", "").replace("]", "").split(",");
                if (parts.length >= 5) {
                    int hour = Integer.parseInt(parts[3].trim());
                    int minute = Integer.parseInt(parts[4].trim());
                    return String.format("%02d:%02d", hour, minute);
                }
            }

            // Standard ISO parsing
            LocalDateTime dt = LocalDateTime.parse(rawTime);
            return dt.format(DateTimeFormatter.ofPattern("HH:mm"));

        } catch (Exception e) {
            System.out.println("Date parse error (ignoring): " + rawTime);
            return ""; // Return empty so the message still displays!
        }
    }

    private ImageIcon createAvatar(String name, int size) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
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

        field.setCaretColor(Color.BLACK);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Use .equals() to check if the current text is the placeholder
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR); // Make sure TEXT_COLOR is defined (e.g., Color.BLACK)
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void styleScrollBar(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200);
                this.trackColor = new Color(250, 250, 250);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                return jbutton;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 6, 6);
                g2.dispose();
            }
        });
    }

    private void loadMoreHistory() {
        if (messagesPanel.getComponentCount() == 0)
            return;

        isLoadingHistory = true;

        // 1. Get the ID of the top-most message (Cursor)
        Component topMsg = messagesPanel.getComponent(0);
        Object idObj = ((JComponent) topMsg).getClientProperty("msgId");

        if (idObj == null) {
            isLoadingHistory = false;
            return;
        }

        long cursorId = (long) idObj;
        long chatId = this.currentChatId;
        String token = UserSession.getUser().getToken();
        boolean isGroup = "GROUP".equalsIgnoreCase(currentChatType);

        System.out.println("DEBUG: Loading history before ID: " + cursorId);

        new SwingWorker<JSONArray, Void>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                // Build URL with cursor
                String url;
                if (isGroup) {
                    url = ApiUrl.GROUP_CONVERSATION + "/" + chatId + "/group-conversation-messages?cursorId="
                            + cursorId;
                } else {
                    url = ApiUrl.PRIVATE_CONVERSATION + "/" + chatId + "/private-conversation-messages?cursorId="
                            + cursorId;
                }

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + token)
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JSONObject json = new JSONObject(response.body());
                    if (isGroup && json.has("groupConversationMessageResponseList")) {
                        return json.getJSONArray("groupConversationMessageResponseList");
                    } else if (!isGroup && json.has("privateConversationMessageResponseList")) {
                        return json.getJSONArray("privateConversationMessageResponseList");
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    JSONArray messages = get();
                    if (messages != null && messages.length() > 0) {
                        prependMessages(messages);
                    } else {
                        hasMoreMessages = false;
                        System.out.println("DEBUG: No more history.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isLoadingHistory = false;
                }
            }
        }.execute();
    }

    private void prependMessages(JSONArray messages) {
        JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();

        int oldViewHeight = scrollPane.getViewport().getViewSize().height;
        int myId = UserSession.getUser().getId();

        for (int i = 0; i < messages.length(); i++) {
            JSONObject msg = messages.getJSONObject(i);

            String content = msg.optString("content", "");
            int senderId = msg.optInt("senderId", -1);
            String rawTime = msg.optString("sentAt", "");
            String senderAvatarUrl = msg.optString("senderAvatar", null);
            String senderDisplayName = msg.optString("senderName", "Unknown");

            long msgId = msg.optLong("groupConversationMessageId", -1);
            if (msgId == -1)
                msgId = msg.optLong("privateConversationMessageId", -1);

            boolean isMe = (senderId == myId);
            String displayTime = formatTime(rawTime);

            if (!isMe && (senderAvatarUrl == null || senderAvatarUrl.isEmpty() || "null".equals(senderAvatarUrl))) {
                senderAvatarUrl = this.currentPartnerAvatarUrl;
            }

            // --- Manual Add Logic (Similar to addMessage but at index 0) ---
            JPanel wrapper = new JPanel(new BorderLayout()) {
                @Override
                public Dimension getMaximumSize() {
                    Dimension pref = getPreferredSize();
                    return new Dimension(Integer.MAX_VALUE, pref.height);
                }
            };
            wrapper.setOpaque(false);
            wrapper.putClientProperty("msgId", msgId); // Store ID

            JPanel contentBox = new JPanel();
            contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
            contentBox.setOpaque(false);

            if (!isMe) {
                JLabel nameLbl = new JLabel(senderDisplayName);
                nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                nameLbl.setForeground(Color.GRAY);
                nameLbl.setBorder(new EmptyBorder(0, 4, 1, 0));
                nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentBox.add(nameLbl);
            }

            MessageBubble bubble = new MessageBubble(content, isMe);
            bubble.setAlignmentX(isMe ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
            contentBox.add(bubble);

            JLabel timeLbl = new JLabel(displayTime);
            timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            timeLbl.setForeground(TIME_COLOR);
            timeLbl.setBorder(new EmptyBorder(1, 2, 0, 2));

            if (isMe) {
                timeLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
                wrapper.add(contentBox, BorderLayout.EAST);
            } else {
                timeLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Avatar logic (Simplified for brevity, copy full logic if needed)
                JPanel incomingContainer = new JPanel(new BorderLayout(8, 0));
                incomingContainer.setOpaque(false);
                JLabel avatarLabel = new JLabel();
                avatarLabel.setPreferredSize(new Dimension(32, 32));
                // Note: For history, synchronous or cached image loading is better to avoid
                // popping
                // Here we use placeholder, you can add async logic if desired
                avatarLabel.setIcon(createAvatar(senderDisplayName, 32));

                if (senderAvatarUrl != null && !senderAvatarUrl.isEmpty() && !"null".equals(senderAvatarUrl)) {
                    String finalUrl = senderAvatarUrl;
                    ImageLoader.loadImageAsync(finalUrl, img -> {
                        if (img != null) {
                            SwingUtilities.invokeLater(() -> {
                                avatarLabel.setIcon(imageEditor.makeCircularImage(img, 32));
                                avatarLabel.repaint();
                            });
                        }
                    });
                }

                JPanel avatarWrapper = new JPanel(new BorderLayout());
                avatarWrapper.setOpaque(false);
                avatarWrapper.add(avatarLabel, BorderLayout.NORTH);
                avatarWrapper.setBorder(new EmptyBorder(isMe ? 0 : 18, 0, 0, 0));

                incomingContainer.add(avatarWrapper, BorderLayout.WEST);
                incomingContainer.add(contentBox, BorderLayout.CENTER);
                wrapper.add(incomingContainer, BorderLayout.WEST);
            }
            contentBox.add(timeLbl);

            // 🔥 ADD TO TOP
            messagesPanel.add(wrapper, 0);
            messagesPanel.add(Box.createVerticalStrut(4), 1); // Add spacing below it
        }

        messagesPanel.revalidate();
        scrollPane.validate();

        // 3. Restore Scroll Position (The Magic Trick)
        int newViewHeight = scrollPane.getViewport().getViewSize().height;
        int heightDifference = newViewHeight - oldViewHeight;

        SwingUtilities.invokeLater(() -> {
            verticalBar.setValue(heightDifference);
        });
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            if (messagesPanel.getParent() != null && messagesPanel.getParent().getParent() instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        });
    }

    // --- Inner Classes ---
    class RoundedPanel extends JPanel {
        private int radius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(new Color(230, 230, 230));
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
            textArea.setMargin(new Insets(0, 0, 0, 0));

            textArea.setSize(new Dimension(MAX_WIDTH, Short.MAX_VALUE));

            Dimension textSize = textArea.getPreferredSize();
            int bubbleWidth = Math.min(textSize.width + 24, MAX_WIDTH + 24);

            textArea.setSize(new Dimension(bubbleWidth - 24, Short.MAX_VALUE));
            int bubbleHeight = textArea.getPreferredSize().height + 12;

            setPreferredSize(new Dimension(bubbleWidth, bubbleHeight));
            setMaximumSize(new Dimension(bubbleWidth, bubbleHeight));

            setBorder(new EmptyBorder(6, 12, 6, 12));
            add(textArea, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
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