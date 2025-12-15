package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.json.JSONArray;
import org.json.JSONObject;

import models.Request;
import utils.ApiUrl;
import utils.UserSession;

import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FriendRequests extends JPanel {
    private JTextField searchField;
    private JPanel requestsPanel;

    private List<Request> allRequests;
    private List<RequestItem> displayedItems;
    private RequestItem selectedItem;

    private SwingWorker<List<Request>, Void> worker;

    // 🔥 MÀU SẮC ĐỒNG BỘ
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(148, 163, 184);
    private final Color COLOR_GREEN = new Color(16, 185, 129); // Accept
    private final Color COLOR_RED = new Color(239, 68, 68);     // Reject
    private final Color BG_HOVER = new Color(241, 245, 249); 

    public FriendRequests() {
        allRequests = new ArrayList<>();
        displayedItems = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);

        JScrollPane scrollPane = createScrollPane();
        // 🔥 Đặt padding cho RequestsPanel bên trong JScrollPane
        requestsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); 
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));

        JLabel titleLabel = new JLabel("Friend Requests");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(titleLabel);
        header.add(Box.createVerticalStrut(15));

        // --- SEARCH BAR ---
        RoundedPanel searchContainer = new RoundedPanel(20, new Color(243, 244, 246));
        searchContainer.setLayout(new BorderLayout());
        searchContainer.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); 
        searchContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(243, 244, 246));
        String placeholder = "Search friend requests...";
        searchField.setText(placeholder);
        searchField.setForeground(TEXT_SECONDARY);

        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(placeholder)) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_PRIMARY);
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(placeholder);
                    searchField.setForeground(TEXT_SECONDARY);
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText().trim();
                if (text.equals(placeholder) || text.isEmpty()) {
                    displayAllRequests();
                } else {
                    filterRequests(text);
                }
            }
        });

        searchContainer.add(searchField, BorderLayout.CENTER);
        header.add(searchContainer);

        return header;
    }

    private JScrollPane createScrollPane() {
        requestsPanel = new JPanel();
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
        requestsPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(requestsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // Customize Scrollbar UI (Giữ nguyên)
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(180, 180, 180);
                this.trackColor = new Color(240, 240, 240);
            }
            @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
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

    private void removeRequestItem(RequestItem item) {
        allRequests.remove(item.getRequest());
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

        if (allRequests.isEmpty()) {
            showEmptyState("No friend requests yet.");
            return;
        }

        for (Request request : allRequests) {
            // Khởi tạo RequestItem và truyền instance của FriendRequests để truy cập màu sắc
            RequestItem item = new RequestItem(request); 
            
            // 🔥 Cần truyền màu sắc và Parent để RequestItem có thể dùng
            // (Tuy nhiên, RequestItem đã sử dụng màu sắc nội bộ, nên ta chỉ cần setup click và callback)
            
            setupItemClick(item);

            // Bắt sự kiện khi Request được xử lý (Accept/Reject)
            item.setOnRequestHandled(e -> {
                // Đảm bảo rằng item được xóa khỏi list sau khi xử lý thành công
                SwingUtilities.invokeLater(() -> removeRequestItem(item));
            });

            displayedItems.add(item);
            requestsPanel.add(item);
            requestsPanel.add(Box.createVerticalStrut(10)); // Thêm khoảng cách giữa các item
        }

        requestsPanel.revalidate();
        requestsPanel.repaint();
    }

    private void filterRequests(String query) {
        clearList();

        for (Request request : allRequests) {
            if (request.getName().toLowerCase().contains(query.toLowerCase())) {
                RequestItem item = new RequestItem(request);
                setupItemClick(item);

                item.setOnRequestHandled(e -> {
                    SwingUtilities.invokeLater(() -> removeRequestItem(item));
                });

                displayedItems.add(item);
                requestsPanel.add(item);
                requestsPanel.add(Box.createVerticalStrut(10));
            }
        }

        requestsPanel.revalidate();
        requestsPanel.repaint();
    }

    private void setupItemClick(RequestItem item) {
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedItem != item) {
                    item.setBackground(BG_HOVER);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                 if (selectedItem != item) {
                    item.setBackground(Color.WHITE);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                selectRequest(item);
            }
        });
    }

    private void selectRequest(RequestItem item) {
        // Deselect previous
        if (selectedItem != null) {
            // 🔥 Gọi phương thức deselect() của item
            selectedItem.deselect(); 
        }
        
        // Nếu chọn lại item đang được chọn, coi như bỏ chọn (toggle)
        if (selectedItem == item) {
            selectedItem = null;
            item.deselect();
        } else {
            // Select new
            selectedItem = item;
            // 🔥 Gọi phương thức select() của item
            item.select(); 
        }
    }

    private void showEmptyState(String message) {
        requestsPanel.removeAll();
        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msgLabel.setForeground(TEXT_SECONDARY);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        requestsPanel.add(Box.createVerticalStrut(50));
        requestsPanel.add(msgLabel);
        requestsPanel.revalidate();
        requestsPanel.repaint();
    }


    // Public method to fetch requests from backend (call this when tab is opened)
    public void fetchRequests() {
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
        selectedItem = null;

        worker = new SwingWorker<List<Request>, Void>() {
            @Override
            protected List<Request> doInBackground() throws Exception {
                List<Request> list = new ArrayList<>();
                String url = ApiUrl.FRIENDREQUESTLIST;
                String token = UserSession.getUser().getToken();

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + token)
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();

                JSONArray arr = new JSONArray();

                if (responseBody.trim().startsWith("[")) {
                    arr = new JSONArray(responseBody);
                } else if (responseBody.trim().startsWith("{")) {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.has("array")) {
                        arr = json.getJSONArray("array");
                    } else if (json.has("data")) {
                        arr = json.getJSONArray("data");
                    } else if (json.has("friendRequests")) {
                        arr = json.getJSONArray("friendRequests");
                    }
                }

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);

                    // Lấy các trường cần thiết
                    String name = o.optString("username", "Unknown User");
                    String avatar = o.optString("avatarUrl", "");
                    int userId = o.optInt("userId", -1);
                    String time = o.optString("sentAt", "");

                    list.add(new Request(name, avatar, userId, time));
                }
                return list;
            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        List<Request> results = get();
                        allRequests = results;
                        displayAllRequests();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    clearList();
                    showEmptyState("Error loading requests.");
                }
            }
        };
        worker.execute();
    }
    
    // Public getter cho màu sắc (dù RequestItem hiện tại không sử dụng)
    public Color getAcceptColor() { return COLOR_GREEN; }
    public Color getRejectColor() { return COLOR_RED; }
    public Color getHoverColor() { return BG_HOVER; }
    public Color getSelectedColor() { return new Color(219, 234, 254); } // Xanh nhạt

    // Reset the panel (optional, similar to SearchFriend)
    public void resetPanel() {
        searchField.setText("Search friend requests...");
        searchField.setForeground(TEXT_SECONDARY);
        selectedItem = null;
        clearList();
    }

    // Add this inner class at the end of FriendRequests.java
    class RoundedPanel extends JPanel {
        private int radius;
        private Color backgroundColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }
}