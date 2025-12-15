package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.ApiClient;
import utils.ApiUrl; 
import utils.UserSession;
import models.User; 
import utils.TimeHandler; 
import javax.swing.event.AncestorListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BlockedUserPanel extends JPanel {
    private JPanel listPanel;
    private List<BlockedUserItem> displayedItems;
    private SwingWorker<List<JSONObject>, Void> worker; 
    
    // --- MÀU SẮC ---
    private final Color BG_COLOR = new Color(248, 250, 252);
    private final Color ITEM_BG = Color.WHITE;
    private final Color HOVER_BG = new Color(241, 245, 249);
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_HINT = new Color(148, 163, 184);
    // Màu nút
    private final Color BTN_UNBLOCK_BASE = new Color(14, 191, 245); 
    private final Color BTN_UNBLOCK_HOVER = new Color(12, 179, 230); 
    private final Color AVATAR_BG = new Color(239, 68, 68); 
    
    public BlockedUserPanel() {
        displayedItems = new ArrayList<>();
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
        
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                fetchBlockedUsers();
            }

            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) { }

            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent event) { }
        });
        
        showEmptyState("No users are currently blocked.");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(30, 25, 20, 25)); 

        JLabel title = new JLabel("Blocked Users");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26)); 
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        
        return header;
    }

    private JScrollPane createBody() {
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_COLOR);
        listPanel.setBorder(new EmptyBorder(0, 20, 0, 20)); 

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_COLOR);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        styleScrollBar(scroll); 
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }
    
    private void styleScrollBar(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
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
    }

    public void fetchBlockedUsers() {
        if (worker != null && !worker.isDone()) worker.cancel(true);
        
        showEmptyState("Loading blocked users...");
        
        User user = UserSession.getUser();
        if (user == null || user.getToken() == null) {
            showEmptyState("Please login to view blocked list.");
            return;
        }

        worker = new SwingWorker<List<JSONObject>, Void>() {
            @Override
            protected List<JSONObject> doInBackground() throws Exception {
                String url = ApiUrl.BLOCK_LIST; 
                String token = user.getToken(); 
                
                JSONObject response = ApiClient.getJSON(url, token);
                
                JSONArray array = new JSONArray();
                String responseString = response.toString().trim();
                
                if (responseString.startsWith("[")) {
                     array = new JSONArray(responseString);
                } else if (response.has("array")) {
                    array = response.getJSONArray("array");
                } else if (response.has("data")) {
                    array = response.getJSONArray("data");
                }
                
                List<JSONObject> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    list.add(array.getJSONObject(i));
                }
                return list;
            }

            @Override
            protected void done() {
                try {
                    List<JSONObject> results = get();
                    displayBlockedUsers(results);
                } catch (Exception e) {
                    e.printStackTrace();
                    showEmptyState("Failed to load list. Check connection.");
                }
            }
        };
        worker.execute();
    }
    
    private void displayBlockedUsers(List<JSONObject> users) {
        listPanel.removeAll();
        displayedItems.clear();

        if (users == null || users.isEmpty()) {
            showEmptyState("No users are currently blocked.");
            listPanel.revalidate();
            listPanel.repaint();
            return;
        }

        for (JSONObject user : users) {
            String blockedAt = user.optString("blockedAt", "");
            BlockedUserItem item = new BlockedUserItem(user);
            
            item.setUnblockCallback(e -> {
                long userIdToUnblock = user.optLong("userId", -1);
                if (userIdToUnblock != -1) {
                    unblockUser(item, userIdToUnblock, blockedAt);
                }
            });
            
            listPanel.add(item);
            listPanel.add(Box.createVerticalStrut(12)); // tăng khoảng cách
            displayedItems.add(item);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void unblockUser(BlockedUserItem item, long blockedUserId, String blockedAt) {
        User currentUser = UserSession.getUser();
        if (currentUser == null) return;
        final long blockerId = currentUser.getId(); 

        item.setUnblockButtonEnabled(false); 
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Do you want to unblock " + item.getName() + "?", 
            "Confirm Unblock", 
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            
            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("blockerId", blockerId);
                    requestBody.put("blockedUserId", blockedUserId);
                    requestBody.put("blockedAt", blockedAt); 

                    String url = ApiUrl.REMOVE_BLOCK; 
                    String token = currentUser.getToken();
                    
                    JSONObject response = ApiClient.deleteJSON(url, requestBody, token); 
                    return response.optString("message", "Request sent.");
                }

                @Override
                protected void done() {
                    try {
                        String message = get();
                        
                        if (message.toLowerCase().contains("successfully") || message.toLowerCase().contains("sent") || message.toLowerCase().contains("removed")) {
                            listPanel.remove(item);
                            displayedItems.remove(item);
                            listPanel.revalidate();
                            listPanel.repaint();
                            
                            JOptionPane.showMessageDialog(BlockedUserPanel.this, 
                                item.getName() + " has been unblocked.", 
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                                
                            if (displayedItems.isEmpty()) {
                                showEmptyState("No users are currently blocked.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(BlockedUserPanel.this, 
                                "Unblock Failed: " + message, 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(BlockedUserPanel.this, 
                            "Connection error during unblock.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        item.setUnblockButtonEnabled(true);
                    }
                }
            }.execute();
        } else {
            item.setUnblockButtonEnabled(true);
        }
    }

    private void showEmptyState(String message) {
        listPanel.removeAll();
        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msgLabel.setForeground(TEXT_HINT);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        listPanel.add(Box.createVerticalGlue());
        listPanel.add(msgLabel);
        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate(); 
        listPanel.repaint();
    }

    private ImageIcon createPlaceholderIcon(String name, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(AVATAR_BG);
        g2d.fillOval(0, 0, size, size);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
        String initial = (name != null && !name.isEmpty()) ? name.substring(0, 1).toUpperCase() : "?";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth(initial)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(initial, x, y);
        g2d.dispose();

        return new ImageIcon(img);
    }

    class RoundedButtonPanel extends JPanel {
        private Color currentColor;
        private final int radius = 30;

        public RoundedButtonPanel(Color initialColor) {
            this.currentColor = initialColor;
            setOpaque(false);
        }

        public void setColor(Color color) {
            this.currentColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight()); 
        }
    }

    class BlockedUserItem extends JPanel {
        private String name;
        private JButton unblockBtn;
        private RoundedButtonPanel btnContainer;
        
        public BlockedUserItem(JSONObject userData) {
            this.name = userData.optString("username", "N/A");

            setLayout(new BorderLayout(15, 0));
            setBackground(ITEM_BG);
            setBorder(new EmptyBorder(12, 15, 12, 15)); 
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 110)); // tăng chiều cao mỗi dòng
            
            String fullName = userData.optString("firstName", "") + " " + userData.optString("lastName", "");
            if (fullName.trim().isEmpty()) fullName = name;
            
            JLabel avt = new JLabel(createPlaceholderIcon(fullName, 50)); 
            
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            centerPanel.setOpaque(false);
            
            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
            nameLabel.setForeground(TEXT_PRIMARY);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            String blockedAt = userData.optString("blockedAt", null);
            TimeHandler timeHandler = new TimeHandler(); 
            String formattedTime = (blockedAt != null && !blockedAt.isEmpty()) 
                                    ? "Blocked " + timeHandler.formatTimeAgo(blockedAt) 
                                    : "Blocked";

            JLabel timeLabel = new JLabel(formattedTime);
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            timeLabel.setForeground(TEXT_HINT);
            timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            centerPanel.add(nameLabel);
            centerPanel.add(timeLabel);

            unblockBtn = new JButton("Unblock");
            unblockBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            unblockBtn.setForeground(Color.WHITE);
            unblockBtn.setFocusPainted(false);
            unblockBtn.setBorderPainted(false);
            unblockBtn.setContentAreaFilled(false);
            unblockBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            unblockBtn.setPreferredSize(new Dimension(110, 36)); // nút rộng hơn và cao hơn

            btnContainer = new RoundedButtonPanel(BTN_UNBLOCK_BASE);
            btnContainer.setLayout(new BorderLayout());
            btnContainer.add(unblockBtn, BorderLayout.CENTER);
            btnContainer.setMaximumSize(new Dimension(110, 36));
            btnContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

            centerPanel.add(Box.createVerticalStrut(8));
            centerPanel.add(btnContainer);

            unblockBtn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { 
                    if (unblockBtn.isEnabled()) btnContainer.setColor(BTN_UNBLOCK_HOVER); 
                }
                public void mouseExited(MouseEvent e) { 
                    if (unblockBtn.isEnabled()) btnContainer.setColor(BTN_UNBLOCK_BASE); 
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { setBackground(HOVER_BG); }
                @Override public void mouseExited(MouseEvent e) { setBackground(ITEM_BG); }
            });

            add(avt, BorderLayout.WEST);
            add(centerPanel, BorderLayout.CENTER);
        }
        
        public String getName() { return name; }
        
        public void setUnblockCallback(ActionListener listener) {
            for (ActionListener al : unblockBtn.getActionListeners()) {
                 unblockBtn.removeActionListener(al);
            }
            unblockBtn.addActionListener(listener);
        }
        
        public void setUnblockButtonEnabled(boolean enabled) {
            unblockBtn.setEnabled(enabled);
            btnContainer.setColor(enabled ? BTN_UNBLOCK_BASE : TEXT_HINT);
            unblockBtn.setText(enabled ? "Unblock" : "...");
        }
    }
}
