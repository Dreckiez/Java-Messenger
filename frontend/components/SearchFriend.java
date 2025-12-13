package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import org.json.JSONArray;
import org.json.JSONObject;
import models.Contact;
import utils.ApiClient;
import utils.ApiUrl;
import utils.UserSession;
import java.awt.*;
import java.awt.event.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchFriend extends JPanel {

    private JTextField searchField;
    private JPanel contactsPanel;
    private JPanel centerContainer;

    private final List<ContactItem> displayed;
    private ContactItem selected; // Lưu item đang được chọn
    private SwingWorker<List<Contact>, Void> worker;

    // --- MÀU SẮC ---
    private final Color ME_BG_COLOR = new Color(236, 253, 245);
    private final Color BG_COLOR = new Color(248, 250, 252);     
    private final Color ITEM_BG = Color.WHITE;                   
    private final Color HOVER_BG = new Color(241, 245, 249);     
    private final Color SELECTED_BG = new Color(219, 234, 254);  
    
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_HINT = new Color(148, 163, 184);

    public SearchFriend(NavPanel parent) {
        displayed = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);

        showEmptyState("Type a name to search..."); 
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        // Giữ padding 15x2 cho Header (30px)
        header.setBorder(new EmptyBorder(20, 15, 10, 15)); 
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Find Friends");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        header.add(title);
        header.add(Box.createVerticalStrut(15));

        // --- SEARCH BAR (SYNCED STYLE) ---
        RoundedPanel searchContainer = new RoundedPanel(20, new Color(243, 244, 246));
        searchContainer.setLayout(new BorderLayout());
        searchContainer.setBorder(new EmptyBorder(8, 15, 8, 15)); 
        searchContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        searchContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        searchField.setBackground(new Color(243, 244, 246)); 
        
        setupPlaceholder(searchField, "Search by name..."); 

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText().trim();
                if (text.isEmpty() || text.equals("Search by name...")) {
                    clearList();
                    showEmptyState("Type a name to search...");
                } else {
                    searchBackend(text);
                }
            }
        });

        searchContainer.add(searchField, BorderLayout.CENTER);
        header.add(searchContainer);

        return header;
    }

    private JScrollPane createBody() {
        contactsPanel = new JPanel();
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        contactsPanel.setBackground(BG_COLOR);
        
        // 🔥 THÊM PADDING NGANG CHO CONTACTS PANEL (15px)
        contactsPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(BG_COLOR);
        
        centerContainer.add(contactsPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(centerContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_COLOR);
        
        // 🔥 ĐẢM BẢO THANH CUỘN NGANG TẮT HOÀN TOÀN
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

    // --- LOGIC HIỂN THỊ & XỬ LÝ KHOẢNG CÁCH ---

    private void setupItemEvent(ContactItem item, boolean isMe) {
        // ... (Logic giữ nguyên) ...
        if (isMe) {
            item.setBackground(ME_BG_COLOR);
            return; 
        }

        item.setBackground(ITEM_BG);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (item != selected) {
                    item.setBackground(HOVER_BG);
                    item.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (item != selected) {
                    item.setBackground(ITEM_BG);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (selected != null && selected != item) {
                    selected.setBackground(ITEM_BG);
                    // selected.deselect(); 
                }
                selected = item;
                selected.setBackground(SELECTED_BG);
                // selected.select(); 
            }
        });
    }

    private void displayResults(List<Contact> contacts) {
        clearList();

        if (contacts.isEmpty()) {
            showEmptyState("No results found.");
            return;
        }

        int myId = UserSession.getUser().getId(); 

        for (Contact c : contacts) {
            boolean isMe = (c.getUserId() == myId);

            if (isMe) {
                c.setName(c.getName() + " (You)");
                c.setIsFriend("");
            }
            

            ContactItem item = new ContactItem(c);
            
            contactsPanel.add(item);
            contactsPanel.add(Box.createVerticalStrut(10)); // Giảm khoảng cách cho đẹp hơn
            
            setupItemEvent(item, isMe);
            displayed.add(item);
        }

        contactsPanel.revalidate();
        contactsPanel.repaint();
    }

    // --- BACKEND & HELPER (Giữ nguyên) ---

    private void showEmptyState(String message) {
        clearList();
        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msgLabel.setForeground(TEXT_HINT);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contactsPanel.add(Box.createVerticalStrut(50));
        contactsPanel.add(msgLabel);
        contactsPanel.revalidate();
        contactsPanel.repaint();
    }

    private void searchBackend(String keyword) {
        if (worker != null && !worker.isDone()) worker.cancel(true);
        // ... (Logic SwingWorker giữ nguyên) ...
        worker = new SwingWorker<List<Contact>, Void>() {
            @Override protected List<Contact> doInBackground() throws Exception {
                List<Contact> list = new ArrayList<>();
                String url = ApiUrl.SEARCH;
                if (!keyword.isEmpty()) {
                    String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
                    url += "?keyword=" + encodedKeyword;
                }
                JSONObject json = ApiClient.getJSON(url, UserSession.getUser().getToken());
                JSONArray arr = json.getJSONArray("array");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    int id = o.getInt("userId");
                    String name = o.optString("username", "");
                    String avatar = o.optString("avatarUrl", "");
                    String isFriend = o.getString("status");
                    String sentAt = o.optString("sentAt", null);
                    list.add(new Contact(id, name, avatar, isFriend, sentAt));
                }
                return list;
            }
            @Override protected void done() {
                try {
                    if (!isCancelled()) displayResults(get());
                } catch (Exception e) {
                    e.printStackTrace();
                    showEmptyState("Error searching.");
                }
            }
        };
        worker.execute();
    }

    public void resetSearch() {
        searchField.setText("Search by name...");
        searchField.setForeground(TEXT_HINT);
        showEmptyState("Type a name to search...");
    }

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
    public void clearList() { 
        contactsPanel.removeAll(); 
        displayed.clear(); 
        selected = null; 
        contactsPanel.revalidate(); contactsPanel.repaint(); 
    }
}