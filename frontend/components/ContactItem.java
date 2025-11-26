package components;

import javax.swing.*;

import models.Contact;

import java.awt.*;
import java.awt.event.MouseAdapter;

public class ContactItem extends BaseItem {

    private String isFriend = "none";

    public ContactItem(String username, String avatarUrl, String isFriend) {
        super(username, avatarUrl);
        this.isFriend = (isFriend != null) ? isFriend : "none";

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // 15px left margin
        centerWrapper.add(createCenterPanel(), BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        actionPanel = createActionPanel();
        if (actionPanel != null) {
            JPanel actionWrapper = new JPanel(new BorderLayout());
            actionWrapper.setOpaque(false);
            actionWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 10px top margin

            // Add horizontal separator line
            JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
            separator.setForeground(new Color(120, 120, 120));
            actionWrapper.add(separator, BorderLayout.NORTH);

            actionWrapper.add(actionPanel, BorderLayout.CENTER);
            add(actionWrapper, BorderLayout.SOUTH);
            actionWrapper.setVisible(false);
            this.actionPanel = actionWrapper; // Store wrapper instead
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                setSelected(true);
            }
        });

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    }

    public ContactItem(Contact c) {
        super(c.getName(), c.getAvatarUrl());
        this.isFriend = (c.isFriend() != null) ? c.isFriend() : "none";

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // 15px left margin
        centerWrapper.add(createCenterPanel(), BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        actionPanel = createActionPanel();
        if (actionPanel != null) {
            JPanel actionWrapper = new JPanel(new BorderLayout());
            actionWrapper.setOpaque(false);
            actionWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 10px top margin

            // Add horizontal separator line
            JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
            separator.setForeground(new Color(120, 120, 120));
            actionWrapper.add(separator, BorderLayout.NORTH);

            actionWrapper.add(actionPanel, BorderLayout.CENTER);
            add(actionWrapper, BorderLayout.SOUTH);
            actionWrapper.setVisible(false);
            this.actionPanel = actionWrapper; // Store wrapper instead
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                setSelected(true);
            }
        });

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    }

    @Override
    protected JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel name = new JLabel(username);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel status = new JLabel((isFriend != null && isFriend.equals("friend")) ? "Friend" : "Not friends yet");
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

        Boolean friend = (isFriend != null && isFriend.equals("friend")) ? true : false;
        String text = friend ? "Unfriend" : "Send Friend Request";

        JButton addBtn = new JButton(text);
        addBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(friend ? new Color(220, 53, 69) : new Color(0, 122, 255)); // iOS-style blue
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setPreferredSize(new Dimension(250, 36));

        // Add hover effect
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addBtn.setBackground(friend ? new Color(200, 35, 51) : new Color(0, 105, 217));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                addBtn.setBackground(friend ? new Color(220, 53, 69) : new Color(0, 122, 255));
            }
        });

        panel.add(addBtn);
        return panel;
    }
}
