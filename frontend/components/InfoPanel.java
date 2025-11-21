package components;

import javax.swing.*;

import utils.ImageEditor;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

public class InfoPanel extends JPanel {
    private String currentChatName;
    private boolean isGroupChat;
    private JLabel chatNameLabel;
    private JLabel chatStatusLabel;
    private JPanel membersPanel;
    private JPanel searchResultsPanel;
    private JTextField searchField;
    private List<String> members;

    public InfoPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(320, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(220, 220, 220)));

        members = new ArrayList<>();

        // Main scrollable content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Header section with avatar and name
        JPanel headerSection = createHeaderSection();
        contentPanel.add(headerSection);

        // Search section
        JPanel searchSection = createSearchSection();
        contentPanel.add(searchSection);

        // Search results (hidden by default)
        searchResultsPanel = new JPanel();
        searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.Y_AXIS));
        searchResultsPanel.setBackground(Color.WHITE);
        searchResultsPanel.setVisible(false);

        searchResultsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchResultsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        contentPanel.add(searchResultsPanel);

        // Members section
        JPanel membersSection = createMembersSection();
        contentPanel.add(membersSection);

        // Privacy section
        JPanel privacySection = createPrivacySection();
        contentPanel.add(privacySection);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderSection() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridBagLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Avatar
        ImageIcon avatar = new ImageIcon(getClass().getClassLoader().getResource("assets/wolf-howling.jpg"));

        ImageEditor editor = new ImageEditor();
        JLabel avatarLabel = new JLabel(editor.makeCircularImage(avatar.getImage(), 80));
        headerPanel.add(avatarLabel, gbc);

        // Chat name
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 5, 0);
        chatNameLabel = new JLabel("Chat Info");
        chatNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(chatNameLabel, gbc);

        // Status
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        chatStatusLabel = new JLabel("Active now");
        chatStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chatStatusLabel.setForeground(new Color(100, 100, 100));
        headerPanel.add(chatStatusLabel, gbc);

        return headerPanel;
    }

    private JPanel createSearchSection() {
        JPanel searchSection = new JPanel();

        searchSection.setLayout(new BoxLayout(searchSection, BoxLayout.Y_AXIS));
        searchSection.setBackground(Color.WHITE);
        searchSection.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        searchSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Approximate height

        JLabel searchLabel = new JLabel("Search in Conversation");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        searchSection.add(searchLabel);
        searchSection.add(searchField);

        return searchSection;
    }

    private void performSearch(String query) {
        searchResultsPanel.removeAll();
        searchResultsPanel.setVisible(true);

        // Demo search results
        String[] demoResults = {
                "Hey! How are you?",
                "That sounds great!",
                "See you tomorrow"
        };

        for (String result : demoResults) {
            if (result.toLowerCase().contains(query.toLowerCase())) {
                JPanel resultItem = createSearchResultItem(result);
                searchResultsPanel.add(resultItem);
            }
        }

        if (searchResultsPanel.getComponentCount() == 0) {
            JLabel noResults = new JLabel("No messages found");
            noResults.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noResults.setForeground(new Color(150, 150, 150));
            noResults.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            searchResultsPanel.add(noResults);
        }

        searchResultsPanel.revalidate();
        searchResultsPanel.repaint();
    }

    private JPanel createSearchResultItem(String text) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        item.add(label, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(245, 245, 245));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(Color.WHITE);
            }

            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(InfoPanel.this,
                        "Jump to message: " + text);
            }
        });

        return item;
    }

    private JPanel createMembersSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel titleLabel = new JLabel("Members");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
        membersPanel.setBackground(Color.WHITE);

        membersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(titleLabel);
        section.add(membersPanel);

        // Add demo members
        addMember("You", true);
        addMember("Alice", true);
        addMember("Bob", false);

        return section;
    }

    private JPanel createPrivacySection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel titleLabel = new JLabel("Privacy & Support");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton muteBtn = createOptionButton("🔕", "Mute Notifications");
        JButton blockBtn = createOptionButton("🚫", "Block User");
        JButton reportBtn = createOptionButton("⚠️", "Report");

        section.add(titleLabel);
        section.add(muteBtn);
        section.add(blockBtn);
        section.add(reportBtn);

        return section;
    }

    private JButton createOptionButton(String icon, String text) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(10, 0));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        button.add(iconLabel, BorderLayout.WEST);
        button.add(textLabel, BorderLayout.CENTER);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(245, 245, 245));
                button.setOpaque(true);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setOpaque(false);
            }
        });

        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, text + " clicked!");
        });

        return button;
    }

    public void addMember(String name, boolean isOnline) {
        JPanel memberItem = new JPanel(new BorderLayout(10, 0));
        memberItem.setBackground(Color.WHITE);
        memberItem.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        memberItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        memberItem.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Avatar with online indicator
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Avatar circle
                g2d.setColor(new Color(59, 130, 246));
                g2d.fillOval(2, 2, 36, 36);

                // Initial
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                String initial = name.substring(0, 1);
                FontMetrics fm = g2d.getFontMetrics();
                int x = 2 + (36 - fm.stringWidth(initial)) / 2;
                int y = 2 + ((36 - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(initial, x, y);

                // Online indicator
                if (isOnline) {
                    g2d.setColor(new Color(34, 197, 94));
                    g2d.fillOval(28, 28, 12, 12);
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(28, 28, 12, 12);
                }

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(45, 45);
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(45, 45);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(45, 45);
            }
        };
        // avatarPanel.setPreferredSize(new Dimension(40, 40));
        avatarPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        memberItem.add(avatarPanel, BorderLayout.WEST);
        memberItem.add(nameLabel, BorderLayout.CENTER);

        members.add(name);
        membersPanel.add(memberItem);
        membersPanel.revalidate();
        membersPanel.repaint();
    }

    public void setChatInfo(String chatName, boolean isGroup) {
        this.currentChatName = chatName;
        this.isGroupChat = isGroup;

        chatNameLabel.setText(chatName);
        chatStatusLabel.setText(isGroup ? members.size() + " members" : "Active now");

        // Show/hide members section based on group chat
        membersPanel.getParent().setVisible(isGroup);
    }

}
