package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatPanel extends JPanel {
    private JPanel messagesPanel;
    private JTextField inputField;
    private JButton sendButton;
    private String currentUser;
    private JLabel headerLabel;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        // User info in header
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userInfoPanel.setOpaque(false);

        // Avatar placeholder
        JLabel avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(40, 40));
        avatarLabel.setIcon(createDefaultAvatar());

        headerLabel = new JLabel("Select a chat");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        userInfoPanel.add(avatarLabel);
        userInfoPanel.add(headerLabel);

        headerPanel.add(userInfoPanel, BorderLayout.WEST);

        // Messages area
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(Color.WHITE);
        messagesPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JScrollPane scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        inputField.setBackground(new Color(245, 245, 245));

        // Placeholder text
        inputField.setText("Type a message...");
        inputField.setForeground(new Color(150, 150, 150));

        inputField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (inputField.getText().equals("Type a message...")) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (inputField.getText().isEmpty()) {
                    inputField.setText("Type a message...");
                    inputField.setForeground(new Color(150, 150, 150));
                }
            }
        });

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(59, 130, 246));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setPreferredSize(new Dimension(80, 40));

        // Hover effect for send button
        sendButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                sendButton.setBackground(new Color(37, 99, 235));
            }

            public void mouseExited(MouseEvent e) {
                sendButton.setBackground(new Color(59, 130, 246));
            }
        });

        // Send message action
        ActionListener sendAction = e -> sendMessage();
        inputField.addActionListener(sendAction);
        sendButton.addActionListener(sendAction);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Add some demo messages
        addDemoMessages();
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty() && !msg.equals("Type a message...")) {
            addMessage(msg, true);
            inputField.setText("");
            inputField.setForeground(Color.BLACK);

            // Simulate received message after 1 second (for demo)
            Timer timer = new Timer(1000, e -> {
                addMessage("Thanks for your message! 😊", false);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void addMessage(String message, boolean isSentByMe) {
        MessageBubble bubble = new MessageBubble(message, isSentByMe);
        messagesPanel.add(bubble);
        messagesPanel.revalidate();

        // Auto-scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    public void setChatUser(String user) {
        currentUser = user;
        headerLabel.setText(user);
        messagesPanel.removeAll();
        messagesPanel.revalidate();
        messagesPanel.repaint();

        // Add welcome message
        addMessage("Chat started with " + user, false);
    }

    private void addDemoMessages() {
        // Demo conversation
        addMessage("Hey! How are you?", false);
        addMessage("I'm doing great! Thanks for asking 😊", true);
        addMessage("That's awesome! Want to grab lunch later?", false);
    }

    private ImageIcon createDefaultAvatar() {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(40, 40,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(156, 163, 175));
        g2d.fillOval(0, 0, 40, 40);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2d.drawString("?", 14, 27);

        g2d.dispose();
        return new ImageIcon(img);
    }
}
