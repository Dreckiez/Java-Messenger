package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SearchFriend extends JPanel {
    private JTextField searchField;
    private JPanel contactsPanel;
    private JPanel actionPanel;
    private List<Contact> allContacts;
    private Contact selectedContact;
    private NavPanel navPanel; // Retained for back button functionality

    // --- Theme Colors (Adapted to Dark/Grey Theme for Consistency with Contact
    // Items) ---
    private static final Color BG_DARK = new Color(36, 37, 38);
    private static final Color BG_LIGHT_GREY = new Color(58, 59, 60); // Hover/Search field background
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_GREY = new Color(170, 170, 170); // Placeholder, Mutual friends
    private static final Color PRIMARY_BLUE = new Color(59, 130, 246);
    private static final Color SELECTION_BG = new Color(50, 60, 80); // Darker selection background
    private static final Color BORDER_COLOR = new Color(50, 50, 50);

    public SearchFriend(NavPanel parent) {
        navPanel = parent;
        allContacts = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(450, 600));

        // Search bar section (Now acting as the Header)
        JPanel searchSection = createSearchSection();
        add(searchSection, BorderLayout.NORTH);

        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Contacts list
        contactsPanel = new JPanel();
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        contactsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(contactsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainContent.add(scrollPane, BorderLayout.CENTER);

        // Action panel (hidden by default)
        actionPanel = createActionPanel();
        actionPanel.setVisible(false);
        mainContent.add(actionPanel, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);

        // Load demo contacts
        loadDemoContacts();
    }

    private JPanel createSearchSection() {
        JPanel searchContainer = new JPanel(new BorderLayout(10, 0));
        searchContainer.setBackground(Color.WHITE);
        searchContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        // Back button (Stays the same)
        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        backBtn.setForeground(Color.BLACK);
        backBtn.setBackground(Color.WHITE);
        backBtn.setFocusable(false);
        backBtn.setBorderPainted(false);
        backBtn.setPreferredSize(new Dimension(40, 40));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backBtn.setBackground(new Color(45, 46, 47));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backBtn.setBackground(Color.WHITE);
            }
        });

        backBtn.addActionListener(e -> {
            if (navPanel != null) {
                navPanel.showChat();
            }
        });

        // Search field (Themed dark)
        searchField = new JTextField("Tìm kiếm trên Messenger");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setForeground(TEXT_GREY);
        searchField.setBackground(BG_LIGHT_GREY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BG_LIGHT_GREY, 1, true),
                BorderFactory.createEmptyBorder(10, 35, 10, 15)));
        searchField.setCaretColor(TEXT_WHITE);

        // Add search icon overlay
        JPanel searchFieldContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw search icon
                g2d.setColor(TEXT_GREY);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                g2d.drawString("🔍", 12, 26);

                g2d.dispose();
            }
        };
        searchFieldContainer.setLayout(new BorderLayout());
        searchFieldContainer.setBackground(BG_LIGHT_GREY);
        searchFieldContainer.setBorder(BorderFactory.createLineBorder(BG_LIGHT_GREY, 0, true));
        searchFieldContainer.add(searchField, BorderLayout.CENTER);

        // Search field focus listeners
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Tìm kiếm trên Messenger")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Tìm kiếm trên Messenger");
                    searchField.setForeground(TEXT_GREY);
                }
            }
        });

        // Add key listener for search
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterContacts(searchField.getText());
            }
        });

        searchContainer.add(backBtn, BorderLayout.WEST);
        searchContainer.add(searchFieldContainer, BorderLayout.CENTER);

        return searchContainer;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BG_DARK);

        // Send Request button
        JButton sendRequestBtn = new JButton("Send Request");
        sendRequestBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendRequestBtn.setForeground(TEXT_WHITE);
        sendRequestBtn.setBackground(PRIMARY_BLUE);
        sendRequestBtn.setFocusable(false);
        sendRequestBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        sendRequestBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendRequestBtn.setOpaque(true);
        sendRequestBtn.setBorderPainted(false);

        sendRequestBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sendRequestBtn.setBackground(new Color(40, 110, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sendRequestBtn.setBackground(PRIMARY_BLUE);
            }
        });

        sendRequestBtn.addActionListener(e -> handleSendRequest());

        buttonPanel.add(sendRequestBtn);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private void loadDemoContacts() {
        String[] names = {
                "Nguyễn Khang", "Diễm Xuân Hinh", "Nguyễn Văn Minh",
                "Đan Anh", "Thanh Phong", "Trần Phùng Đình",
                "Uyển Thùy", "Nguyễn Xuân", "Anh Minh Phan",
                "Thanh Tú Võ", "Chi Phan", "Minh Tùng", "Khôn Chỉ"
        };

        for (String name : names) {
            Contact contact = new Contact(name);
            allContacts.add(contact);
            addContactItem(contact);
        }
    }

    private void addContactItem(Contact contact) {
        JPanel itemPanel = new JPanel(new BorderLayout(12, 0));
        itemPanel.setBackground(BG_DARK);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Avatar (Retained original gradient style)
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Avatar circle with gradient
                Color color1 = PRIMARY_BLUE;
                Color color2 = new Color(147, 51, 234);
                GradientPaint gradient = new GradientPaint(0, 0, color1, 40, 40, color2);
                g2d.setPaint(gradient);
                g2d.fillOval(0, 0, 40, 40);

                // Initial
                g2d.setColor(TEXT_WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                String initial = contact.name.substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (40 - fm.stringWidth(initial)) / 2;
                int y = ((40 - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(initial, x, y);

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(40, 40);
            }
        };
        avatarPanel.setOpaque(false);

        // Name label (Themed white)
        JLabel nameLabel = new JLabel(contact.name);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        nameLabel.setForeground(TEXT_WHITE);

        itemPanel.add(avatarPanel, BorderLayout.WEST);
        itemPanel.add(nameLabel, BorderLayout.CENTER);

        // Store reference to panel in contact
        contact.panel = itemPanel;

        // Hover and click effects
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedContact != contact) {
                    itemPanel.setBackground(BG_LIGHT_GREY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedContact != contact) {
                    itemPanel.setBackground(BG_DARK);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                selectContact(contact);
            }
        });

        contactsPanel.add(itemPanel);
        contactsPanel.revalidate();
        contactsPanel.repaint();
    }

    private void selectContact(Contact contact) {
        // Deselect previous
        if (selectedContact != null && selectedContact.panel != null) {
            selectedContact.panel.setBackground(BG_DARK);
            // Reset border to default empty padding
            selectedContact.panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }

        // Select new
        selectedContact = contact;
        if (selectedContact.panel != null) {
            selectedContact.panel.setBackground(SELECTION_BG);
            // Apply blue selection bar border, matching FriendRequests style
            selectedContact.panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, PRIMARY_BLUE),
                    BorderFactory.createEmptyBorder(10, 16, 10, 20))); // Adjusted left padding for border
        }

        // Show action panel
        actionPanel.setVisible(true);
        revalidate();
        repaint();
    }

    private void handleSendRequest() {
        if (selectedContact != null) {
            // Logic to send a friend request: remove from list, display confirmation
            allContacts.remove(selectedContact);
            if (selectedContact.panel != null) {
                contactsPanel.remove(selectedContact.panel);
            }

            JOptionPane.showMessageDialog(this,
                    "Sent friend request to " + selectedContact.name,
                    "Friend Request Sent",
                    JOptionPane.INFORMATION_MESSAGE);

            selectedContact = null;
            actionPanel.setVisible(false);
            contactsPanel.revalidate();
            contactsPanel.repaint();
        }
    }

    private void filterContacts(String query) {
        contactsPanel.removeAll();
        String lowercaseQuery = query.toLowerCase();

        if (query.equals("Tìm kiếm trên Messenger") || query.trim().isEmpty()) {
            for (Contact contact : allContacts) {
                addContactItem(contact);
            }
        } else {
            for (Contact contact : allContacts) {
                if (contact.name.toLowerCase().contains(lowercaseQuery)) {
                    addContactItem(contact);
                }
            }
        }

        // Clear selection if filtered out
        if (selectedContact != null) {
            boolean found = false;
            for (int i = 0; i < contactsPanel.getComponentCount(); i++) {
                if (contactsPanel.getComponent(i) == selectedContact.panel) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                selectedContact = null;
                actionPanel.setVisible(false);
            }
        }

        contactsPanel.revalidate();
        contactsPanel.repaint();
    }

    // Inner class for contact data (Updated to include panel reference)
    private static class Contact {
        String name;
        JPanel panel; // Added for selection logic

        Contact(String name) {
            this.name = name;
        }
    }
}