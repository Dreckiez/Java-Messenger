package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FriendRequests extends JPanel {
    private JTextField searchField;
    private JPanel requestsPanel;
    private JPanel actionPanel;
    private List<FriendRequest> allRequests;
    private FriendRequest selectedRequest;

    public FriendRequests() {
        allRequests = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header section
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Requests list
        requestsPanel = new JPanel();
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
        requestsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(requestsPanel);
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

        // Load demo data
        loadDemoRequests();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        // Title
        JLabel titleLabel = new JLabel("Friend Requests");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(5, 5, 5));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Search field
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setForeground(new Color(150, 150, 150));
        searchField.setBackground(new Color(240, 242, 245));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(240, 242, 245), 1, true),
                BorderFactory.createEmptyBorder(10, 35, 10, 15)));
        searchField.setCaretColor(new Color(50, 50, 50));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Search icon overlay panel
        JPanel searchContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(150, 150, 150));
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                g2d.drawString("🔍", 12, 28);

                g2d.dispose();
            }
        };
        searchContainer.setLayout(new BorderLayout());
        searchContainer.setBackground(new Color(240, 242, 245));
        searchContainer.setBorder(BorderFactory.createLineBorder(new Color(240, 242, 245), 0, true));
        searchContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        searchContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchContainer.add(searchField, BorderLayout.CENTER);

        // Placeholder text handling
        searchField.setText("Search friend requests...");
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search friend requests...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(50, 50, 50));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search friend requests...");
                    searchField.setForeground(new Color(150, 150, 150));
                }
            }
        });

        // Search filtering
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterRequests(searchField.getText());
            }
        });

        header.add(titleLabel);
        header.add(Box.createVerticalStrut(15));
        header.add(searchContainer);

        return header;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Accept button
        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        acceptBtn.setForeground(Color.WHITE);
        acceptBtn.setBackground(new Color(13, 110, 253));
        acceptBtn.setFocusable(false);
        acceptBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        acceptBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptBtn.setOpaque(true);
        acceptBtn.setBorderPainted(false);

        acceptBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                acceptBtn.setBackground(new Color(11, 94, 215));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                acceptBtn.setBackground(new Color(13, 110, 253));
            }
        });

        acceptBtn.addActionListener(e -> handleAccept());

        // Decline button
        JButton declineBtn = new JButton("Decline");
        declineBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        declineBtn.setForeground(new Color(220, 53, 69));
        declineBtn.setBackground(Color.WHITE);
        declineBtn.setFocusable(false);
        declineBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 53, 69), 2),
                BorderFactory.createEmptyBorder(10, 28, 10, 28)));
        declineBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        declineBtn.setOpaque(true);

        declineBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                declineBtn.setBackground(new Color(255, 245, 246));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                declineBtn.setBackground(Color.WHITE);
            }
        });

        declineBtn.addActionListener(e -> handleDecline());

        buttonPanel.add(acceptBtn);
        buttonPanel.add(declineBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private void loadDemoRequests() {
        String[] names = {
                "Alice Johnson",
                "Bob Smith",
                "Charlie Brown",
                "David Wilson",
                "Eve Martinez"
        };

        for (String name : names) {
            FriendRequest request = new FriendRequest(name);
            allRequests.add(request);
            addRequestItem(request);
        }
    }

    private void addRequestItem(FriendRequest request) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Avatar
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Avatar circle
                g2d.setColor(new Color(220, 220, 220));
                g2d.fillOval(0, 0, 50, 50);

                // Initial
                g2d.setColor(new Color(100, 100, 100));
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
                String initial = request.name.substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (50 - fm.stringWidth(initial)) / 2;
                int y = ((50 - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(initial, x, y);

                g2d.dispose();
            }
        };
        avatarPanel.setPreferredSize(new Dimension(50, 50));
        avatarPanel.setOpaque(false);

        // Center panel with name and mutual friends
        JPanel centerPanel = new JPanel(new BorderLayout(0, 5));
        centerPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(request.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(50, 50, 50));

        JLabel mutualLabel = new JLabel("2 mutual friends");
        mutualLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mutualLabel.setForeground(new Color(100, 100, 100));

        centerPanel.add(nameLabel, BorderLayout.NORTH);
        centerPanel.add(mutualLabel, BorderLayout.CENTER);

        itemPanel.add(avatarPanel, BorderLayout.WEST);
        itemPanel.add(centerPanel, BorderLayout.CENTER);

        // Store reference to panel in request
        request.panel = itemPanel;

        // Hover and click effects
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedRequest != request) {
                    itemPanel.setBackground(new Color(240, 240, 240));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedRequest != request) {
                    itemPanel.setBackground(Color.WHITE);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                selectRequest(request);
            }
        });

        requestsPanel.add(itemPanel);
        requestsPanel.revalidate();
        requestsPanel.repaint();
    }

    private void selectRequest(FriendRequest request) {
        // Deselect previous
        if (selectedRequest != null && selectedRequest.panel != null) {
            selectedRequest.panel.setBackground(Color.WHITE);
            selectedRequest.panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        }

        // Select new
        selectedRequest = request;
        if (selectedRequest.panel != null) {
            selectedRequest.panel.setBackground(new Color(230, 240, 255));
            selectedRequest.panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(59, 130, 246)),
                    BorderFactory.createEmptyBorder(10, 11, 10, 15)));
        }

        // Show action panel
        actionPanel.setVisible(true);
        revalidate();
        repaint();
    }

    private void handleAccept() {
        if (selectedRequest != null) {
            allRequests.remove(selectedRequest);
            if (selectedRequest.panel != null) {
                requestsPanel.remove(selectedRequest.panel);
            }

            JOptionPane.showMessageDialog(this,
                    "Accepted friend request from " + selectedRequest.name,
                    "Friend Request Accepted",
                    JOptionPane.INFORMATION_MESSAGE);

            selectedRequest = null;
            actionPanel.setVisible(false);
            requestsPanel.revalidate();
            requestsPanel.repaint();
        }
    }

    private void handleDecline() {
        if (selectedRequest != null) {
            allRequests.remove(selectedRequest);
            if (selectedRequest.panel != null) {
                requestsPanel.remove(selectedRequest.panel);
            }

            JOptionPane.showMessageDialog(this,
                    "Declined friend request from " + selectedRequest.name,
                    "Friend Request Declined",
                    JOptionPane.INFORMATION_MESSAGE);

            selectedRequest = null;
            actionPanel.setVisible(false);
            requestsPanel.revalidate();
            requestsPanel.repaint();
        }
    }

    private void filterRequests(String query) {
        requestsPanel.removeAll();

        if (query.equals("Search friend requests...") || query.trim().isEmpty()) {
            // Show all requests
            for (FriendRequest request : allRequests) {
                addRequestItem(request);
            }
        } else {
            // Filter requests
            for (FriendRequest request : allRequests) {
                if (request.name.toLowerCase().contains(query.toLowerCase())) {
                    addRequestItem(request);
                }
            }
        }

        // Clear selection if filtered out
        if (selectedRequest != null) {
            boolean found = false;
            for (int i = 0; i < requestsPanel.getComponentCount(); i++) {
                if (requestsPanel.getComponent(i) == selectedRequest.panel) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                selectedRequest = null;
                actionPanel.setVisible(false);
            }
        }

        requestsPanel.revalidate();
        requestsPanel.repaint();
    }

    // Inner class for friend request data
    private static class FriendRequest {
        String name;
        JPanel panel;

        FriendRequest(String name) {
            this.name = name;
        }
    }
}