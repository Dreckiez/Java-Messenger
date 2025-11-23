package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.json.JSONArray;
import org.json.JSONObject;

import models.Friend;
import models.Request;
import utils.ApiClient;
import utils.ApiUrl;
import utils.UserSession;

public class FriendPanel extends JPanel {
    private JTextField searchField;
    private JPanel requestsPanel;

    private List<Friend> allRequests;
    private List<FriendItem> displayedItems;
    private FriendItem selectedItem;

    private SwingWorker<List<Friend>, Void> worker;

    public FriendPanel() {
        allRequests = new ArrayList<>();
        displayedItems = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);

        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Requests list with custom scrollbar
        requestsPanel = new JPanel();
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
        requestsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = createScrollPane();
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        // Title
        JLabel titleLabel = new JLabel("Friends");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(5, 5, 5));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(titleLabel);
        header.add(Box.createVerticalStrut(10));

        // Search field
        searchField = new JTextField("Search friends...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(new Color(150, 150, 150));
        searchField.setBackground(new Color(240, 242, 245));
        searchField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Placeholder text handling
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search friends...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(50, 50, 50));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search friends...");
                    searchField.setForeground(new Color(150, 150, 150));
                }
            }
        });

        // Search filtering
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText().trim();
                if (text.equals("Search friends...") || text.isEmpty()) {
                    displayAllRequests();
                } else {
                    filterRequests(text);
                }
            }
        });

        header.add(searchField);
        return header;
    }

    private JScrollPane createScrollPane() {
        JScrollPane scroll = new JScrollPane(requestsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(180, 180, 180);
                this.trackColor = new Color(240, 240, 240);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });

        return scroll;
    }

    private void clearList() {
        requestsPanel.removeAll();
        displayedItems.clear();
        requestsPanel.revalidate();
        requestsPanel.repaint();
    }

    private void removeRequestItem(FriendItem item) {
        allRequests.remove(item.getFriend());
        displayedItems.remove(item);
        requestsPanel.remove(item);

        if (selectedItem == item) {
            selectedItem = null;
        }

        requestsPanel.revalidate();
        requestsPanel.repaint();
    }

    private void displayAllRequests() {
        clearList();

        for (Friend request : allRequests) {
            FriendItem item = new FriendItem(request);
            setupItemClick(item);

            item.setOnRequestHandled(e -> removeRequestItem(item));

            displayedItems.add(item);
            requestsPanel.add(item);
        }

        requestsPanel.revalidate();
        requestsPanel.repaint();
    }

    private void filterRequests(String query) {
        clearList();

        for (Friend request : allRequests) {
            if (request.getName().toLowerCase().contains(query.toLowerCase())) {
                FriendItem item = new FriendItem(request);
                setupItemClick(item);

                item.setOnRequestHandled(e -> removeRequestItem(item));

                displayedItems.add(item);
                requestsPanel.add(item);
            }
        }

        requestsPanel.revalidate();
        requestsPanel.repaint();
    }

    private void setupItemClick(FriendItem item) {
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                selectRequest(item);
            }
        });
    }

    private void selectRequest(FriendItem item) {
        // Deselect previous
        if (selectedItem != null) {
            selectedItem.deselect();
        }

        // Select new
        selectedItem = item;
        item.select();
    }

    // Public method to fetch requests from backend (call this when tab is opened)
    public void fetchRequests() {
        // Cancel previous worker if still running
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }

        // Clear selection
        selectedItem = null;

        worker = new SwingWorker<List<Friend>, Void>() {
            @Override
            protected List<Friend> doInBackground() throws Exception {
                List<Friend> list = new ArrayList<>();

                JSONObject json = ApiClient.getJSON(ApiUrl.FRIENDLIST, UserSession.getUser().getToken());
                JSONArray arr = json.getJSONArray("listOfFriend"); // Adjust based on your API response

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);

                    String name = o.optString("fullName", "");
                    String avatar = o.optString("avatarUrl", "");
                    int userId = o.getInt("userId");
                    Boolean isonline = o.getBoolean("isOnline");

                    list.add(new Friend(name, avatar, userId, isonline));
                }
                return list;
            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        List<Friend> results = get();
                        allRequests = results;
                        displayAllRequests();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    clearList();
                    JOptionPane.showMessageDialog(FriendPanel.this,
                            "Failed to load friend",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    // Reset the panel (optional, similar to SearchFriend)
    public void resetPanel() {
        searchField.setText("Search friend...");
        searchField.setForeground(new Color(150, 150, 150));
        selectedItem = null;
        clearList();
    }
}