package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.json.JSONArray;
import org.json.JSONObject;

import models.User;
import services.UserListener;
import services.ChatListener; // 🔥 Import ChatListener
import utils.ChatSessionManager; // 🔥 Import ChatSessionManager
import services.UserServices;
import utils.ImageEditor;
import utils.ImageLoader;
import utils.UserSession;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

// 🔥🔥🔥 THÊM CHATLISTENER VÀO INTERFACE
public class ChatList extends JPanel implements UserListener, ChatListener {
    private JPanel chatListPanel;
    private ChatItem activeItem = null;
    private Consumer<JSONObject> onChatSelected;
    private UserServices userService;
    private ImageEditor imageEditor;

    // Colors
    private final Color ACTIVE_BG = new Color(235, 242, 255);
    private final Color HOVER_BG = new Color(248, 249, 250);
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private final Color TEXT_PRIMARY = new Color(31, 41, 55);
    private final Color TEXT_SECONDARY = new Color(107, 114, 128);

    public ChatList(Consumer<JSONObject> onChatSelected) {
        this.onChatSelected = onChatSelected;
        this.userService = new UserServices();
        this.imageEditor = new ImageEditor();

        UserSession.addListener(this);
        ChatSessionManager.addListener(this); // 🔥 ĐĂNG KÝ LẮNG NGHE SỰ KIỆN CHAT

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(320, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        // --- 1. SEARCH AREA ---
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setBackground(Color.WHITE);
        searchContainer.setBorder(new EmptyBorder(20, 15, 10, 15));

        RoundedPanel searchBar = new RoundedPanel(20, new Color(243, 244, 246));
        searchBar.setLayout(new BorderLayout());
        searchBar.setBorder(new EmptyBorder(8, 15, 8, 15));

        JTextField searchField = new JTextField("Search...");
        searchField.setBorder(null);
        searchField.setBackground(new Color(243, 244, 246));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setupPlaceholder(searchField, "Search message global...");

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterChats(searchField.getText().trim());
            }
        });

        searchBar.add(searchField, BorderLayout.CENTER);
        searchContainer.add(searchBar, BorderLayout.NORTH); // Lỗi logic: cần BorderLayout.CENTER
        // Sử dụng BorderLayout.CENTER (Nếu không có lỗi xảy ra, giữ nguyên)
        // searchContainer.add(searchBar, BorderLayout.CENTER); 

        add(searchContainer, BorderLayout.NORTH);

        // --- 2. CHAT LIST ---
        chatListPanel = new JPanel();
        chatListPanel.setLayout(new BoxLayout(chatListPanel, BoxLayout.Y_AXIS));
        chatListPanel.setBackground(Color.WHITE);
        chatListPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(chatListPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        styleScrollBar(scrollPane);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void onUserUpdated(User user) {
        if (user != null) {
            loadConversations();
        }
    }
    
    // 🔥🔥 TRIỂN KHAI PHƯƠNG THỨC XỬ LÝ SỰ KIỆN TỪ CHATLISTENER
    @Override
    public void onGroupNameChanged(long conversationId, String newName) {
        // Chạy trên Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            for (Component comp : chatListPanel.getComponents()) {
                if (comp instanceof ChatItem) {
                    ChatItem item = (ChatItem) comp;
                    long itemId = item.getChatData().optLong("id", -1);
                    
                    if (itemId == conversationId) {
                        // Cập nhật tên cục bộ trên ChatList Item
                        item.setChatName(newName);
                        
                        // Nếu item này đang được chọn, đảm bảo InfoPanel cũng cập nhật
                        if (item == activeItem && onChatSelected != null) {
                            // Tạo JSON Data mới để cập nhật tên
                            JSONObject updatedData = item.getChatData();
                            updatedData.put("name", newName);
                            
                            // Gửi lại dữ liệu (chỉ riêng tên) cho InfoPanel
                            onChatSelected.accept(updatedData);
                        }
                        
                        // Chỉ cập nhật UI cục bộ cho item này
                        item.revalidate();
                        item.repaint();
                        break;
                    }
                }
            }
        });
    }
    
    @Override
    public void onConversationDeleted(long conversationId) {
        // 🔥 Logic xử lý xóa chat (tương tự)
        SwingUtilities.invokeLater(() -> {
            ChatItem itemToRemove = null;
            for (Component comp : chatListPanel.getComponents()) {
                if (comp instanceof ChatItem) {
                    ChatItem item = (ChatItem) comp;
                    if (item.getChatData().optLong("id", -1) == conversationId) {
                        itemToRemove = item;
                        break;
                    }
                }
            }
            
            if (itemToRemove != null) {
                // Xóa item khỏi panel
                chatListPanel.remove(itemToRemove);
                
                // Nếu item bị xóa đang là item active, reset UI
                if (itemToRemove == activeItem) {
                    activeItem = null;
                    // Gọi callback với null hoặc empty object để reset ChatPanel
                    if (onChatSelected != null) {
                        onChatSelected.accept(new JSONObject()); // hoặc onChatSelected.accept(null);
                    }
                }
                
                // Kiểm tra xem danh sách có trống không
                if (chatListPanel.getComponentCount() == 0) {
                    showEmptyState();
                } else {
                    chatListPanel.revalidate();
                    chatListPanel.repaint();
                }
            }
        });
    }
    // 🔥🔥 KẾT THÚC CHATLISTENER IMPLEMENTATION

    public void loadConversations() {
        if (UserSession.getUser() == null) return;

        // 🔥 BƯỚC 1: XÓA UI TRÊN LUỒNG SỰ KIỆN (EDT) NGAY LẬP TỨC
        SwingUtilities.invokeLater(() -> {
            chatListPanel.removeAll();
            chatListPanel.revalidate();
            chatListPanel.repaint();
            System.out.println("DEBUG: ChatList UI cleaned up.");
        });

        String token = UserSession.getUser().getToken();

        new SwingWorker<JSONArray, Void>() {
            JSONArray conversations = null;

            @Override
            protected JSONArray doInBackground() throws Exception {
                // Code gọi API
                return userService.getConversations(token);
            }

            @Override
            protected void done() {
                try {
                    conversations = get(); // Lấy kết quả từ doInBackground
                } catch (Exception e) {
                    System.err.println("Error fetching conversations: " + e.getMessage());
                }

                // 🔥 BƯỚC 2: HIỂN THỊ KẾT QUẢ CŨNG TRÊN LUỒNG SỰ KIỆN (EDT)
                SwingUtilities.invokeLater(() -> {
                    
                    if (conversations == null || conversations.length() == 0) {
                        showEmptyState();
                    } else {
                        ChatItem firstItem = null;
                        
                        chatListPanel.removeAll(); 
                        
                        for (int i = 0; i < conversations.length(); i++) {
                            JSONObject chat = conversations.getJSONObject(i);
                            ChatItem item = addChat(chat);
                            if (i == 0) firstItem = item;
                        }
                        
                        // Auto-select first item
                        if (firstItem != null) {
                            selectChat(firstItem);
                            if (onChatSelected != null) {
                                onChatSelected.accept(firstItem.getChatData());
                            }
                        }
                    }
                    
                    chatListPanel.revalidate(); 
                    chatListPanel.repaint();
                    System.out.println("DEBUG: ChatList loaded " + (conversations != null ? conversations.length() : 0) + " items.");
                });
            }
        }.execute();
    }

    // 🔥 METHOD MỚI: HIỂN THỊ TRẠNG THÁI RỖNG
    private void showEmptyState() {
        chatListPanel.removeAll();
        
        // ... (Logic tạo Empty Panel giữ nguyên) ...
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBackground(Color.WHITE);
        emptyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Icon (Dùng Text Emoji phóng to cho nhanh, hoặc vẽ hình)
        JLabel iconLabel = new JLabel("👋");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("No conversations yet");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("Search for a friend to start chatting!");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLabel.setForeground(TEXT_SECONDARY);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Căn giữa theo chiều dọc
        chatListPanel.add(Box.createVerticalGlue());
        chatListPanel.add(iconLabel);
        chatListPanel.add(Box.createVerticalStrut(10));
        chatListPanel.add(titleLabel);
        chatListPanel.add(Box.createVerticalStrut(5));
        chatListPanel.add(subLabel);
        chatListPanel.add(Box.createVerticalGlue());
        
        chatListPanel.revalidate();
        chatListPanel.repaint();
    }

    private ChatItem addChat(JSONObject chatData) {
        String name = chatData.optString("name", "Unknown");
        String message = chatData.optString("previewContent", "");
        if (message.equals("null")) message = "";
        if (message.isEmpty()) message = "Start a conversation";

        String time = formatTime(chatData.optString("previewTime"));
        String avatarUrl = chatData.optString("avatarUrl", null);
        if (avatarUrl != null && (avatarUrl.equals("null") || avatarUrl.isEmpty())) {
            avatarUrl = null;
        }

        boolean isGroup = "GROUP".equals(chatData.optString("conversationType"));
        
        ImageIcon placeholder = createAvatar(name, isGroup);
        
        ChatItem item = new ChatItem(placeholder, name, message, time, 0, false, chatData); 

        if (avatarUrl != null) {
            ImageLoader.loadImageAsync(avatarUrl, new ImageLoader.ImageLoadCallback() {
                @Override
                public void onLoaded(Image img) {
                    if (img != null) {
                        item.setAvatar(imageEditor.makeCircularImage(img, 48));
                    }
                }
            });
        }

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectChat(item);
                if (onChatSelected != null) {
                    onChatSelected.accept(item.getChatData());
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (item != activeItem) item.setBackground(HOVER_BG);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (item != activeItem) item.setBackground(Color.WHITE);
            }
        });

        chatListPanel.add(item);
        return item;
    }

    private void filterChats(String query) {
        // Nếu đang ở trạng thái Empty State thì không filter
        if (chatListPanel.getComponentCount() > 0 && !(chatListPanel.getComponent(0) instanceof ChatItem)) {
            return;
        }

        for (Component comp : chatListPanel.getComponents()) {
            if (comp instanceof ChatItem) {
                ChatItem item = (ChatItem) comp;
                boolean match = item.getChatName().toLowerCase().contains(query.toLowerCase());
                item.setVisible(match);
            }
        }
        chatListPanel.revalidate();
    }

    private void selectChat(ChatItem item) {
        if (activeItem != null) {
            activeItem.setActive(false);
        }
        item.setActive(true);
        activeItem = item;
    }

    // --- HELPER: Time Formatter ---
    private String formatTime(String isoTime) {
        if (isoTime == null || isoTime.equals("null") || isoTime.isEmpty()) return "";
        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoTime); 
            LocalDateTime now = LocalDateTime.now();
            
            long days = ChronoUnit.DAYS.between(dateTime, now);
            if (days == 0) {
                return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            } else if (days < 7) {
                return dateTime.format(DateTimeFormatter.ofPattern("EEE"));
            } else {
                return dateTime.format(DateTimeFormatter.ofPattern("dd/MM"));
            }
        } catch (Exception e) {
            return "";
        }
    }

    // --- INNER CLASS: CHAT ITEM ---
    class ChatItem extends JPanel {
        private String chatName;
        private JSONObject chatData;
        private boolean isActive = false;
        private ImageIcon avatarIcon;
        private JPanel avatarPanel; 
        
        // 🔥 LƯU TRỮ NAME LABEL để cập nhật tên nhóm
        private JLabel nameLabel; 

        public ChatItem(ImageIcon avatar, String name, String message, String time, int unread, boolean online, JSONObject data) {
            this.chatName = name;
            this.chatData = data;
            this.avatarIcon = avatar;

            setLayout(new BorderLayout(10, 0));
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(10, 15, 10, 15));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // 1. Avatar Area
            avatarPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (avatarIcon != null) {
                        g.drawImage(avatarIcon.getImage(), 0, 0, 48, 48, null);
                    }
                }
            };
            avatarPanel.setPreferredSize(new Dimension(48, 48));
            avatarPanel.setOpaque(false);

            // 2. Text Info
            JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 2));
            centerPanel.setOpaque(false);
            
            // 🔥 GÁN NAME LABEL
            nameLabel = new JLabel(name); 
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            nameLabel.setForeground(TEXT_PRIMARY);
            
            JLabel messageLabel = new JLabel(message);
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            messageLabel.setForeground(TEXT_SECONDARY);
            
            centerPanel.add(nameLabel);
            centerPanel.add(messageLabel);

            // 3. Meta Data
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            
            JLabel timeLabel = new JLabel(time);
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            timeLabel.setForeground(new Color(156, 163, 175));
            timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            
            rightPanel.add(timeLabel, BorderLayout.NORTH);

            add(avatarPanel, BorderLayout.WEST);
            add(centerPanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);
        }

        public void setAvatar(ImageIcon newAvatar) {
            this.avatarIcon = newAvatar;
            this.avatarPanel.repaint();
        }

        public void setActive(boolean active) {
            this.isActive = active;
            setBackground(active ? ACTIVE_BG : Color.WHITE);
        }
        
        // 🔥 PHƯƠNG THỨC MỚI: Cập nhật tên nhóm
        public void setChatName(String newName) {
            this.chatName = newName;
            this.chatData.put("name", newName); // Cập nhật dữ liệu JSON
            this.nameLabel.setText(newName); // Cập nhật Label
        }

        public String getChatName() { return chatName; }
        public JSONObject getChatData() { return chatData; }
    }

    // --- UTILS ---
    private ImageIcon createAvatar(String name, boolean isGroup) {
        int size = 50;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg = isGroup ? new Color(245, 158, 11) : new Color(59, 130, 246); 
        g2d.setColor(bg);
        g2d.fillOval(0, 0, size, size);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
        String initial = (name != null && !name.isEmpty()) ? name.substring(0, 1).toUpperCase() : "?";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth(initial)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(initial, x, y);
        g2d.dispose();

        return new ImageIcon(img);
    }

    private void setupPlaceholder(JTextField field, String text) {
        field.setText(text);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void styleScrollBar(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(220, 220, 220);
                this.trackColor = Color.WHITE;
            }
            @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                return jbutton;
            }
        });
    }

    @Override
    public void onGroupAvatarChanged(long conversationId, String newAvatarUrl) {
        SwingUtilities.invokeLater(() -> {
            for (Component comp : chatListPanel.getComponents()) {
                if (comp instanceof ChatItem) {
                    ChatItem item = (ChatItem) comp;
                    long itemId = item.getChatData().optLong("id", -1);
                    
                    if (itemId == conversationId) {
                        // 1. Cập nhật dữ liệu JSON
                        JSONObject updatedData = item.getChatData();
                        updatedData.put("avatarUrl", newAvatarUrl);
                        
                        // 2. Load ảnh mới và cập nhật UI Item
                        ImageLoader.loadImageAsync(newAvatarUrl, img -> {
                            if (img != null) {
                                item.setAvatar(imageEditor.makeCircularImage(img, 48));
                                item.repaint();
                            }
                        });
                        
                        // 3. Nếu đang active, cập nhật lại dữ liệu cho consumer
                        if (item == activeItem && onChatSelected != null) {
                            onChatSelected.accept(updatedData);
                        }
                        
                        break;
                    }
                }
            }
        });
    }

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
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }
}