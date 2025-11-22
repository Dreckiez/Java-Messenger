package components;

import javax.swing.*;

import java.awt.*;

public class FriendItem extends BaseItem {

    private boolean online;

    public FriendItem(String username, String avatarUrl, boolean online) {
        super(username, avatarUrl);
        this.online = online;
    }

    @Override
    protected JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel name = new JLabel(username);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel status = new JLabel(online ? "Online" : "Offline");
        status.setForeground(online ? new Color(40, 160, 60) : new Color(130, 130, 130));

        panel.add(name, BorderLayout.NORTH);
        panel.add(status, BorderLayout.SOUTH);

        return panel;
    }

    @Override
    protected JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(245, 247, 250));

        panel.add(new JButton("Chat"));
        panel.add(new JButton("Create Group"));
        panel.add(new JButton("Unfriend"));
        panel.add(new JButton("Block"));

        return panel;
    }
}
