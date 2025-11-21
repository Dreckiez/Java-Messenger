package components;

import javax.swing.*;

import models.Contact;

import java.awt.*;

public class ContactItem extends BaseItem {

    private boolean isFriend;

    public ContactItem(String username, String avatarUrl, boolean isFriend) {
        super(username, avatarUrl);
        this.isFriend = isFriend;
    }

    public ContactItem(Contact c) {
        super(c.getName(), c.getAvatarUrl());
        this.isFriend = c.isFriend();
    }

    @Override
    protected JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel name = new JLabel(username);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel status = new JLabel(isFriend ? "Friend" : "Not friends yet");
        status.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        status.setForeground(new Color(130, 130, 130));
        status.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        panel.add(name, BorderLayout.NORTH);
        panel.add(status, BorderLayout.SOUTH);
        return panel;
    }

    @Override
    protected JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // CENTER alignment, 10px top/bottom
                                                                             // padding
        panel.setOpaque(false); // Transparent background

        JButton addBtn = new JButton("Send Friend Request");
        addBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(new Color(0, 122, 255)); // iOS-style blue
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setPreferredSize(new Dimension(250, 36));

        // Add hover effect
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addBtn.setBackground(new Color(0, 105, 217));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                addBtn.setBackground(new Color(0, 122, 255));
            }
        });

        panel.add(addBtn);
        return panel;
    }
}
