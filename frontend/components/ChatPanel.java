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

    private boolean isJumping = false;
    private boolean isProgrammaticScroll = false;

    private boolean isLoadingFuture = false;
    private boolean hasMoreFutureMessages = false;

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
            if (e.getValueIsAdjusting())
                return;

            JScrollBar sb = (JScrollBar) e.getSource();

            if (isProgrammaticScroll) {
                return;
            }
            if (isJumping) {
                return;
            }

            // 1. SCROLL UP (Load History)
            if (sb.getValue() == 0) {
                if (!isLoadingHistory && hasMoreMessages && currentChatId != -1) {
                    loadMoreHistory();
                }
            }

            // 2. SCROLL DOWN (Load Future)
            // Check if we are near the bottom
            if (isUserAtBottom(sb)) {
                if (!isLoadingFuture && hasMoreFutureMessages && currentChatId != -1) {
                    loadMoreFuture();
                }
            }

        });

        JPanel inputContainer = createInputPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputContainer, BorderLayout.SOUTH);
    }

    private void loadMoreFuture() {
        if (messagesPanel.getComponentCount() == 0)
            return;

        isLoadingFuture = true;

        // 1. Get ID of the BOTTOM message (Newest loaded so far)
        Component bottomMsg = messagesPanel.getComponent(messagesPanel.getComponentCount() - 1);
        // Sometimes there's a strut/spacer at the bottom, check index
        if (!(bottomMsg instanceof JPanel)) {
            bottomMsg = messagesPanel.getComponent(messagesPanel.getComponentCount() - 2);
        }

        Object idObj = ((JComponent) bottomMsg).getClientProperty("msgId");
        if (idObj == null) {
            isLoadingFuture = false;
            return;
        }
        long newerCursorId = (long) idObj;

        long chatId = this.currentChatId;
        String token = UserSession.getUser().getToken();
        boolean isGroup = "GROUP".equalsIgnoreCase(currentChatType);

        new SwingWorker<JSONArray, Void>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                String url;
                if (isGroup) {
                    url = ApiUrl.GROUP_CONVERSATION + "/" + chatId
                            + "/group-conversation-messages?newerCursorId=" + newerCursorId;
                } else {
                    url = ApiUrl.PRIVATE_CONVERSATION + "/" + chatId
                            + "/private-conversation-messages?newerCursorId=" + newerCursorId;
                }
                return utils.ApiClient.getJSON(url, token).optJSONArray(
                        isGroup ? "groupConversationMessageResponseList" : "privateConversationMessageResponseList");
            }

            @Override
            protected void done() {
                try {
                    JSONArray messages = get();
                    if (messages != null && messages.length() > 0) {
                        appendMessages(messages);
                    } else {
                        hasMoreFutureMessages = false; // We reached the live head
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isLoadingFuture = false;
                }
            }
        }.execute();
    }

    private void appendMessages(JSONArray messages) {
        JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();

        // Save current scroll position
        // int oldValue = verticalBar.getValue();
        // int oldMax = verticalBar.getMaximum();

        int myId = UserSession.getUser().getId();

        for (int i = messages.length() - 1; i >= 0; i--) {
            JSONObject msg = messages.getJSONObject(i);

            // --- Parse Data ---
            String content = msg.optString("content", "");
            int senderId = msg.optInt("senderId", -1);
            String rawTime = msg.optString("sentAt", "");
            String senderAvatarUrl = msg.optString("senderAvatar", null);

            long msgId = msg.optLong("groupConversationMessageId", -1);
            if (msgId == -1)
                msgId = msg.optLong("privateConversationMessageId", -1);

            boolean isMe = (senderId == myId);
            String displayTime = formatTime(rawTime);

            if (!isMe && (senderAvatarUrl == null || "null".equals(senderAvatarUrl))) {
                senderAvatarUrl = this.currentPartnerAvatarUrl;
            }

            String senderDisplayName = msg.optString("senderName");
            if (senderDisplayName == null || senderDisplayName.isEmpty()) {
                String fname = msg.optString("firstName", "");
                String lname = msg.optString("lastName", "");
                if (!fname.isEmpty() || !lname.isEmpty()) {
                    senderDisplayName = (fname + " " + lname).trim();
                } else {
                    // Fallback for private chat: If not me, use the partner's name passed in
                    // headers
                    senderDisplayName = isMe ? "You" : "User";
                }
            }

            // --- Add to UI ---
            addMessage(msgId, content, displayTime, (isMe ? "You" : senderDisplayName), isMe, senderAvatarUrl);
        }

        messagesPanel.revalidate();
        scrollPane.validate();
        messagesPanel.repaint();

        // int newMax = verticalBar.getMaximum();
        // int maxDiff = newMax - oldMax;

        // SwingUtilities.invokeLater(() -> {
        // isProgrammaticScroll = true;
        // verticalBar.setValue(oldValue + maxDiff);
        // SwingUtilities.invokeLater(() -> isProgrammaticScroll = false);
        // });
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

            long msgId = msg.optLong("groupConversationMessageId", -1);
            if (msgId == -1)
                msgId = msg.optLong("privateConversationMessageId", -1);

            boolean isMe = (senderId == myId);
            String displayTime = formatTime(rawTime);

            String senderDisplayName = msg.optString("senderName");
            if (senderDisplayName == null || senderDisplayName.isEmpty()) {
                String fname = msg.optString("firstName", "");
                String lname = msg.optString("lastName", "");
                if (!fname.isEmpty() || !lname.isEmpty()) {
                    senderDisplayName = (fname + " " + lname).trim();
                } else {
                    // Fallback for private chat: If not me, use the partner's name passed in
                    // headers
                    senderDisplayName = isMe ? "You" : "User";
                }
            }

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

        this.hasMoreFutureMessages = false;
        this.hasMoreMessages = true;
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

                    // 🔥 FIX: Calculate a centered square for a perfect circle
                    int size = Math.min(c.getWidth(), c.getHeight());
                    int x = (c.getWidth() - size) / 2;
                    int y = (c.getHeight() - size) / 2;

                    g2.fillOval(x, y, size, size);
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

    public String getCurrentPartnerAvatarUrl() {
        return this.currentPartnerAvatarUrl;
    }

    public void jumpToMessage(long messageId) {
        if (this.currentChatId == -1)
            return;

        this.isJumping = true;
        this.isLoadingHistory = true;
        this.isLoadingFuture = true;

        messagesPanel.removeAll(); // Clear current messages
        messagesPanel.repaint();

        // Show a loading indicator (optional)
        // messagesPanel.add(new JLabel("Loading context..."));

        long chatId = this.currentChatId;
        String token = UserSession.getUser().getToken();
        boolean isGroup = "GROUP".equalsIgnoreCase(currentChatType);

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                // Build URL with jumpToMessageId
                String url;
                if (isGroup) {
                    url = ApiUrl.GROUP_CONVERSATION + "/" + chatId
                            + "/group-conversation-messages?jumpToMessageId=" + messageId;
                } else {
                    url = ApiUrl.PRIVATE_CONVERSATION + "/" + chatId
                            + "/private-conversation-messages?jumpToMessageId=" + messageId;
                }

                // Use existing ApiClient
                return utils.ApiClient.getJSON(url, token);
            }

            @Override
            protected void done() {
                try {
                    JSONObject response = get();
                    if (response != null) {
                        JSONArray messages = null;

                        // Parse list based on type
                        if (isGroup && response.has("groupConversationMessageResponseList")) {
                            messages = response.getJSONArray("groupConversationMessageResponseList");
                        } else if (!isGroup && response.has("privateConversationMessageResponseList")) {
                            messages = response.getJSONArray("privateConversationMessageResponseList");
                        }

                        if (messages != null) {
                            // 1. Render the messages (Reuse your existing loadMessages logic somewhat)
                            // We use 'loadMessages' style logic but we don't want to scroll to bottom
                            // automatically
                            renderJumpContext(messages);

                            // 2. Scroll to the specific message ID
                            scrollToMessage(messageId);

                            hasMoreFutureMessages = true;
                            hasMoreMessages = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ChatPanel.this, "Failed to load message context.");
                    isJumping = false;
                    isProgrammaticScroll = false;
                } finally {
                    isLoadingHistory = false;
                    isLoadingFuture = false;
                }
            }
        }.execute();
    }

    private void renderJumpContext(JSONArray messages) {
        messagesPanel.removeAll();
        int myId = UserSession.getUser().getId();

        // Based on your previous code 'loadMessages', you loop backwards (i--),
        // implying the API returns Newest First.
        for (int i = messages.length() - 1; i >= 0; i--) {
            JSONObject msg = messages.getJSONObject(i);

            String content = msg.optString("content", "");
            int senderId = msg.optInt("senderId", -1);
            String rawTime = msg.optString("sentAt", "");
            String senderAvatarUrl = msg.optString("senderAvatar", null);

            long msgId = msg.optLong("groupConversationMessageId", -1);
            if (msgId == -1)
                msgId = msg.optLong("privateConversationMessageId", -1);

            boolean isMe = (senderId == myId);
            String displayTime = formatTime(rawTime);

            if (!isMe && (senderAvatarUrl == null || "null".equals(senderAvatarUrl))) {
                senderAvatarUrl = this.currentPartnerAvatarUrl;
            }

            String senderDisplayName = msg.optString("senderName");
            if (senderDisplayName == null || senderDisplayName.isEmpty()) {
                String fname = msg.optString("firstName", "");
                String lname = msg.optString("lastName", "");
                if (!fname.isEmpty() || !lname.isEmpty()) {
                    senderDisplayName = (fname + " " + lname).trim();
                } else {
                    // Fallback for private chat: If not me, use the partner's name passed in
                    // headers
                    senderDisplayName = isMe ? "You" : "User";
                }
            }

            addMessage(msgId, content, displayTime, senderDisplayName, isMe, senderAvatarUrl);
        }

        messagesPanel.revalidate();
        messagesPanel.repaint();
    }

    private void scrollToMessage(long targetId) {
        SwingUtilities.invokeLater(() -> {
            boolean found = false;
            for (Component comp : messagesPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    Object idObj = ((JPanel) comp).getClientProperty("msgId");

                    if (idObj != null && (long) idObj == targetId) {
                        isProgrammaticScroll = true;

                        Rectangle bounds = comp.getBounds();

                        bounds.y = Math.max(0, bounds.y - 50);
                        bounds.height += 100;
                        messagesPanel.scrollRectToVisible(bounds);

                        flashMessage((JPanel) comp);

                        Timer stabilizationTimer = new Timer(1000, evt -> {
                            isProgrammaticScroll = false;
                            isJumping = false;
                        });
                        stabilizationTimer.setRepeats(false);
                        stabilizationTimer.start();

                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                isProgrammaticScroll = false;
                isJumping = false;
            }
        });
    }

    private void flashMessage(JPanel wrapper) {
        Color original = wrapper.getBackground();
        Color highlight = new Color(28, 180, 255); // Light Yellow

        wrapper.setOpaque(true);
        wrapper.setBackground(highlight);

        Timer timer = new Timer(2000, e -> {
            wrapper.setBackground(original);
            wrapper.setOpaque(false);
            wrapper.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public JLabel getNameLabel() {
        return this.nameLabel;
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

        if (msg.isEmpty() || msg.equals("Type a message and press Enter...") || this.currentChatId == -1) {
            return;
        }

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        // 1. Add Optimistic Bubble (ID = 0)
        // We capture the returned JPanel so we can update it later
        JPanel pendingBubble = addMessage(0, msg, time, "You", true, null);

        messagesPanel.revalidate();
        messagesPanel.repaint();

        scrollToBottom();
        inputField.setText("");

        long chatId = this.currentChatId;
        String token = UserSession.getUser().getToken();
        String content = msg;

        // Change SwingWorker to return Long (the new ID)
        new SwingWorker<Long, Void>() {
            @Override
            protected Long doInBackground() throws Exception {
                try {
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

                    if (response.statusCode() == 200) {
                        // 2. Parse the Real ID from Server Response
                        JSONObject respJson = new JSONObject(response.body());
                        // Check keys based on your DTO
                        if (respJson.has("messageId"))
                            return respJson.getLong("messageId");
                        if (respJson.has("groupConversationMessageId"))
                            return respJson.getLong("groupConversationMessageId");
                        if (respJson.has("privateConversationMessageId"))
                            return respJson.getLong("privateConversationMessageId");
                    } else {
                        System.err.println("❌ Failed. Code: " + response.statusCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    Long realId = get();
                    if (realId != null) {
                        // 3. 🔥 UPDATE THE UI BUBBLE WITH REAL ID
                        pendingBubble.putClientProperty("msgId", realId);
                        System.out.println("✅ UI Updated: Temp ID 0 -> Real ID " + realId);

                        // Force a repaint just to be safe
                        pendingBubble.repaint();
                    } else {
                        // Optional: Mark bubble as "Failed" (red border?)
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public void addSocketMessage(models.MessageWsResponse msg) {
        if ("DELETE".equalsIgnoreCase(msg.getRealTimeAction())) {
            long msgId = (msg.getGroupConversationMessageId() != null && msg.getGroupConversationMessageId() > -1)
                    ? msg.getGroupConversationMessageId()
                    : msg.getPrivateConversationMessageId();

            removeMessageFromUI(msgId);
            return; // Stop here! Do not draw a bubble.
        }
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

        JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
        JScrollBar sb = scrollPane.getVerticalScrollBar();

        boolean wasAtBottom = isUserAtBottom(sb);

        addMessage(msgId, msg.getContent(), time, senderName, isMe, avatarUrl);

        scrollToBottom();

        messagesPanel.revalidate();
        messagesPanel.repaint();

        if (wasAtBottom) {
            SwingUtilities.invokeLater(() -> {
                isProgrammaticScroll = true;
                sb.setValue(sb.getMaximum());
                SwingUtilities.invokeLater(() -> isProgrammaticScroll = false);
            });
        }
    }

    private JPanel addMessage(long messageId, String message, String time, String senderName, boolean isMe,
            String avatarUrl) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        // This property is the "Brain" of the bubble. We will update this later!
        wrapper.putClientProperty("msgId", messageId);
        wrapper.setBorder(new EmptyBorder(2, 0, 2, 0));

        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setOpaque(false);

        if (!isMe) {
            JLabel nameLbl = new JLabel(senderName);
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nameLbl.setForeground(new Color(100, 100, 100));
            nameLbl.setBorder(new EmptyBorder(0, 4, 2, 0));
            nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentBox.add(nameLbl);
        }

        MessageBubble bubble = new MessageBubble(message, isMe);
        bubble.setAlignmentX(isMe ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

        if (isMe) {
            JPanel bubbleRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            bubbleRow.setOpaque(false);
            bubbleRow.setAlignmentX(Component.RIGHT_ALIGNMENT);

            JButton optionsBtn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isRollover()) {
                        g2.setColor(new Color(240, 240, 240));
                        g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                    }
                    g2.setColor(new Color(160, 160, 160));
                    int dotSize = 4;
                    int x = (getWidth() - dotSize) / 2;
                    int yStart = (getHeight() - (dotSize * 3 + 4)) / 2;
                    g2.fillOval(x, yStart, dotSize, dotSize);
                    g2.fillOval(x, yStart + dotSize + 2, dotSize, dotSize);
                    g2.fillOval(x, yStart + (dotSize + 2) * 2, dotSize, dotSize);
                    g2.dispose();
                }
            };
            optionsBtn.setPreferredSize(new Dimension(28, 28));
            optionsBtn.setBorderPainted(false);
            optionsBtn.setContentAreaFilled(false);
            optionsBtn.setFocusPainted(false);
            optionsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            optionsBtn.setBorder(new EmptyBorder(0, 0, 0, 4));
            optionsBtn.setVisible(false);

            // 🔥 CRITICAL FIX: Look up the ID dynamically when clicked!
            // Do NOT use 'messageId' here, use wrapper.getClientProperty("msgId")
            optionsBtn.addActionListener(e -> {
                long currentId = (long) wrapper.getClientProperty("msgId");
                showDeleteOptions(optionsBtn, currentId);
            });

            MouseAdapter hoverListener = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    optionsBtn.setVisible(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), bubbleRow);
                    if (!bubbleRow.contains(p) && !optionsBtn.getModel().isPressed()) {
                        optionsBtn.setVisible(false);
                    }
                }
            };

            bubble.addMouseListener(hoverListener);
            bubbleRow.addMouseListener(hoverListener);
            optionsBtn.setModel(new DefaultButtonModel() {
                @Override
                public boolean isRollover() {
                    return super.isRollover();
                }
            });
            optionsBtn.addMouseListener(hoverListener);

            bubbleRow.add(optionsBtn);
            bubbleRow.add(bubble);
            contentBox.add(bubbleRow);
        } else {
            contentBox.add(bubble);
        }

        JLabel timeLbl = new JLabel(time);
        timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLbl.setForeground(new Color(160, 160, 160));
        timeLbl.setBorder(new EmptyBorder(2, 2, 0, 2));

        if (isMe) {
            timeLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
            contentBox.add(timeLbl);
            wrapper.add(contentBox, BorderLayout.EAST);
        } else {
            timeLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentBox.add(timeLbl);

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
            avatarWrapper.add(avatarLabel, BorderLayout.SOUTH);
            avatarWrapper.setBorder(new EmptyBorder(0, 0, 14, 0));
            incomingContainer.add(avatarWrapper, BorderLayout.WEST);
            incomingContainer.add(contentBox, BorderLayout.CENTER);
            wrapper.add(incomingContainer, BorderLayout.WEST);
        }

        messagesPanel.add(wrapper);
        messagesPanel.add(Box.createVerticalStrut(2));

        // Return the wrapper so we can modify it later!
        return wrapper;
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

    private void showDeleteOptions(Component invoker, long messageId) {
        JPopupMenu popup = new JPopupMenu();
        // Clean white background and subtle border
        popup.setBackground(Color.WHITE);
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        JMenuItem unsendItem = createModernMenuItem("Unsend for everyone", true);
        unsendItem.addActionListener(e -> handleDeleteMessage(messageId, true));

        JMenuItem removeForItem = createModernMenuItem("Remove for you", false);
        removeForItem.addActionListener(e -> handleDeleteMessage(messageId, false));

        popup.add(unsendItem);
        // Add a subtle separator line
        JPopupMenu.Separator sep = new JPopupMenu.Separator();
        sep.setForeground(new Color(240, 240, 240));
        popup.add(sep);
        popup.add(removeForItem);

        // Adjust X position to account for the new button size and padding
        popup.show(invoker, invoker.getWidth() - popup.getPreferredSize().width + 10, invoker.getHeight() + 5);
    }

    // Helper to create clean menu items
    private JMenuItem createModernMenuItem(String text, boolean isDestructive) {
        JMenuItem item = new JMenuItem(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. Logic for Background Color (Hover vs Normal)
                if (isArmed() || isSelected()) {
                    g2.setColor(new Color(245, 245, 245)); // Light gray hover
                } else {
                    g2.setColor(Color.WHITE); // White normal
                }

                // 2. Paint the Background
                g2.fillRect(0, 0, getWidth(), getHeight());

                // 3. Let Swing paint the Text & Icons on top
                // We must dispose the graphics copy before calling super or we might get weird
                // artifacts
                g2.dispose();
                super.paintComponent(g);
            }
        };

        // Standard styling properties
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBackground(Color.WHITE);
        item.setOpaque(false); // We handle painting manually

        // Set Text Color
        // Destructive = Red, Normal = Dark Gray
        item.setForeground(isDestructive ? new Color(220, 50, 50) : new Color(30, 30, 30));

        // Padding
        item.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return item;
    }

    private void handleDeleteMessage(long messageId, boolean forEveryone) {
        if (this.currentChatId == -1)
            return;

        long chatId = this.currentChatId;
        String token = UserSession.getUser().getToken();
        boolean isGroup = "GROUP".equalsIgnoreCase(currentChatType);

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    // 1. Construct URL based on Chat Type
                    // Endpoint format: .../{chatId}/...-messages/{msgId}?isAll={bool}
                    String baseUrl;
                    if (isGroup) {
                        baseUrl = ApiUrl.GROUP_CONVERSATION + "/" + chatId + "/group-conversation-messages/"
                                + messageId;
                    } else {
                        baseUrl = ApiUrl.PRIVATE_CONVERSATION + "/" + chatId + "/private-conversation-messages/"
                                + messageId;
                    }

                    String fullUrl = baseUrl + "?isAll=" + forEveryone;

                    // 2. Build Request
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(fullUrl))
                            .header("Authorization", "Bearer " + token)
                            .DELETE()
                            .build();

                    // 3. Send Request
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        return true;
                    } else {
                        System.err.println("❌ Delete failed: " + response.statusCode() + " | " + response.body());
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        // Success: Remove it from the UI immediately
                        removeMessageFromUI(messageId);
                    } else {
                        JOptionPane.showMessageDialog(ChatPanel.this,
                                "Failed to delete message. Please try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void removeMessageFromUI(long messageId) {
        for (Component comp : messagesPanel.getComponents()) {
            if (comp instanceof JPanel) {
                Object idObj = ((JPanel) comp).getClientProperty("msgId");
                if (idObj != null && (long) idObj == messageId) {
                    messagesPanel.remove(comp);

                    // Also remove the strut (spacer) after it, if it exists
                    int index = messagesPanel.getComponentZOrder(comp);
                    if (index + 1 < messagesPanel.getComponentCount()) {
                        // Check if next component is a vertical strut box
                        Component next = messagesPanel.getComponent(index + 1);
                        if (next instanceof javax.swing.Box.Filler) {
                            messagesPanel.remove(next);
                        }
                    }

                    messagesPanel.revalidate();
                    messagesPanel.repaint();
                    break;
                }
            }
        }
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

                // System.out.println();

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

            long msgId = msg.optLong("groupConversationMessageId", -1);
            if (msgId == -1)
                msgId = msg.optLong("privateConversationMessageId", -1);

            boolean isMe = (senderId == myId);
            String displayTime = formatTime(rawTime);

            if (!isMe && (senderAvatarUrl == null || senderAvatarUrl.isEmpty() || "null".equals(senderAvatarUrl))) {
                senderAvatarUrl = this.currentPartnerAvatarUrl;
            }

            String senderDisplayName = msg.optString("senderName");
            if (senderDisplayName == null || senderDisplayName.isEmpty()) {
                String fname = msg.optString("firstName", "");
                String lname = msg.optString("lastName", "");
                if (!fname.isEmpty() || !lname.isEmpty()) {
                    senderDisplayName = (fname + " " + lname).trim();
                } else {
                    // Fallback for private chat: If not me, use the partner's name passed in
                    // headers
                    senderDisplayName = isMe ? "You" : "User";
                }
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
                isProgrammaticScroll = true;

                JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
                JScrollBar vertical = scrollPane.getVerticalScrollBar();

                vertical.setValue(vertical.getMaximum());

                SwingUtilities.invokeLater(() -> {
                    isProgrammaticScroll = false;
                });
            }
        });
    }

    private boolean isUserAtBottom(JScrollBar sb) {
        return sb.getValue() + sb.getVisibleAmount() >= sb.getMaximum() - 5;
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
        private final int MAX_WIDTH = 350;

        public MessageBubble(String message, boolean isMe) {
            this.isMe = isMe;
            setLayout(new BorderLayout());
            setOpaque(false); // Important for custom painting

            textArea = new JTextArea(message);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setOpaque(false);
            textArea.setEditable(false);
            textArea.setFocusable(false);
            // Slightly deeper gray for better readability
            textArea.setForeground(isMe ? Color.WHITE : new Color(50, 50, 50));
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setMargin(new Insets(0, 0, 0, 0));

            // --- Calculate Sizes (Same logic as before) ---
            FontMetrics fm = new JLabel().getFontMetrics(textArea.getFont());
            int textWidth = fm.stringWidth(message);
            int textHeight = fm.getHeight();
            int hPadding = 24;
            int vPadding = 18; // Slightly more vertical breathing room

            int bubbleWidth, bubbleHeight;

            if (textWidth < (MAX_WIDTH - hPadding)) {
                bubbleWidth = textWidth + hPadding + 4;
                bubbleHeight = textHeight + vPadding;
            } else {
                bubbleWidth = MAX_WIDTH;
                textArea.setSize(new Dimension(bubbleWidth - hPadding, Short.MAX_VALUE));
                bubbleHeight = textArea.getPreferredSize().height + vPadding;
            }

            setPreferredSize(new Dimension(bubbleWidth, bubbleHeight));
            setMaximumSize(new Dimension(bubbleWidth, bubbleHeight));
            // Slightly more padding on edges
            setBorder(new EmptyBorder(9, 12, 9, 12));
            add(textArea, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 18;

            if (isMe) {
                // --- Modern Gradient for Sent Messages ---
                // Creates a subtle diagonal gradient from PRIMARY_COLOR to a slightly lighter
                // shade
                Color color1 = PRIMARY_COLOR;
                Color color2 = new Color(
                        Math.min(255, PRIMARY_COLOR.getRed() + 30),
                        Math.min(255, PRIMARY_COLOR.getGreen() + 30),
                        Math.min(255, PRIMARY_COLOR.getBlue() + 30));
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            } else {
                // Standard style for received messages
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                // A cleaner, subtler border color
                g2.setColor(new Color(230, 230, 230));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}