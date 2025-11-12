package components;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    public InfoPanel() {
        setPreferredSize(new Dimension(200, 0));
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel label = new JLabel("Chat Info");
        add(label, BorderLayout.NORTH);
    }
}
