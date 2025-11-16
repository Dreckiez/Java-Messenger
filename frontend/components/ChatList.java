package components;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import javax.swing.*;

import utils.ImageEditor;

public class ChatList extends JPanel {
    private JPanel chatListPanel;
    private ChatItem activeItem = null;
    private Consumer<String> onChatSelected;

    public ChatList(Consumer<String> onChatSelected) {
        this.onChatSelected = onChatSelected;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0));
        setBackground(Color.WHITE);

        // Search field
        JTextField searchField = new JTextField("Search...");
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        searchField.setBackground(new Color(245, 245, 245));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(new Color(150, 150, 150));

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search...");
                    searchField.setForeground(new Color(150, 150, 150));
                }
            }
        });

        add(searchField, BorderLayout.NORTH);

        // Chat list panel
        chatListPanel = new JPanel();
        chatListPanel.setLayout(new BoxLayout(chatListPanel, BoxLayout.Y_AXIS));
        chatListPanel.setBackground(Color.WHITE);

        // Add sample chats (you can replace this with your data)
        addSampleChats();

        JScrollPane scrollPane = new JScrollPane(chatListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void addSampleChats() {
        // Sample data - replace with your actual data
        String[][] chats = {
                { "Alice", "Hey! How are you doing?", "2m", "2", "true" },
                { "Bob", "Did you see the new update?", "15m", "0", "true" },
                { "Charlie", "Thanks for your help!", "1h", "1", "false" }
        };

        for (String[] chatData : chats) {
            addChat(chatData[0], chatData[1], chatData[2],
                    Integer.parseInt(chatData[3]), Boolean.parseBoolean(chatData[4]));
        }
    }

    public void addChat(String name, String lastMessage, String timestamp,
            int unreadCount, boolean isOnline) {
        // Create avatar (you can replace with actual user avatars)
        // ImageIcon avatar = createAvatarPlaceholder(name);
        ImageIcon avatar = new ImageIcon(getClass().getClassLoader().getResource("assets/wolf-howling.jpg"));

        ImageEditor editor = new ImageEditor();

        ChatItem item = new ChatItem(editor.makeCircularImage(avatar.getImage(), 50), name, lastMessage, timestamp,
                unreadCount, isOnline);

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                selectChat(item);
            }
        });

        chatListPanel.add(item);
        chatListPanel.revalidate();
        chatListPanel.repaint();
    }

    private void selectChat(ChatItem item) {
        if (activeItem != null) {
            activeItem.setActive(false);
        }
        item.setActive(true);
        activeItem = item;

        // Notify listener
        if (onChatSelected != null) {
            onChatSelected.accept(item.getChatName());
        }
    }

    public void selectFirstChat() {
        if (chatListPanel.getComponentCount() > 0) {
            Component firstComponent = chatListPanel.getComponent(0);
            if (firstComponent instanceof ChatItem) {
                selectChat((ChatItem) firstComponent);
            }
        }
    }

    public void clearChats() {
        chatListPanel.removeAll();
        activeItem = null;
        chatListPanel.revalidate();
        chatListPanel.repaint();
    }

    // Helper method to create avatar placeholder
    // private ImageIcon createAvatarPlaceholder(String name) {
    // Color[] colors = {
    // new Color(239, 68, 68), // Red
    // new Color(59, 130, 246), // Blue
    // new Color(34, 197, 94), // Green
    // new Color(251, 146, 60), // Orange
    // new Color(168, 85, 247), // Purple
    // };

    // int colorIndex = Math.abs(name.hashCode()) % colors.length;

    // java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(50, 50,
    // java.awt.image.BufferedImage.TYPE_INT_ARGB);
    // Graphics2D g2d = img.createGraphics();
    // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    // RenderingHints.VALUE_ANTIALIAS_ON);

    // // Draw circle background
    // g2d.setColor(colors[colorIndex]);
    // g2d.fillOval(0, 0, 50, 50);

    // // Draw initial letter
    // g2d.setColor(Color.WHITE);
    // g2d.setFont(new Font("Segoe UI", Font.BOLD, 22));
    // String initial = name.substring(0, 1).toUpperCase();
    // FontMetrics fm = g2d.getFontMetrics();
    // int x = (50 - fm.stringWidth(initial)) / 2;
    // int y = ((50 - fm.getHeight()) / 2) + fm.getAscent();
    // g2d.drawString(initial, x, y);

    // g2d.dispose();
    // return new ImageIcon(img);
    // }
}
