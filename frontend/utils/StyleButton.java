package utils;

import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.*;

public class StyleButton {
    public void styleButton(JButton button) {
        // button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(50, 50));
        button.setMaximumSize(new Dimension(50, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw hover background
                if (button.getModel().isRollover()) {
                    g2d.setColor(new Color(200, 200, 200, 100));
                    g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
                }

                g2d.dispose();
                super.paint(g, c);
            }
        });
    }
}
