package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.json.JSONObject; // 🔥 Added Import

public class ChatItem extends JPanel {
    private boolean isHovered = false;
    private boolean isActive = false;
    private Color normalBg = Color.WHITE;
    private Color hoverBg = new Color(240, 240, 240);
    private Color activeBg = new Color(230, 240, 255);

    private String chatName;
    private JSONObject chatData; // 🔥 Added to store API data
    private ImageIcon avatarIcon; // 🔥 Added to allow updates

    // UI Components promoted to fields for updates
    private JPanel avatarPanel;
    private JLabel nameLabel;
    private JLabel messageLabel;
    private JLabel timeLabel;

    public ChatItem(ImageIcon avatar, String name, String lastMessage,
            String timestamp, int unreadCount, boolean isOnline, JSONObject data) { // 🔥 Added data param

        this.chatName = name;
        this.chatData = data;
        this.avatarIcon = avatar;

        setLayout(new BorderLayout(10, 0));
        setBackground(normalBg);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 72)); // 72 matches your list height
        setPreferredSize(new Dimension(300, 72));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 1. Avatar Area
        avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw avatar from field
                if (avatarIcon != null) {
                    g2d.drawImage(avatarIcon.getImage(), 0, 0, 50, 50, null);
                }

                // Draw online indicator
                if (isOnline) {
                    g2d.setColor(new Color(34, 197, 94)); // Green
                    g2d.fillOval(36, 36, 14, 14);
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(36, 36, 14, 14);
                }
                g2d.dispose();
            }
        };
        avatarPanel.setPreferredSize(new Dimension(50, 50));
        avatarPanel.setOpaque(false);

        // 2. Center Panel (Name & Message)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 5));
        centerPanel.setOpaque(false);

        // Top row: Name and timestamp
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(unreadCount > 0 ? Color.BLACK : new Color(50, 50, 50));

        timeLabel = new JLabel(timestamp);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(128, 128, 128));

        topRow.add(nameLabel, BorderLayout.WEST);
        topRow.add(timeLabel, BorderLayout.EAST);

        // Bottom row: Last message and unread badge
        JPanel bottomRow = new JPanel(new BorderLayout(10, 0));
        bottomRow.setOpaque(false);

        // Truncate message initially
        messageLabel = new JLabel(truncateMessage(lastMessage));
        messageLabel.setFont(new Font("Segoe UI", unreadCount > 0 ? Font.BOLD : Font.PLAIN, 13));
        messageLabel.setForeground(unreadCount > 0 ? Color.BLACK : new Color(100, 100, 100));

        bottomRow.add(messageLabel, BorderLayout.CENTER);

        // Unread badge
        if (unreadCount > 0) {
            JLabel badgeLabel = new JLabel(unreadCount > 9 ? "9+" : String.valueOf(unreadCount)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(59, 130, 246)); // Blue
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                    g2d.dispose();
                }
            };
            badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            badgeLabel.setForeground(Color.WHITE);
            badgeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            badgeLabel.setPreferredSize(new Dimension(22, 22));
            bottomRow.add(badgeLabel, BorderLayout.EAST);
        }

        centerPanel.add(topRow, BorderLayout.NORTH);
        centerPanel.add(bottomRow, BorderLayout.CENTER);

        add(avatarPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // Mouse Listeners
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActive) {
                    isHovered = true;
                    setBackground(hoverBg);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                if (!isActive)
                    setBackground(normalBg);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setActive(true);
            }
        });
    }

    // --- GETTERS & SETTERS (Required by ChatList) ---

    public JSONObject getChatData() {
        return chatData;
    }

    public String getChatName() {
        return chatName;
    }

    public void setAvatar(ImageIcon newAvatar) {
        this.avatarIcon = newAvatar;
        this.avatarPanel.repaint(); // Repaint just the avatar area
    }

    public void setChatName(String newName) {
        this.chatName = newName;
        if (this.chatData != null) {
            this.chatData.put("name", newName);
        }
        this.nameLabel.setText(newName);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
        if (active) {
            setBackground(activeBg);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(59, 130, 246)),
                    BorderFactory.createEmptyBorder(10, 11, 10, 15)));
        } else {
            setBackground(normalBg);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        }
        repaint();
    }

    // --- SOCKET UPDATE METHODS ---

    public void updatePreview(String message, String time) {
        this.messageLabel.setText(truncateMessage(message));
        this.timeLabel.setText(time);

        // Highlight text to show activity
        this.messageLabel.setForeground(Color.BLACK);
        this.messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        revalidate();
        repaint();
    }

    private String truncateMessage(String msg) {
        if (msg == null)
            return "";
        int maxLength = 25;
        if (msg.length() > maxLength) {
            return msg.substring(0, maxLength) + "...";
        }
        return msg;
    }
}