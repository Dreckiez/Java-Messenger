package components;

import utils.ImageLoader;
import utils.UserSession;
import utils.ImageEditor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessageSearchDialog extends JDialog {

    // --- CẤU HÌNH UI ---
    private final Color BG_MAIN = Color.WHITE;
    private final Color BG_INPUT = new Color(241, 245, 249); 
    private final Color BG_CARD_HOVER = new Color(248, 250, 252);
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private final Color ACCENT_COLOR = new Color(59, 130, 246); // Màu xanh cho tên nhóm
    private final Color CARD_BORDER = new Color(226, 232, 240);
    
    private final Font FONT_MAIN = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    private final int SEARCH_RADIUS = 40; 
    private final int CARD_RADIUS = 16;   

    private JTextField searchField;
    private JPanel resultPanel;
    private JScrollPane scrollPane;
    private JLabel statusLabel;
    
    private Timer debounceTimer;
    private ImageEditor imageEditor;
    private Consumer<JSONObject> onResultSelected;
    
    private long currentChatId; 
    private boolean isGroup;
    private boolean isGlobalSearch; // 🔥 Biến xác định chế độ tìm kiếm
    private static final String API_BASE_URL = "http://localhost:8080";

    public MessageSearchDialog(Frame owner, long currentChatId, boolean isGroup, Consumer<JSONObject> onSelect) {
        super(owner, "Search Messages", true);
        
        // 🔥 LOGIC: Nếu chatId = -1 thì là Global Search
        if (currentChatId == -1) {
            this.isGlobalSearch = true;
            this.currentChatId = 0;
            setTitle("Search All Messages");
        } else {
            this.isGlobalSearch = false;
            this.currentChatId = currentChatId;
            this.isGroup = isGroup;
            setTitle("Search in Conversation");
        }

        this.onResultSelected = onSelect;
        this.imageEditor = new ImageEditor();

        setSize(650, 750); 
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        initHeaderModern();
        initResultListModern();
        
        debounceTimer = new Timer(500, e -> executeSearch());
        debounceTimer.setRepeats(false);
    }

    private void initHeaderModern() {
        JPanel header = new JPanel(new BorderLayout(0, 15));
        header.setBackground(BG_MAIN);
        header.setBorder(new EmptyBorder(25, 25, 15, 25));

        // Tiêu đề thay đổi theo ngữ cảnh
        String titleText = isGlobalSearch ? "Search Everywhere" : "Search Messages";
        JLabel titleLbl = new JLabel(titleText);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLbl.setForeground(TEXT_PRIMARY);
        header.add(titleLbl, BorderLayout.NORTH);

        JPanel controlsPanel = new JPanel(new BorderLayout(15, 0));
        controlsPanel.setBackground(BG_MAIN);

        RoundedPanel searchContainer = new RoundedPanel(SEARCH_RADIUS, BG_INPUT);
        searchContainer.setLayout(new BorderLayout(10, 0));
        searchContainer.setBorder(new EmptyBorder(5, 15, 5, 15));
        searchContainer.setPreferredSize(new Dimension(200, 44));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchIcon.setForeground(TEXT_SECONDARY);
        
        searchField = new JTextField();
        searchField.setFont(FONT_MAIN);
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setBackground(new Color(0,0,0,0));

        searchContainer.add(searchIcon, BorderLayout.WEST);
        searchContainer.add(searchField, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { restartTimer(); }
            public void removeUpdate(DocumentEvent e) { restartTimer(); }
            public void changedUpdate(DocumentEvent e) { restartTimer(); }
        });

        controlsPanel.add(searchContainer, BorderLayout.CENTER);
        header.add(controlsPanel, BorderLayout.CENTER);
        
        statusLabel = new JLabel("Type to start searching...");
        statusLabel.setFont(FONT_SMALL);
        statusLabel.setForeground(TEXT_SECONDARY);
        header.add(statusLabel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
    }
    
    private void restartTimer() { debounceTimer.restart(); }

    private void initResultListModern() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(BG_MAIN);
        resultPanel.setBorder(new EmptyBorder(10, 25, 25, 25));

        scrollPane = new JScrollPane(resultPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    // --- LOGIC SEARCH API ---
    private void executeSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            resultPanel.removeAll();
            statusLabel.setText("Type to start searching...");
            repaintList();
            return;
        }

        statusLabel.setText("Searching...");
        resultPanel.removeAll();
        
        new SwingWorker<List<JSONObject>, Void>() {
            @Override
            protected List<JSONObject> doInBackground() throws Exception {
                List<JSONObject> results = new ArrayList<>();
                String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
                String urlString;

                // 🔥 UPDATE: Xử lý 3 trường hợp API
                if (isGlobalSearch) {
                    // Endpoint Global
                    urlString = API_BASE_URL + "/api/chat/user/search/conversation-messages?keyword=" + encodedKeyword;
                } else if (isGroup) {
                    // Endpoint Group cụ thể
                    urlString = API_BASE_URL + "/api/chat/user/search/" + currentChatId + "/group-conversation-messages?keyword=" + encodedKeyword;
                } else {
                    // Endpoint Private cụ thể
                    urlString = API_BASE_URL + "/api/chat/user/search/" + currentChatId + "/private-conversation-messages?keyword=" + encodedKeyword;
                }
                
                String token = UserSession.getUser().getToken();
                
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + token);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) content.append(inputLine);
                    in.close();

                    JSONArray jsonArray = new JSONArray(content.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        results.add(jsonArray.getJSONObject(i));
                    }
                }
                return results;
            }

            @Override
            protected void done() {
                try {
                    List<JSONObject> data = get();
                    if (data == null || data.isEmpty()) {
                        statusLabel.setText("No results found.");
                        showEmptyState();
                    } else {
                        statusLabel.setText("Found " + data.size() + " messages.");
                        for (JSONObject msg : data) addResultItemModern(msg);
                    }
                } catch (Exception e) {
                    statusLabel.setText("Error occurred.");
                    e.printStackTrace();
                }
                repaintList();
            }
        }.execute();
    }
    
    private void repaintList() {
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    // 🔥 SETUP GIAO DIỆN KẾT QUẢ (CẬP NHẬT LOGIC GROUP/PRIVATE)
    private void addResultItemModern(JSONObject msg) {
        String senderName = msg.optString("name", "Unknown");
        String content = msg.optString("content", "");
        String rawTime = msg.optString("sentAt", "");
        String avatarUrl = msg.optString("avatarUrl", null);
        
        // 🔥 Lấy thông tin loại hội thoại và tên nhóm
        String conversationType = msg.optString("conversationType", ""); // "GROUP" hoặc "PRIVATE"
        String conversationName = msg.optString("conversationName", "");
        
        String displayTime = formatTime(rawTime);

        // Container Card
        RoundedPanel card = new RoundedPanel(CARD_RADIUS, Color.WHITE);
        card.setLayout(new BorderLayout(15, 10)); 
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setBorderPaint(CARD_BORDER);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Tăng chiều cao max xíu
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 1. Avatar (Left)
        JLabel avatarLbl = new JLabel();
        avatarLbl.setPreferredSize(new Dimension(48, 48));
        avatarLbl.setIcon(createPlaceholderIcon(senderName, 48));
        
        if (avatarUrl != null && !avatarUrl.isEmpty() && !avatarUrl.equals("null")) {
             ImageLoader.loadImageAsync(avatarUrl, img -> {
                if (img != null) {
                    avatarLbl.setIcon(imageEditor.makeCircularImage(img, 48));
                    avatarLbl.repaint();
                }
            });
        }
        
        JPanel avatarWrapper = new JPanel(new BorderLayout());
        avatarWrapper.setOpaque(false);
        avatarWrapper.add(avatarLbl, BorderLayout.NORTH);
        card.add(avatarWrapper, BorderLayout.WEST);

        // 2. Content (Center)
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS)); 
        textPanel.setOpaque(false);

        // Row 1: Name + Time
        JPanel topText = new JPanel(new BorderLayout());
        topText.setOpaque(false);
        topText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel nameLbl = new JLabel(senderName);
        nameLbl.setFont(FONT_BOLD);
        nameLbl.setForeground(TEXT_PRIMARY);
        
        JLabel timeLbl = new JLabel(displayTime);
        timeLbl.setFont(FONT_SMALL);
        timeLbl.setForeground(TEXT_SECONDARY);

        topText.add(nameLbl, BorderLayout.WEST);
        topText.add(timeLbl, BorderLayout.EAST);
        textPanel.add(topText);
        
        // 🔥 LOGIC HIỂN THỊ CONTEXT (Tên Nhóm)
        // Chỉ hiện khi: Đang Search Global VÀ Loại là GROUP VÀ Có tên nhóm
        boolean showGroupContext = isGlobalSearch 
                                   && "GROUP".equalsIgnoreCase(conversationType) 
                                   && !conversationName.isEmpty();

        if (showGroupContext) {
            textPanel.add(Box.createVerticalStrut(2)); // Cách dòng trên
            
            JLabel contextLbl = new JLabel("in " + conversationName);
            contextLbl.setFont(new Font("Segoe UI", Font.ITALIC, 11)); // Chữ nghiêng, nhỏ
            contextLbl.setForeground(ACCENT_COLOR); // Màu xanh nổi bật
            contextLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            textPanel.add(contextLbl);
            textPanel.add(Box.createVerticalStrut(6)); // Cách dòng dưới
        } else {
            // Nếu là Private hoặc Search Local thì chỉ cần khoảng trắng thường
            textPanel.add(Box.createVerticalStrut(10)); 
        }

        // Row 2: Message Content
        String displayContent = content;
        if (displayContent.length() > 90) displayContent = displayContent.substring(0, 87) + "...";
        
        JLabel msgLbl = new JLabel("<html>" + displayContent + "</html>");
        msgLbl.setFont(FONT_MAIN);
        msgLbl.setForeground(TEXT_PRIMARY);
        msgLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(msgLbl);

        card.add(textPanel, BorderLayout.CENTER);

        // Hover Effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackgroundColor(BG_CARD_HOVER); card.repaint(); }
            public void mouseExited(MouseEvent e) { card.setBackgroundColor(Color.WHITE); card.repaint(); }
            public void mouseClicked(MouseEvent e) {
                if (onResultSelected != null) {
                    onResultSelected.accept(msg);
                    dispose();
                }
            }
        });

        resultPanel.add(card);
        resultPanel.add(Box.createVerticalStrut(12));
    }

    private String formatTime(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoDate); 
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            return dateTime.format(formatter);
        } catch (Exception e) {
            return isoDate;
        }
    }

    private void showEmptyState() {
        JLabel empty = new JLabel("No messages found", SwingConstants.CENTER);
        empty.setFont(FONT_MAIN);
        empty.setForeground(TEXT_SECONDARY);
        empty.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(Box.createVerticalStrut(30));
        resultPanel.add(empty);
    }

    private ImageIcon createPlaceholderIcon(String name, int size) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(203, 213, 225));
        g2.fillOval(0, 0, size, size);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
        String initial = (name != null && !name.isEmpty()) ? name.substring(0, 1).toUpperCase() : "?";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(initial, (size - fm.stringWidth(initial)) / 2, ((size - fm.getHeight()) / 2) + fm.getAscent());
        g2.dispose();
        return new ImageIcon(img);
    }

    private static class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private Color borderColor = null;
        private int cornerRadius = 15;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }
        
        public void setBackgroundColor(Color color) { this.backgroundColor = color; }
        public void setBorderPaint(Color color) { this.borderColor = color; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
            
            if (borderColor != null) {
                graphics.setColor(borderColor);
                graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
            }
        }
    }
}