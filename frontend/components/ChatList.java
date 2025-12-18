package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.json.JSONArray;
import org.json.JSONObject;

import models.User;
import services.UserListener;
import services.ChatListener;
import utils.ChatSessionManager;
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

public class ChatList extends JPanel implements UserListener, ChatListener {
    private JPanel chatListPanel;
    private ChatItem activeItem = null; // References the external ChatItem
    private Consumer<JSONObject> onChatSelected;
    private UserServices userService;
    private ImageEditor imageEditor;

    private long pendingOpenId = -1;
    private String pendingOpenType = null;

    private final Color ACTIVE_BG = new Color(235, 242, 255);
    private final Color HOVER_BG = new Color(248, 249, 250);
    private final Color TEXT_PRIMARY = new Color(31, 41, 55);
    private final Color TEXT_SECONDARY = new Color(107, 114, 128);

    public ChatList(Consumer<JSONObject> onChatSelected) {
        this.onChatSelected = onChatSelected;
        this.userService = new UserServices();
        this.imageEditor = new ImageEditor();

        UserSession.addListener(this);
        ChatSessionManager.addListener(this);

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
        setupPlaceholder(searchField, "Search conversation");

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterChats(searchField.getText().trim());
            }
        });

        searchBar.add(searchField, BorderLayout.CENTER);
        searchContainer.add(searchBar, BorderLayout.NORTH);
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

    public long getChatId(JSONObject chat) {
        if (chat == null)
            return -1;
        return chat.optLong("id", -1);
    }

    // --- SOCKET EVENT HANDLERS ---

    public void updateConversationOnMessage(long conversationId, String message, String time) {
        SwingUtilities.invokeLater(() -> {
            ChatItem foundItem = null;

            for (Component comp : chatListPanel.getComponents()) {
                if (comp instanceof ChatItem) {
                    ChatItem item = (ChatItem) comp;
                    long itemId = getChatId(item.getChatData());
                    if (itemId == conversationId) {
                        foundItem = item;
                        break;
                    }
                }
            }

            if (foundItem != null) {
                // Update text and move to top
                foundItem.updatePreview(message, time);
                chatListPanel.remove(foundItem);
                chatListPanel.add(foundItem, 0);
                chatListPanel.revalidate();
                chatListPanel.repaint();
            } else {
                loadConversations(); // Reload if new chat
            }
        });
    }

    @Override
    public void onGroupNameChanged(long conversationId, String newName) {
        SwingUtilities.invokeLater(() -> {
            for (Component comp : chatListPanel.getComponents()) {
                if (comp instanceof ChatItem) {
                    ChatItem item = (ChatItem) comp;
                    long itemId = item.getChatData().optLong("id", -1);

                    if (itemId == conversationId) {
                        item.setChatName(newName);

                        // Update InfoPanel if active
                        if (item == activeItem && onChatSelected != null) {
                            JSONObject updatedData = item.getChatData();
                            updatedData.put("name", newName);
                            onChatSelected.accept(updatedData);
                        }

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
                chatListPanel.remove(itemToRemove);
                if (itemToRemove == activeItem) {
                    activeItem = null;
                    if (onChatSelected != null)
                        onChatSelected.accept(new JSONObject());
                }

                if (chatListPanel.getComponentCount() == 0)
                    showEmptyState();
                else {
                    chatListPanel.revalidate();
                    chatListPanel.repaint();
                }
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
                        JSONObject updatedData = item.getChatData();
                        updatedData.put("avatarUrl", newAvatarUrl);

                        ImageLoader.loadImageAsync(newAvatarUrl, img -> {
                            if (img != null) {
                                item.setAvatar(imageEditor.makeCircularImage(img, 48));
                            }
                        });

                        if (item == activeItem && onChatSelected != null) {
                            onChatSelected.accept(updatedData);
                        }
                        break;
                    }
                }
            }
        });
    }

    // --- LOADING LOGIC ---

    public void loadConversations(int targetId, String targetType) {
        this.pendingOpenId = targetId;
        this.pendingOpenType = targetType;
        loadConversations(); // Calls the main unified method
    }

    public void loadConversations() {
        if (UserSession.getUser() == null)
            return;

        long targetId = -1;

        if (this.pendingOpenId != -1) {
            targetId = this.pendingOpenId;
        } else if (activeItem != null) {
            targetId = getChatId(activeItem.getChatData());
        }

        final long idToSelect = targetId; // Final variable for the inner class

        SwingUtilities.invokeLater(() -> {
        });

        String token = UserSession.getUser().getToken();

        new SwingWorker<JSONArray, Void>() {
            private JSONArray conversations = null;

            @Override
            protected JSONArray doInBackground() throws Exception {
                return userService.getConversations(token);
            }

            @Override
            protected void done() {
                try {
                    conversations = get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SwingUtilities.invokeLater(() -> {
                    if (conversations == null || conversations.length() == 0) {
                        showEmptyState();
                        pendingOpenId = -1; // Reset pending since we failed
                        if (onChatSelected != null)
                            onChatSelected.accept(new JSONObject());
                        return;
                    }

                    chatListPanel.removeAll();
                    ChatItem itemToSelect = null;
                    boolean foundTarget = false;

                    for (int i = 0; i < conversations.length(); i++) {
                        JSONObject chat = conversations.getJSONObject(i);
                        ChatItem item = addChat(chat);
                        long currentId = getChatId(chat);

                        if (idToSelect != -1 && currentId == idToSelect) {
                            itemToSelect = item;
                            foundTarget = true;
                        }

                        if (itemToSelect == null && i == 0) {
                            itemToSelect = item;
                        }
                    }

                    if (itemToSelect != null) {
                        selectChat(itemToSelect);

                        if (foundTarget || activeItem == null) {
                            if (onChatSelected != null) {
                                onChatSelected.accept(itemToSelect.getChatData());
                            }
                        }

                        Rectangle bounds = itemToSelect.getBounds();
                        chatListPanel.scrollRectToVisible(bounds);
                    }

                    pendingOpenId = -1; // Request handled
                    chatListPanel.revalidate();
                    chatListPanel.repaint();
                });
            }
        }.execute();
    }

    // public void loadConversations() {
    // if (UserSession.getUser() == null)
    // return;
    // SwingUtilities.invokeLater(() -> {
    // chatListPanel.removeAll();
    // chatListPanel.revalidate();
    // chatListPanel.repaint();
    // });
    // String token = UserSession.getUser().getToken();
    // new SwingWorker<JSONArray, Void>() {
    // JSONArray conversations = null;

    // @Override
    // protected JSONArray doInBackground() throws Exception {
    // return userService.getConversations(token);
    // }

    // @Override
    // protected void done() {
    // try {
    // conversations = get();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // System.out.println(conversations.toString());
    // SwingUtilities.invokeLater(() -> {
    // if (conversations == null || conversations.length() == 0) {
    // showEmptyState();
    // } else {
    // ChatItem firstItem = null;
    // chatListPanel.removeAll();
    // for (int i = 0; i < conversations.length(); i++) {
    // JSONObject chat = conversations.getJSONObject(i);
    // ChatItem item = addChat(chat);
    // if (i == 0)
    // firstItem = item;
    // }
    // if (firstItem != null) {
    // selectChat(firstItem);
    // if (onChatSelected != null)
    // onChatSelected.accept(firstItem.getChatData());
    // }
    // }
    // chatListPanel.revalidate();
    // chatListPanel.repaint();
    // });
    // }
    // }.execute();
    // }

    // private void loadConversations_V2() {
    // if (UserSession.getUser() == null)
    // return;
    // SwingUtilities.invokeLater(() -> {
    // chatListPanel.removeAll();
    // chatListPanel.revalidate();
    // chatListPanel.repaint();
    // });
    // String token = UserSession.getUser().getToken();
    // new SwingWorker<JSONArray, Void>() {
    // JSONArray conversations = null;

    // @Override
    // protected JSONArray doInBackground() throws Exception {
    // return userService.getConversations(token);
    // }

    // @Override
    // protected void done() {
    // try {
    // conversations = get();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // SwingUtilities.invokeLater(() -> {
    // if (conversations == null || conversations.length() == 0) {
    // showEmptyState();
    // pendingOpenId = -1;
    // pendingOpenType = null;
    // if (onChatSelected != null)
    // onChatSelected.accept(new JSONObject());
    // } else {
    // ChatItem itemToSelect = null;
    // chatListPanel.removeAll();
    // for (int i = 0; i < conversations.length(); i++) {
    // JSONObject chat = conversations.getJSONObject(i);
    // ChatItem item = addChat(chat);

    // long currentId = getChatId(chat);
    // String currentType = chat.optString("conversationType", "PRIVATE");
    // if (pendingOpenId != -1 && currentId == pendingOpenId) {
    // if (pendingOpenType != null && pendingOpenType.equalsIgnoreCase(currentType))
    // {
    // itemToSelect = item;
    // }
    // }
    // if (i == 0 && itemToSelect == null && pendingOpenId == -1) {
    // itemToSelect = item;
    // }
    // }
    // if (itemToSelect != null) {
    // selectChat(itemToSelect);
    // if (onChatSelected != null)
    // onChatSelected.accept(itemToSelect.getChatData());
    // Rectangle bounds = itemToSelect.getBounds();
    // chatListPanel.scrollRectToVisible(bounds);
    // } else if (pendingOpenId != -1) {
    // if (onChatSelected != null)
    // onChatSelected.accept(new JSONObject());
    // }
    // pendingOpenId = -1;
    // pendingOpenType = null;
    // }
    // chatListPanel.revalidate();
    // chatListPanel.repaint();
    // });
    // }
    // }.execute();
    // }

    private ChatItem addChat(JSONObject chatData) {
        String name = chatData.optString("name", "Unknown");
        String message = chatData.optString("previewContent", "");
        if (message.equals("null"))
            message = "";
        if (message.isEmpty())
            message = "Start a conversation";

        String time = formatTime(chatData.optString("previewTime"));
        String avatarUrl = chatData.optString("avatarUrl", null);
        if (avatarUrl != null && (avatarUrl.equals("null") || avatarUrl.isEmpty())) {
            avatarUrl = null;
        }

        boolean isGroup = "GROUP".equals(chatData.optString("conversationType"));
        ImageIcon placeholder = createAvatar(name, isGroup);

        // 🔥 Using the External ChatItem with Data Param
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
                if (item != activeItem)
                    item.setBackground(HOVER_BG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (item != activeItem)
                    item.setBackground(Color.WHITE);
            }
        });

        chatListPanel.add(item);
        return item;
    }

    private void showEmptyState() {
        chatListPanel.removeAll();
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBackground(Color.WHITE);
        emptyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

    private void filterChats(String query) {
        if (chatListPanel.getComponentCount() > 0 && !(chatListPanel.getComponent(0) instanceof ChatItem))
            return;
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

    private String formatTime(String isoTime) {
        if (isoTime == null || isoTime.equals("null") || isoTime.isEmpty())
            return "";
        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoTime);
            LocalDateTime now = LocalDateTime.now();
            long days = ChronoUnit.DAYS.between(dateTime, now);
            if (days == 0)
                return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            else if (days < 7)
                return dateTime.format(DateTimeFormatter.ofPattern("EEE"));
            else
                return dateTime.format(DateTimeFormatter.ofPattern("dd/MM"));
        } catch (Exception e) {
            return "";
        }
    }

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
        g2d.drawString(initial, (size - fm.stringWidth(initial)) / 2, ((size - fm.getHeight()) / 2) + fm.getAscent());
        g2d.dispose();
        return new ImageIcon(img);
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