package components;

import javax.swing.*;
import models.Friend;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FriendItem extends BaseItem {
    private Friend friend;
    private boolean isOnline;
    private ActionListener onRequestHandled;

    // --- MÀU SẮC ---
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color ONLINE_COLOR = new Color(34, 197, 94);  // Xanh lá
    private final Color OFFLINE_COLOR = new Color(148, 163, 184); // Xám
    
    private final Color BTN_BLUE = new Color(59, 130, 246);
    private final Color BTN_RED = new Color(239, 68, 68);
    private final Color BTN_RED_HOVER = new Color(220, 38, 38);
    private final Color BTN_BLUE_HOVER = new Color(37, 99, 235);

    public FriendItem(Friend f) {
        super(f.getName(), f.getAvatarUrl());
        this.friend = f;
        this.isOnline = (f.getOnline() != null) ? f.getOnline() : false;
        
        initUI();
    }

    private void initUI() {
        // --- CENTER PANEL ---
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        centerWrapper.add(createCenterPanel(), BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        // --- ACTION PANEL ---
        actionPanel = createActionPanel();
        if (actionPanel != null) {
            JPanel actionWrapper = new JPanel(new BorderLayout());
            actionWrapper.setOpaque(false);
            actionWrapper.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0)); 

            JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
            separator.setForeground(new Color(226, 232, 240)); 
            actionWrapper.add(separator, BorderLayout.NORTH);

            actionWrapper.add(actionPanel, BorderLayout.CENTER);
            add(actionWrapper, BorderLayout.SOUTH);
            
            actionWrapper.setVisible(false);
            this.actionPanel = actionWrapper; 
        }

        // --- EVENT ---
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setSelected(true);
            }
        });

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    }

    public Friend getFriend() { return friend; }

    public void setOnRequestHandled(ActionListener callback) {
        this.onRequestHandled = callback;
    }

    @Override
    protected JPanel createCenterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(TEXT_PRIMARY);

        JLabel statusDot = new JLabel("●");
        statusDot.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusDot.setForeground(isOnline ? ONLINE_COLOR : OFFLINE_COLOR);
        statusDot.setToolTipText(isOnline ? "Online" : "Offline");

        panel.add(nameLabel);
        panel.add(statusDot);

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(panel, BorderLayout.CENTER);
        
        return container;
    }

    @Override
    protected JPanel createActionPanel() {
        // GridLayout 2 cột, khoảng cách 10px
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

        // 1. Chat
        JButton chatBtn = createModernButton("Chat", BTN_BLUE, BTN_BLUE_HOVER);
        
        // 2. Group (Đã đổi tên từ Create Group)
        JButton groupBtn = createModernButton("Group", BTN_BLUE, BTN_BLUE_HOVER);

        // 3. Unfriend
        JButton unfriendBtn = createModernButton("Unfriend", BTN_RED, BTN_RED_HOVER);

        // 4. Block
        JButton blockBtn = createModernButton("Block", BTN_RED, BTN_RED_HOVER);

        panel.add(chatBtn);
        panel.add(groupBtn);
        panel.add(unfriendBtn);
        panel.add(blockBtn);

        return panel;
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 34)); 

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }
}