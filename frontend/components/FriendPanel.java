package components;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.json.JSONArray;
import org.json.JSONObject;

import models.Friend;
import utils.ApiClient;
import utils.ApiUrl;
import utils.UserSession;

public class FriendPanel extends JPanel {
    private JTextField searchField;
    private JPanel listPanel;
    private JPanel centerContainer;
    
    private List<Friend> allFriends; 
    private List<FriendItem> displayedItems; 
    private FriendItem selectedItem;
    
    private SwingWorker<List<Friend>, Void> worker;
    
    // --- CALLBACKS ---
    private Runnable onNavigateToChat; 
    private Consumer<FriendItem.ChatTarget> onOpenChat; // Callback mở chat bằng ID

    // --- MÀU SẮC ---
    private final Color BG_COLOR = new Color(248, 250, 252);
    private final Color ITEM_BG = Color.WHITE;
    private final Color HOVER_BG = new Color(241, 245, 249);
    private final Color SELECTED_BG = new Color(219, 234, 254);
    
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_HINT = new Color(148, 163, 184);

    public FriendPanel() {
        allFriends = new ArrayList<>();
        displayedItems = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
        
        showEmptyState("Loading friends...");
    }

    // --- SETTERS ---
    public void setOnNavigateToChat(Runnable onNavigateToChat) {
        this.onNavigateToChat = onNavigateToChat;
    }
    
    // Setter quan trọng để nhận Callback từ HomeScreen
    public void setOnOpenChat(Consumer<FriendItem.ChatTarget> onOpenChat) { 
        this.onOpenChat = onOpenChat; 
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(20, 15, 10, 15));

        JLabel titleLabel = new JLabel("My Friends");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(titleLabel);
        header.add(Box.createVerticalStrut(15));

        // Search Bar
        RoundedPanel searchContainer = new RoundedPanel(20, new Color(243, 244, 246));
        searchContainer.setLayout(new BorderLayout());
        searchContainer.setBorder(new EmptyBorder(8, 15, 8, 15)); 
        searchContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        searchContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(243, 244, 246)); 
        searchField.setOpaque(false);
        
        setupPlaceholder(searchField, "Search friends...");

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText().trim();
                if (text.isEmpty() || text.equals("Search friends...")) {
                    displayFriends(allFriends); 
                } else {
                    filterFriends(text);
                }
            }
        });

        searchContainer.add(searchField, BorderLayout.CENTER);
        header.add(searchContainer);

        return header;
    }

    private JScrollPane createBody() {
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_COLOR);
        
        centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(BG_COLOR);
        centerContainer.setBorder(new EmptyBorder(0, 20, 0, 20)); 
        
        centerContainer.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(centerContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_COLOR);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(203, 213, 225);
                this.trackColor = BG_COLOR;
            }
            @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });

        return scroll;
    }

    // --- LOGIC HIỂN THỊ ---

    private void clearList() {
        listPanel.removeAll();
        displayedItems.clear();
        selectedItem = null;
        listPanel.revalidate();
        listPanel.repaint();
    }
    
    private void showEmptyState(String msg) {
        clearList();
        JLabel label = new JLabel(msg);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_HINT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        listPanel.add(Box.createVerticalStrut(50));
        listPanel.add(label);
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void displayFriends(List<Friend> friends) {
        clearList();

        if (friends.isEmpty()) {
            showEmptyState("No friends found.");
            return;
        }

        for (Friend friend : friends) {
            FriendItem item = new FriendItem(friend);
            
            // 1. Truyền callback Create Group
            if (this.onNavigateToChat != null) {
                item.setOnNavigateToChat(this.onNavigateToChat);
            }
            
            // 🔥 2. TRUYỀN CALLBACK OPEN CHAT (QUAN TRỌNG)
            if (this.onOpenChat != null) {
                item.setOnOpenChat(this.onOpenChat);
            }

            // 3. Xử lý sự kiện Unfriend/Block (Lazy Remove)
            item.setOnRequestHandled(e -> {
                String cmd = e.getActionCommand();
                if ("UNFRIEND".equals(cmd) || "BLOCK".equals(cmd)) {
                    // Xóa item khỏi UI ngay lập tức
                    listPanel.remove(item);
                    displayedItems.remove(item);
                    allFriends.remove(friend); // Xóa khỏi cache
                    
                    listPanel.revalidate();
                    listPanel.repaint();
                    
                    if (displayedItems.isEmpty()) {
                        showEmptyState("No friends found.");
                    }
                }
            });

            setupItemClick(item);

            listPanel.add(item);
            listPanel.add(Box.createVerticalStrut(15));
            
            displayedItems.add(item);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void filterFriends(String query) {
        List<Friend> filtered = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Friend f : allFriends) {
            if (f.getName().toLowerCase().contains(lowerQuery)) {
                filtered.add(f);
            }
        }
        displayFriends(filtered);
    }

    private void setupItemClick(FriendItem item) {
        item.setBackground(ITEM_BG); // Default

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (item != selectedItem) {
                    item.setBackground(HOVER_BG);
                    item.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (item != selectedItem) {
                    item.setBackground(ITEM_BG);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedItem != null && selectedItem != item) {
                    selectedItem.setBackground(ITEM_BG);
                    selectedItem.deselect(); 
                }
                selectedItem = item;
                selectedItem.setBackground(SELECTED_BG);
                selectedItem.select(); 
            }
        });
    }

    // --- LOGIC API ---

    public void fetchRequests() {
        if (worker != null && !worker.isDone()) worker.cancel(true);
        
        showEmptyState("Loading...");

        worker = new SwingWorker<List<Friend>, Void>() {
            @Override
            protected List<Friend> doInBackground() throws Exception {
                List<Friend> list = new ArrayList<>();
                JSONObject json = ApiClient.getJSON(ApiUrl.FRIENDLIST, UserSession.getUser().getToken());
                
                JSONArray arr = json.optJSONArray("listOfFriend"); 
                if (arr == null) arr = new JSONArray(); 

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    String name = o.optString("username", "Unknown");
                    String avatar = o.optString("avatarUrl", "");
                    int userId = o.getInt("userId");
                    Boolean isonline = o.optBoolean("isOnline", false);

                    list.add(new Friend(name, avatar, userId, isonline));
                }
                return list;
            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        allFriends = get();
                        displayFriends(allFriends);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showEmptyState("Failed to load friends.");
                }
            }
        };
        worker.execute();
    }

    public void resetPanel() {
        searchField.setText("Search friends...");
        searchField.setForeground(TEXT_HINT);
        selectedItem = null;
        fetchRequests(); 
    }

    // --- HELPER CLASSES ---

    private void setupPlaceholder(JTextField field, String text) {
        field.setText(text);
        field.setForeground(TEXT_HINT);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(TEXT_HINT);
                }
            }
        });
    }

    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(new Color(226, 232, 240));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            super.paintComponent(g);
        }
    }
}