package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class NotificationDot extends JComponent {
    public NotificationDot() {
        setPreferredSize(new Dimension(10, 10));
        setOpaque(false);
        setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(239, 68, 68)); // red
        g.fillOval(0, 0, 8, 8);
    }
}
