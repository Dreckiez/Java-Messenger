package components;

import javax.swing.*;

import models.Friend;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class FriendItem extends BaseItem {
    private Friend friend;
    private boolean isOnline;
    private ActionListener onRequestHandled;

    public FriendItem(Friend f) {
        super(f.getName(), f.getAvatarUrl());
        this.friend = f;
        this.isOnline = (f.getOnline() != null) ? f.getOnline() : false;

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

    public Friend getFriend() {
        return friend;
    }

    public void setOnRequestHandled(ActionListener callback) {
        this.onRequestHandled = callback;
    }

    @Override
    protected JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 3));
        panel.setOpaque(false);

        // Name with online status indicator
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        namePanel.setOpaque(false);

        JLabel name = new JLabel(username);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel statusDot = createStatusDot(isOnline);
        namePanel.add(statusDot);

        namePanel.add(name);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(namePanel); // Changed from 'name' to 'namePanel'

        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }

    // Helper method to create online status dot (optional)
    private JLabel createStatusDot(boolean isOnline) {
        JLabel dot = new JLabel("●");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        dot.setForeground(isOnline ? new Color(67, 181, 129) : new Color(150, 150, 150));
        return dot;
    }

    @Override
    protected JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Chat button
        JButton chatBtn = new JButton("Chat");
        chatBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatBtn.setForeground(Color.WHITE);
        chatBtn.setBackground(new Color(0, 122, 255));
        chatBtn.setFocusPainted(false);
        chatBtn.setBorderPainted(false);
        chatBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chatBtn.setPreferredSize(new Dimension(250, 36));
        chatBtn.setMaximumSize(new Dimension(250, 36));
        chatBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        chatBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                chatBtn.setBackground(new Color(0, 105, 217));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                chatBtn.setBackground(new Color(0, 122, 255));
            }
        });

        // Create Group button
        JButton createGroupBtn = new JButton("Create Group");
        createGroupBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        createGroupBtn.setForeground(Color.WHITE);
        createGroupBtn.setBackground(new Color(0, 122, 255));
        createGroupBtn.setFocusPainted(false);
        createGroupBtn.setBorderPainted(false);
        createGroupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createGroupBtn.setPreferredSize(new Dimension(250, 36));
        createGroupBtn.setMaximumSize(new Dimension(250, 36));
        createGroupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        createGroupBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createGroupBtn.setBackground(new Color(0, 105, 217));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                createGroupBtn.setBackground(new Color(0, 122, 255));
            }
        });

        // Unfriend button
        JButton unFriendBtn = new JButton("Unfriend");
        unFriendBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        unFriendBtn.setForeground(Color.WHITE);
        unFriendBtn.setBackground(new Color(220, 53, 69));
        unFriendBtn.setFocusPainted(false);
        unFriendBtn.setBorderPainted(false);
        unFriendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        unFriendBtn.setPreferredSize(new Dimension(250, 36));
        unFriendBtn.setMaximumSize(new Dimension(250, 36));
        unFriendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        unFriendBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                unFriendBtn.setBackground(new Color(200, 35, 51));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                unFriendBtn.setBackground(new Color(220, 53, 69));
            }
        });

        // Block button
        JButton blockBtn = new JButton("Block");
        blockBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        blockBtn.setForeground(Color.WHITE);
        blockBtn.setBackground(new Color(220, 53, 69));
        blockBtn.setFocusPainted(false);
        blockBtn.setBorderPainted(false);
        blockBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        blockBtn.setPreferredSize(new Dimension(250, 36));
        blockBtn.setMaximumSize(new Dimension(250, 36));
        blockBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        blockBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                blockBtn.setBackground(new Color(200, 35, 51));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                blockBtn.setBackground(new Color(220, 53, 69));
            }
        });

        // Block & Unfriend button
        JButton blockUFBtn = new JButton("Block & Unfriend");
        blockUFBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        blockUFBtn.setForeground(Color.WHITE);
        blockUFBtn.setBackground(new Color(220, 53, 69));
        blockUFBtn.setFocusPainted(false);
        blockUFBtn.setBorderPainted(false);
        blockUFBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        blockUFBtn.setPreferredSize(new Dimension(250, 36));
        blockUFBtn.setMaximumSize(new Dimension(250, 36));
        blockUFBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        blockUFBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                blockUFBtn.setBackground(new Color(200, 35, 51));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                blockUFBtn.setBackground(new Color(220, 53, 69));
            }
        });

        // Add buttons with spacing
        panel.add(chatBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(createGroupBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(unFriendBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(blockBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(blockUFBtn);

        return panel;
    }

    // private void handleChat() {

    // }
}
