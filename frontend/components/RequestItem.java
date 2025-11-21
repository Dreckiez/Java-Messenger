package components;

import javax.swing.*;
import java.awt.*;

public class RequestItem extends BaseItem {

    public RequestItem(String username, String avatarUrl) {
        super(username, avatarUrl);
    }

    @Override
    protected JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel name = new JLabel(username);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel info = new JLabel("sent you a friend request");
        info.setForeground(new Color(130, 130, 130));

        panel.add(name, BorderLayout.NORTH);
        panel.add(info, BorderLayout.SOUTH);

        return panel;
    }

    @Override
    protected JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(245, 247, 250));

        panel.add(new JButton("Accept"));
        panel.add(new JButton("Deny"));

        return panel;
    }
}
