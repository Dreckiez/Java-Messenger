package components;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageBubble extends JPanel {
    private boolean isSentByMe;
    private String message;
    private String timestamp;

    public MessageBubble(String message, boolean isSentByMe) {
        this.message = message;
        this.isSentByMe = isSentByMe;
        this.timestamp = new SimpleDateFormat("HH:mm").format(new Date());

        setLayout(new BorderLayout());
        setOpaque(false);

        // Create bubble panel
        JPanel bubblePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Bubble color
                if (isSentByMe) {
                    g2d.setColor(new Color(59, 130, 246)); // Blue for sent messages
                } else {
                    g2d.setColor(new Color(229, 231, 235)); // Gray for received messages
                }

                // Draw rounded rectangle bubble
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2d.dispose();
            }
        };

        bubblePanel.setLayout(new BorderLayout());
        bubblePanel.setOpaque(false);
        bubblePanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        // Message text
        JLabel messageLabel = new JLabel("<html><body style='width: 200px'>" + message + "</body></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(isSentByMe ? Color.WHITE : Color.BLACK);

        // Timestamp
        JLabel timeLabel = new JLabel(timestamp);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(isSentByMe ? new Color(255, 255, 255, 180) : new Color(0, 0, 0, 120));
        timeLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(messageLabel, BorderLayout.CENTER);
        textPanel.add(timeLabel, BorderLayout.SOUTH);

        bubblePanel.add(textPanel, BorderLayout.CENTER);

        // Wrapper to align bubble
        JPanel wrapper = new JPanel(new FlowLayout(isSentByMe ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false);
        wrapper.add(bubblePanel);

        add(wrapper, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
    }
}
