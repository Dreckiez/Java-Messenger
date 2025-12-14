package components;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONArray;
import org.json.JSONObject;

import models.User;
import utils.ApiClient;
import utils.ApiUrl;
import utils.ImageEditor;
import utils.ImageLoader;
import utils.UserSession;
import utils.ChatSessionManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class InfoPanel extends JPanel {
    private JLabel chatNameLabel;
    private JLabel chatStatusLabel;
    private JLabel avatarLabel;
    
    private JPanel membersSection;
    private JPanel actionsSection;
    private JPanel contentPanel;
    
    private ImageEditor imageEditor;
    private boolean isGroup;
    private JSONObject currentChatData;
    
    private Runnable onChatActionCompleted;    
    // --- COLORS PALETTE ---
    private final Color TEXT_PRIMARY = new Color(15, 23, 42);    // Slate-900
    private final Color TEXT_SECONDARY = new Color(100, 116, 139); // Slate-500
    private final Color BG_HOVER = new Color(241, 245, 249);     // Slate-100
    private final Color LINE_COLOR = new Color(226, 232, 240);   // Slate-200
    private final Color INPUT_BG = new Color(243, 244, 246);     // Gray-100
    private long currentAvatarLoadId = -1;
    // Role Badge Colors
    private final Color ADMIN_BADGE_BG = new Color(254, 243, 199); // Amber-100
    private final Color ADMIN_BADGE_TEXT = new Color(180, 83, 9);  // Amber-700
    private final Color MEMBER_BADGE_BG = new Color(241, 245, 249); // Slate-100
    private final Color MEMBER_BADGE_TEXT = new Color(71, 85, 105); // Slate-600
    private final Color ONLINE_COLOR = new Color(34, 197, 94);     // Green-500
    
    // Action Buttons Colors
    private final Color BTN_DANGER = new Color(239, 68, 68);       // Red-500
    private final Color BTN_TEXT_NORMAL = new Color(51, 65, 85);   // Slate-700 (Biến bạn bị thiếu)
    
    private final Color BTN_PROMOTE_BG = new Color(219, 234, 254); // Blue-100
    private final Color BTN_PROMOTE_TEXT = new Color(37, 99, 235); // Blue-600
    private final Color BTN_KICK_BG = new Color(254, 226, 226);    // Red-100
    private final Color BTN_KICK_TEXT = new Color(220, 38, 38);    // Red-600

    // 🔥 FONTS (Biến bạn bị thiếu)
    // Sử dụng Segoe UI Emoji để đảm bảo icon hiển thị đẹp trên Windows
    private final Font EMOJI_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 14);   

    private Timer debounceTimer; // Timer để delay search
    private SwingWorker<List<User>, Void> currentSearchWorker;

    public InfoPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(340, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, LINE_COLOR));

        imageEditor = new ImageEditor();

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(createHeaderSection());
        contentPanel.add(createStyledSearchBox());
        
        membersSection = createMembersSection();
        contentPanel.add(membersSection);

        actionsSection = new JPanel();
        actionsSection.setLayout(new BoxLayout(actionsSection, BoxLayout.Y_AXIS));
        actionsSection.setBackground(Color.WHITE);
        actionsSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(actionsSection);

        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setOnChatActionCompleted(Runnable callback) {
        this.onChatActionCompleted = callback;
    }

    // ========================================================================
    // UPDATE DATA
    // ========================================================================
    public void updateInfo(JSONObject chatData) {
        if (chatData == null) return;
        this.currentChatData = chatData;

        // Lấy ID hiện tại (dùng để kiểm tra ghi đè)
        final long currentId = chatData.optLong("id", -1);
        
        // 🔥 1. Cập nhật ID cuộc trò chuyện hiện tại đang được xử lý ảnh
        this.currentAvatarLoadId = currentId; 

        // --- XÁC ĐỊNH TYPE VÀ CÁC BIẾN CƠ SỞ ---
        final String type = chatData.optString("conversationType", "PRIVATE");
        this.isGroup = chatData.has("groupConversationId") || "GROUP".equalsIgnoreCase(type);
        final boolean isGroupFinal = this.isGroup; 

        // --- LOGIC XÁC ĐỊNH TÊN, TRẠNG THÁI VÀ AVATAR ---
        String tempName;
        String tempStatus;
        String tempAvatarUrl;
        
        if (isGroupFinal) {
            tempName = chatData.has("groupName") ? chatData.getString("groupName") : chatData.optString("name", "Unknown Group");
            tempStatus = "Group Chat";
            tempAvatarUrl = chatData.optString("avatarUrl", null);
        } else {
            String partnerFirstName = chatData.optString("partnerFirstName", "");
            String partnerLastName = chatData.optString("partnerLastName", "");
            
            tempName = (partnerFirstName + " " + partnerLastName).trim();
            if (tempName.isEmpty()) {
                tempName = chatData.optString("name", "Unknown User");
            }
            
            tempAvatarUrl = chatData.optString("avatarUrl", null);
            tempStatus = chatData.optBoolean("isOnline", false) ? "Active now" : "Offline"; 
            
            // Cần gọi updatePrivateDetailsUI(chatData); ở đây nếu bạn muốn cập nhật các chi tiết khác
        }
        
        // Khai báo final
        final String name = tempName;
        final String status = tempStatus;
        final String avatarUrl = tempAvatarUrl;

        SwingUtilities.invokeLater(() -> {
            // --- CẬP NHẬT HEADER CHUNG ---
            chatNameLabel.setText(name);
            chatStatusLabel.setText(status);
            
            // 🔥 Cưỡng bức dọn dẹp và đặt Placeholder Icon
            // Điều này đảm bảo Icon Group cũ (nếu có) bị xóa và Icon chữ cái mới được vẽ.
            avatarLabel.setIcon(null); 
            avatarLabel.setIcon(createPlaceholderIcon(name, isGroupFinal, 80)); 

            // 2. Tải ảnh nếu có
            if (avatarUrl != null && !avatarUrl.isEmpty() && !"null".equals(avatarUrl)) {
                // Kiểm tra ID ngay bên trong callback của ImageLoader
                ImageLoader.loadImageAsync(avatarUrl, img -> {
                    if (currentId == InfoPanel.this.currentAvatarLoadId) { 
                        if (img != null) {
                            SwingUtilities.invokeLater(() -> avatarLabel.setIcon(imageEditor.makeCircularImage(img, 80)));
                        }
                    } else {
                        // Nếu ID không khớp, đây là một worker cũ và bị bỏ qua.
                        System.out.println("DEBUG INFO: ImageLoader callback ABORTED for old ID " + currentId);
                    }
                });
            }

            // Cập nhật Layout
            updateLayoutForType();

            // Cưỡng bức Vẽ Lại (Cần thiết cho Swing)
            InfoPanel.this.revalidate();
            InfoPanel.this.repaint();
        });
    }

    private void updateLayoutForType() {
        membersSection.removeAll();
        actionsSection.removeAll();
        
        boolean amIAdmin = checkCurrentUserRole();

        // 🔥 1. TẠO THANH TÌM KIẾM (FAKE INPUT BUTTON)
        // Phần này sẽ nằm ngay dưới Header và trên cùng của nội dung cuộn
        JPanel searchPanel = createSearchBar();

        if (isGroup) {
            // --- GROUP CHAT ---
            membersSection.setVisible(true);
            
            // A. Thêm Search Bar vào đầu tiên của Member Section
            membersSection.add(searchPanel);
            searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            // B. Hiển thị danh sách thành viên
            membersSection.add(createMemberTitle("Members"));
            
            JSONArray members = currentChatData.optJSONArray("groupMemberResponseList");
            if (members != null) {
                for (int i = 0; i < members.length(); i++) {
                    addMemberItem(members.getJSONObject(i), amIAdmin);
                }
            } else {
                JLabel emptyLbl = new JLabel("No members loaded");
                emptyLbl.setBorder(new EmptyBorder(5, 20, 5, 0));
                emptyLbl.setForeground(TEXT_SECONDARY);
                membersSection.add(emptyLbl);
            }

            // C. Các hành động chung (Group Settings)
            actionsSection.add(createSectionTitle("Group Settings"));
            
            actionsSection.add(createActionBtn("✏️   Rename Group", false, e -> confirmRenameGroup()));
            actionsSection.add(createActionBtn("🖼️   Change Avatar", false, e -> uploadGroupAvatar()));
            actionsSection.add(createActionBtn("➕   Add Members", false, e -> showAddMemberDialog()));
            actionsSection.add(Box.createVerticalStrut(10));
            actionsSection.add(createSectionTitle("Assistant"));
            actionsSection.add(createActionBtn("🤖   Ask AI Group Bot", false, e -> {
                JOptionPane.showMessageDialog(this, "AI for Group is coming soon!");
                // TODO: Mở dialog AI với context là Group Chat
            }));
            actionsSection.add(Box.createVerticalStrut(10));
            actionsSection.add(createSectionTitle("Danger Zone"));
            
            actionsSection.add(createActionBtn("🚪   Leave Group", true, e -> confirmLeaveGroup()));
            actionsSection.add(createActionBtn("🗑️   Delete Group", true, e -> confirmDelete()));

        } else {
            // --- PRIVATE CHAT ---
            membersSection.setVisible(false);
            searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            // A. Thêm Search Bar vào đầu tiên của Actions Section
            actionsSection.add(searchPanel);
            
            // B. Các hành động
            actionsSection.add(createSectionTitle("Actions"));
            actionsSection.add(createActionBtn("🤖   Ask AI Assistant", false, e -> {})); 
            
            actionsSection.add(Box.createVerticalStrut(10));
            actionsSection.add(createSectionTitle("Privacy & Support"));
            
            actionsSection.add(createActionBtn("🗑️   Delete Chat", true, e -> confirmDelete()));
            actionsSection.add(createActionBtn("🚫   Block User", true, e -> confirmBlock()));
            actionsSection.add(createActionBtn("⚠️   Report", true, e -> confirmReport()));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================================
    // 🔥 HELPER: TẠO NÚT TÌM KIẾM GIẢ LẬP INPUT
    // =========================================================================
    private JPanel createSearchBar() {
        JButton searchBtn = new JButton("🔍   Search in conversation...");
        
        // Style
        searchBtn.setFont(this.EMOJI_FONT);
        searchBtn.setForeground(new Color(150, 150, 150));
        searchBtn.setBackground(new Color(245, 247, 250));
        searchBtn.setHorizontalAlignment(SwingConstants.LEFT);
        
        searchBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchBtn.addActionListener(e -> {
            // 1. Lấy ID từ dữ liệu hiện tại của Class (biến currentChatData)
            long id = -1;
            if (currentChatData != null) {
                // Ưu tiên lấy groupConversationId hoặc privateConversationId
                if (currentChatData.has("groupConversationId")) {
                    id = currentChatData.getLong("groupConversationId");
                } else if (currentChatData.has("privateConversationId")) {
                    id = currentChatData.getLong("privateConversationId");
                } else {
                    // Fallback: lấy id thường
                    id = currentChatData.optLong("id", -1);
                }
            }

            // 2. Lấy Frame cha để hiển thị Dialog
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            
            // 3. Gọi Dialog với ID vừa lấy được
            new MessageSearchDialog(parentFrame, id, isGroup, msgJson -> {
                System.out.println("Selected Message: " + msgJson);
                // TODO: Xử lý logic nhảy tới tin nhắn tại đây
            }).setVisible(true);
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(0, 20, 15, 20)); 
        wrapper.add(searchBtn, BorderLayout.CENTER);
        
        // 🔥 QUAN TRỌNG: KHÔNG setAlignmentX ở đây nữa.
        // Để bên ngoài quyết định.
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 
        
        return wrapper;
    }
    
    // ---------------------------------------------------------
    // 🔥 NEW MEMBER ITEM RENDERER (CARD STYLE)
    // ---------------------------------------------------------
    private void addMemberItem(JSONObject memberData, boolean amIAdmin) {
        int userId = memberData.optInt("userId");
        String username = memberData.optString("username", "Unknown");
        String firstName = memberData.optString("firstName", "");
        String lastName = memberData.optString("lastName", "");
        String displayName = (firstName + " " + lastName).trim();
        if (displayName.isEmpty()) displayName = username;
        
        String role = memberData.optString("groupRole", "MEMBER");
        String avatarUrl = memberData.optString("avatarUrl", null);
        boolean isOnline = memberData.optBoolean("isOnline", false);
        
        int myId = UserSession.getUser().getId();
        boolean isMe = (userId == myId);
        boolean targetIsAdmin = "ADMIN".equalsIgnoreCase(role);
        
        boolean hasPermission = amIAdmin && !isMe;

        MemberCard card = new MemberCard(userId, displayName, avatarUrl, isOnline, isMe, targetIsAdmin, hasPermission);
        membersSection.add(card);
        membersSection.add(Box.createVerticalStrut(8));
    }

    private class MemberCard extends JPanel {
        private JPanel actionPanel;
        private JLabel arrowLabel;
        private boolean isExpanded = false;

        public MemberCard(int userId, String name, String avatarUrl, boolean isOnline, boolean isMe, boolean targetIsAdmin, boolean hasPermission) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            // Border bo tròn nhẹ
            setBorder(new javax.swing.border.LineBorder(new Color(241, 245, 249), 1, true));
            setMaximumSize(new Dimension(300, 150));
            setAlignmentX(Component.CENTER_ALIGNMENT);

            // 1. Header (Clickable)
            JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
            headerPanel.setBackground(Color.WHITE);
            headerPanel.setBorder(new EmptyBorder(12, 15, 12, 15));
            headerPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Avatar
            JLabel avt = new JLabel();
            avt.setPreferredSize(new Dimension(42, 42)); 
            if (avatarUrl != null && !avatarUrl.isEmpty() && !"null".equals(avatarUrl)) {
                ImageLoader.loadImageAsync(avatarUrl, img -> {
                    if (img != null) avt.setIcon(imageEditor.makeCircularImage(img, 42));
                });
            } else {
                avt.setIcon(createPlaceholderIcon(name, false, 42));
            }
            headerPanel.add(avt, BorderLayout.WEST);

            // Info (Tên + Role)
            JPanel infoBox = new JPanel(new GridLayout(2, 1, 0, 4));
            infoBox.setBackground(Color.WHITE);
            
            JLabel nameLbl = new JLabel(isMe ? name + " (You)" : name);
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nameLbl.setForeground(TEXT_PRIMARY);
            infoBox.add(nameLbl);

            JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            statusRow.setBackground(Color.WHITE);
            
            if (targetIsAdmin) {
                statusRow.add(createRoundedBadge("ADMIN", ADMIN_BADGE_BG, ADMIN_BADGE_TEXT));
            } else {
                statusRow.add(createRoundedBadge("MEMBER", MEMBER_BADGE_BG, MEMBER_BADGE_TEXT));
            }
            
            statusRow.add(Box.createHorizontalStrut(8));

            if (isOnline) {
                JLabel onlineDot = new JLabel("● Online");
                onlineDot.setFont(new Font("Segoe UI", Font.BOLD, 10));
                onlineDot.setForeground(ONLINE_COLOR);
                statusRow.add(onlineDot);
            }
            infoBox.add(statusRow);
            headerPanel.add(infoBox, BorderLayout.CENTER);

            // Mũi tên
            if (hasPermission) {
                arrowLabel = new JLabel("▼");
                arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                arrowLabel.setForeground(TEXT_SECONDARY);
                headerPanel.add(arrowLabel, BorderLayout.EAST);
            }

            add(headerPanel, BorderLayout.NORTH);

            // 2. Action Panel (Expandable)
            if (hasPermission) {
                actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
                actionPanel.setBackground(new Color(248, 250, 252));
                actionPanel.setVisible(false);

                // Nút chỉnh Role (Toggle)
                String roleText = targetIsAdmin ? "Demote Member" : "Make Admin";
                JButton roleBtn = createRoundedButton(roleText, BTN_PROMOTE_TEXT, BTN_PROMOTE_BG);
                roleBtn.addActionListener(e -> confirmChangeRole(userId, name, targetIsAdmin));
                
                // Nút Kick
                JButton kickBtn = createRoundedButton("Kick", BTN_KICK_TEXT, BTN_KICK_BG);
                kickBtn.addActionListener(e -> confirmKickMember(userId, name));

                actionPanel.add(roleBtn);
                actionPanel.add(kickBtn);
                add(actionPanel, BorderLayout.SOUTH);

                // Click event
                headerPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        isExpanded = !isExpanded;
                        actionPanel.setVisible(isExpanded);
                        arrowLabel.setText(isExpanded ? "▲" : "▼");
                        revalidate();
                        repaint();
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) { 
                        headerPanel.setBackground(BG_HOVER); 
                        infoBox.setBackground(BG_HOVER); 
                        statusRow.setBackground(BG_HOVER); 
                    }
                    @Override
                    public void mouseExited(MouseEvent e) { 
                        headerPanel.setBackground(Color.WHITE); 
                        infoBox.setBackground(Color.WHITE); 
                        statusRow.setBackground(Color.WHITE); 
                    }
                });
            }
        }

        private JLabel createRoundedBadge(String text, Color bg, Color fg) {
            JLabel lbl = new JLabel(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 9));
            lbl.setForeground(fg);
            lbl.setOpaque(false);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setBorder(new EmptyBorder(2, 8, 2, 8));
            return lbl;
        }

        private JButton createRoundedButton(String text, Color fg, Color bg) {
            JButton btn = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isRollover()) g2.setColor(bg.darker());
                    else g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16); // Bo tròn nút
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setForeground(fg);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(new EmptyBorder(6, 16, 6, 16));
            return btn;
        }
    }

    // ---------------------------------------------------------
    // 🔥 MODERN ICONS (NO MORE BROKEN EMOJIS)
    // ---------------------------------------------------------
    private static class ModernIcon implements Icon {
        private final String type;
        private final Color color;
        private final int size = 18;

        public ModernIcon(String type, Color color) {
            this.type = type;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.translate(x, y);

            switch (type) {
                case "EDIT": // Pencil
                    g2.drawLine(4, 14, 14, 4);
                    g2.drawLine(4, 14, 6, 16);
                    g2.drawLine(14, 4, 16, 6);
                    g2.drawLine(6, 16, 16, 6);
                    break;
                case "IMAGE": // Picture
                    g2.drawRect(2, 4, 14, 10);
                    g2.drawOval(10, 6, 2, 2);
                    g2.drawPolyline(new int[]{2, 6, 10, 16}, new int[]{12, 8, 12, 9}, 4);
                    break;
                case "ADD": // Plus User
                    g2.drawOval(4, 4, 6, 6);
                    g2.drawArc(2, 10, 10, 8, 0, 180);
                    g2.drawLine(14, 8, 14, 14);
                    g2.drawLine(11, 11, 17, 11);
                    break;
                case "TRASH": // Bin
                    g2.drawLine(4, 4, 14, 4);
                    g2.drawRect(5, 4, 8, 10);
                    g2.drawLine(7, 6, 7, 12);
                    g2.drawLine(11, 6, 11, 12);
                    break;
                case "LEAVE": // Door
                    g2.drawRect(4, 2, 10, 14);
                    g2.drawLine(14, 2, 16, 4);
                    g2.drawLine(14, 16, 16, 14);
                    break;
                case "BLOCK": // Circle Slash
                    g2.drawOval(2, 2, 14, 14);
                    g2.drawLine(4, 14, 14, 4);
                    break;
                case "REPORT": // Flag
                    g2.drawLine(4, 2, 4, 16);
                    g2.drawPolygon(new int[]{4, 14, 4}, new int[]{2, 5, 8}, 3);
                    break;
                case "AI": // Robot
                    g2.drawRect(4, 6, 10, 8);
                    g2.drawLine(6, 6, 6, 4);
                    g2.drawLine(12, 6, 12, 4);
                    g2.drawOval(3, 8, 2, 2);
                    break;
            }
            g2.dispose();
        }

        @Override public int getIconWidth() { return size; }
        @Override public int getIconHeight() { return size; }
    }

    // ---------------------------------------------------------
    // 🔥 LOGIC API
    // ---------------------------------------------------------

    private boolean checkCurrentUserRole() {
        if (currentChatData == null) return false;
        int myId = UserSession.getUser().getId();
        JSONArray members = currentChatData.optJSONArray("groupMemberResponseList");
        if (members != null) {
            for (int i = 0; i < members.length(); i++) {
                if (members.getJSONObject(i).optInt("userId") == myId) {
                    return "ADMIN".equalsIgnoreCase(members.getJSONObject(i).optString("groupRole", "MEMBER"));
                }
            }
        }
        return false;
    }

        private void showAddMemberDialog() {
            long groupId = findConversationIdToDelete();
            if (groupId == -1) return;

        // 1. Setup Dialog
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Members", true);
            dialog.setSize(420, 580);
            dialog.setLayout(new BorderLayout());
            dialog.setLocationRelativeTo(this);
            dialog.getContentPane().setBackground(Color.WHITE);

            // 2. Header Panel (Search Box)
            JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
            headerPanel.setBackground(new Color(248, 250, 252)); // Slate-50
            headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel titleLbl = new JLabel("Add People");
            titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLbl.setForeground(TEXT_PRIMARY);
            titleLbl.setBorder(new EmptyBorder(0, 0, 10, 0));

            // Search Field đẹp hơn
            JTextField searchField = new JTextField();
            searchField.setPreferredSize(new Dimension(200, 40));
            searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            searchField.setBorder(new CompoundBorder(
                    new LineBorder(LINE_COLOR, 1, true),
                    new EmptyBorder(0, 10, 0, 10)
            ));
            // Placeholder giả (Nếu bạn chưa có thư viện hỗ trợ, có thể bỏ qua hoặc dùng FocusListener)
            // searchField.setText("Search friends..."); 

            headerPanel.add(titleLbl, BorderLayout.NORTH);
            headerPanel.add(searchField, BorderLayout.CENTER);

            // 3. Result Container
            JPanel listContainer = new JPanel();
            listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
            listContainer.setBackground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(listContainer);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            // Loading Indicator
            JLabel statusLabel = new JLabel("Loading...", SwingConstants.CENTER);
            statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            statusLabel.setForeground(TEXT_SECONDARY);
            statusLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
            statusLabel.setVisible(false);
            
            // Thêm statusLabel vào dưới search field
            headerPanel.add(statusLabel, BorderLayout.SOUTH);

            dialog.add(headerPanel, BorderLayout.NORTH);
            dialog.add(scrollPane, BorderLayout.CENTER);

            // 4. Logic Search (Debounce & Empty Input)
            
            // Hàm thực hiện search
            Runnable performSearch = () -> {
                String keyword = searchField.getText().trim();
                
                // UI Loading
                statusLabel.setText("Searching...");
                statusLabel.setVisible(true);
                listContainer.removeAll();
                listContainer.repaint();

                // Cancel worker cũ nếu đang chạy
                if (currentSearchWorker != null && !currentSearchWorker.isDone()) {
                    currentSearchWorker.cancel(true);
                }

                currentSearchWorker = new SwingWorker<List<User>, Void>() {
                    @Override
                    protected List<User> doInBackground() throws Exception {
                        String token = UserSession.getUser().getToken();
                        String url = ApiUrl.GROUP_CONVERSATION + "/" + groupId + "/list-add-members";
                        
                        if (!keyword.isEmpty()) {

                            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
                            url += "?keyword=" + encodedKeyword;
                        }

                        // Gọi API lấy chuỗi JSON thô
                        String jsonRaw = ApiClient.sendGetRequestRaw(url, token);
                        
                        // Parse JSON Array -> List User
                        JSONArray jsonArray = new JSONArray(jsonRaw);
                        List<User> results = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            User u = new User();
                            u.setId(obj.getInt("userId"));
                            
                            String fName = obj.optString("firstName", "");
                            String lName = obj.optString("lastName", "");
                            String dName = (fName + " " + lName).trim();
                            if (dName.isEmpty()) dName = obj.optString("username", "Unknown");
                            
                            u.setFirstName(dName); // Tạm dùng field firstName để lưu Full Name hiển thị
                            u.setLastName(""); 
                            u.setAvatar(obj.optString("avatarUrl", ""));
                            results.add(u);
                        }
                        return results;
                    }

                    @Override
                    protected void done() {
                        statusLabel.setVisible(false); // Ẩn loading
                        if (isCancelled()) return;

                        try {
                            List<User> users = get();
                            if (users.isEmpty()) {
                                JLabel emptyLbl = new JLabel("No friends found.");
                                emptyLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                                emptyLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                                emptyLbl.setForeground(TEXT_SECONDARY);
                                emptyLbl.setBorder(new EmptyBorder(20, 0, 0, 0));
                                listContainer.add(emptyLbl);
                            } else {
                                for (User user : users) {
                                    listContainer.add(createModernUserRow(user, dialog, groupId));
                                    listContainer.add(Box.createVerticalStrut(1)); // Line separator styling
                                }
                            }
                        } catch (Exception ex) {
                            // Bỏ qua lỗi cancel, chỉ hiện lỗi thật
                            if (!(ex instanceof java.util.concurrent.CancellationException)) {
                                ex.printStackTrace();
                                statusLabel.setText("Error loading data.");
                                statusLabel.setVisible(true);
                        }
                    }
                        listContainer.revalidate();
                        listContainer.repaint();
                }
            };
                currentSearchWorker.execute();
        };

        // Timer Debounce (Chờ 400ms sau khi ngừng gõ mới search)
        debounceTimer = new Timer(400, e -> performSearch.run());
        debounceTimer.setRepeats(false);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() { debounceTimer.restart(); }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });

        // 5. Initial Load (Gọi search rỗng ngay khi mở dialog)
        SwingUtilities.invokeLater(performSearch::run);

        dialog.setVisible(true);
    }

    // --- Row hiển thị User đẹp hơn ---
    private JPanel createModernUserRow(User user, JDialog parentDialog, long groupId) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15)); // Padding rộng hơn
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Avatar
        JLabel lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(40, 40));
        lblAvatar.setIcon(createPlaceholderIcon(user.getFirstName(), false, 40));
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            ImageLoader.loadImageAsync(user.getAvatar(), img -> {
                if (img != null) lblAvatar.setIcon(imageEditor.makeCircularImage(img, 40));
            });
        }

        // Tên
        JLabel lblName = new JLabel(user.getFirstName()); // Đã gộp tên ở bước parse
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblName.setForeground(TEXT_PRIMARY);

        // Button Add
        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.setBackground(new Color(37, 99, 235)); // Blue-600
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setBorder(new EmptyBorder(6, 15, 6, 15));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect cho Button
        btnAdd.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnAdd.setBackground(new Color(29, 78, 216)); } // Darker Blue
            public void mouseExited(MouseEvent e) { btnAdd.setBackground(new Color(37, 99, 235)); }
        });

        btnAdd.addActionListener(e -> {
            executeAddMember(user, groupId, parentDialog, btnAdd);
        });

        panel.add(lblAvatar, BorderLayout.WEST);
        panel.add(lblName, BorderLayout.CENTER);
        panel.add(btnAdd, BorderLayout.EAST);

        // Tạo đường kẻ mờ dưới mỗi dòng (giống list Messenger)
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(panel, BorderLayout.CENTER);
        
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(241, 245, 249)); // Slate-100
        wrapper.add(sep, BorderLayout.SOUTH);
        
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 61));
        return wrapper;
    }

    // --- Logic gọi API Add Member ---
    private void executeAddMember(User user, long groupId, JDialog dialog, JButton btnSource) {
        
        // 1. UI LAZY UPDATE (Phản hồi ngay lập tức cho người dùng)
        
        // A. Cập nhật nút bấm trong Dialog
        btnSource.setEnabled(false);
        btnSource.setText("Added");
        btnSource.setBackground(ONLINE_COLOR); // Màu xanh lá
        btnSource.setOpaque(true);

        // B. Cập nhật danh sách thành viên ở màn hình chính (Fake Local Data)
        // Tạo JSON Object giả lập thành viên mới
        JSONObject fakeMember = new JSONObject();
        fakeMember.put("userId", user.getId());
        fakeMember.put("username", user.getFirstName()); // Tạm lấy tên hiển thị
        fakeMember.put("firstName", user.getFirstName());
        fakeMember.put("lastName", "");
        fakeMember.put("avatarUrl", user.getAvatar());
        fakeMember.put("groupRole", "MEMBER");
        fakeMember.put("isOnline", false); 

        // Lấy mảng thành viên hiện tại từ RAM
        final JSONArray currentMembers = currentChatData.optJSONArray("groupMemberResponseList");
        
        // Thêm vào mảng local
        currentMembers.put(fakeMember);

        // Vẽ lại giao diện InfoPanel ngay lập tức
        updateLayoutForType(); 

        // 2. GỌI API (Chạy ngầm - Background)
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                String url = ApiUrl.GROUP_CONVERSATION + "/" + groupId + "/members";
                
                // Body chuẩn theo yêu cầu mới: { "memberIds": [1] }
                JSONObject body = new JSONObject();
                JSONArray ids = new JSONArray();
                ids.put(user.getId());
                body.put("memberIds", ids); // <--- Key là số nhiều
                
                return ApiClient.postJSON(url, body, token);
            }

            @Override
            protected void done() {
                try {
                    JSONObject response = get();
                    
                    // Kiểm tra thành công (Giả sử status < 300 là OK)
                    if (response != null && response.optInt("httpStatus", 500) < 300) {
                        System.out.println("Synced add member to server: " + user.getId());
                        // Không cần làm gì thêm vì UI đã cập nhật từ trước rồi
                        
                        // Nếu muốn chắc chắn đồng bộ full dữ liệu mới nhất:
                        // if (onChatActionCompleted != null) onChatActionCompleted.run();
                        
                    } else {
                        // 3. ROLLBACK (Nếu API thất bại)
                        String msg = response != null ? response.optString("message") : "Connection Error";
                        
                        // Xóa thành viên fake khỏi danh sách
                        for (int i = 0; i < currentMembers.length(); i++) {
                            if (currentMembers.getJSONObject(i).optInt("userId") == user.getId()) {
                                currentMembers.remove(i);
                                break;
                            }
                        }
                        updateLayoutForType(); // Vẽ lại UI cũ (xóa người đó đi)
                        
                        // Báo lỗi cho người dùng
                        JOptionPane.showMessageDialog(dialog, "Failed to add " + user.getFirstName() + "\nError: " + msg);
                        
                        // Reset trạng thái nút
                        btnSource.setEnabled(true);
                        btnSource.setText("Add");
                        btnSource.setBackground(new Color(37, 99, 235)); // Trả về màu xanh dương
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Xử lý Rollback tương tự như trên nếu lỗi mạng
                    // ... (Code lặp lại phần Rollback)
                }
            }
        }.execute();
    }

    private void confirmKickMember(int userId, String userName) {
        long groupId = findConversationIdToDelete();
        int choice = JOptionPane.showConfirmDialog(this, 
            "Kick " + userName + " from the group?", "Confirm Kick", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (choice == JOptionPane.YES_OPTION) {
            
            // 1. Lưu backup để rollback nếu lỗi
            JSONArray originalMembers = new JSONArray(currentChatData.getJSONArray("groupMemberResponseList").toString());
            
            // 2. OPTIMISTIC UPDATE: Xóa khỏi UI ngay lập tức
            JSONArray members = currentChatData.optJSONArray("groupMemberResponseList");
            for (int i = 0; i < members.length(); i++) {
                if (members.getJSONObject(i).optInt("userId") == userId) {
                    members.remove(i);
                    break;
                }
            }
            updateLayoutForType(); // Vẽ lại UI ngay

            // 3. CALL API BACKGROUND
            String token = UserSession.getUser().getToken();
            String url = ApiUrl.GROUP_CONVERSATION + "/" + groupId + "/members"; 
            
            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() throws Exception {
                    // Tạo body: { "memberId": [5] }
                    JSONObject body = new JSONObject();
                    JSONArray arr = new JSONArray();
                    arr.put(userId);
                    body.put("memberIds", arr);
                    
                    // Giả định ApiClient.deleteJSON hỗ trợ body. 
                    // Nếu thư viện của bạn ko hỗ trợ body cho DELETE, bạn cần dùng HttpClient của Java.
                    return ApiClient.deleteJSON(url, body, token); 
                }

                @Override
                protected void done() {
                    try {
                        JSONObject response = get();
                        // Kiểm tra lỗi (Giả sử < 300 là OK)
                        if (response == null || response.optInt("httpStatus", 500) >= 300) {
                             throw new Exception(response != null ? response.optString("message") : "Unknown Error");
                        }
                        // Thành công: Không cần làm gì thêm vì UI đã xóa rồi
                        System.out.println("Kick success on server");
                        
                    } catch (Exception e) {
                        // 4. ROLLBACK: Nếu lỗi, trả lại dữ liệu cũ
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(InfoPanel.this, "Failed to kick: " + e.getMessage());
                        
                        currentChatData.put("groupMemberResponseList", originalMembers);
                        updateLayoutForType();
                    }
                }
            }.execute();
        }
    }

    private void confirmChangeRole(int userId, String userName, boolean isCurrentlyAdmin) {
        String action = isCurrentlyAdmin ? "Demote" : "Promote";
        String newRole = isCurrentlyAdmin ? "MEMBER" : "ADMIN";
        int choice = JOptionPane.showConfirmDialog(this, 
            action + " " + userName + " to " + newRole + "?", "Confirm Role Change", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) changeMemberRole(findConversationIdToDelete(), userId, newRole);
    }

    private void kickMember(long groupId, int userId) {
        String token = UserSession.getUser().getToken();
        String url = ApiUrl.GROUP_CONVERSATION + "/" + groupId + "/members/" + userId; 
        executeApiTask(() -> ApiClient.deleteJSON(url, new JSONObject(), token), "Member kicked.");
    }

    private void changeMemberRole(long groupId, int userId, String newRoleString) {
        String token = UserSession.getUser().getToken();
        String url = ApiUrl.GROUP_CONVERSATION + "/" + groupId + "/members"; 
        
        // Map Role String sang Int cho API (ADMIN=1, MEMBER=0)
        int roleInt = "ADMIN".equalsIgnoreCase(newRoleString) ? 1 : 0;

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                JSONObject body = new JSONObject();
                body.put("memberId", userId);
                body.put("groupRole", roleInt);
                
                // Gọi PATCH (Đảm bảo ApiClient đã có hàm patchJSON như hướng dẫn trước)
                return ApiClient.putJSON(url, body, token);
            }

            @Override
            protected void done() {
                try {
                    JSONObject response = get();
                    if (response != null && response.optInt("httpStatus", 500) < 300) {
                        
                        // ✅ LAZY UPDATE: Cập nhật dữ liệu trong bộ nhớ (RAM)
                        JSONArray members = currentChatData.optJSONArray("groupMemberResponseList");
                        if (members != null) {
                            for (int i = 0; i < members.length(); i++) {
                                JSONObject m = members.getJSONObject(i);
                                if (m.optInt("userId") == userId) {
                                    // Cập nhật Role mới vào JSON cục bộ
                                    m.put("groupRole", newRoleString); 
                                    break;
                                }
                            }
                        }

                        // ✅ RE-RENDER: Vẽ lại UI dựa trên dữ liệu mới (Không gọi lại API)
                        updateLayoutForType();
                        
                        JOptionPane.showMessageDialog(InfoPanel.this, "Role updated to " + newRoleString, "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                    } else {
                        String msg = response != null ? response.optString("message", "Failed") : "Unknown Error";
                        JOptionPane.showMessageDialog(InfoPanel.this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(InfoPanel.this, "System Error: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void executeApiTask(java.util.concurrent.Callable<JSONObject> task, String successMsg) {
        new SwingWorker<JSONObject, Void>() {
            @Override protected JSONObject doInBackground() throws Exception { return task.call(); }
            @Override protected void done() {
                try {
                    JSONObject res = get();
                    if (res != null && res.optInt("httpStatus", 500) < 300) {
                        JOptionPane.showMessageDialog(InfoPanel.this, successMsg);
                        if (onChatActionCompleted != null) onChatActionCompleted.run();
                    } else {
                        String msg = res != null ? res.optString("message", "Failed") : "Error";
                        JOptionPane.showMessageDialog(InfoPanel.this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) { 
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(InfoPanel.this, "System Error: " + e.getMessage());
                }
            }
        }.execute();
    }

    // ---------------------------------------------------------
    // CÁC HÀM CŨ (RENAME, UPLOAD, DELETE...)
    // ---------------------------------------------------------
    
    private void confirmRenameGroup() {
        long conversationId = findConversationIdToDelete();
        if (conversationId == -1) return;
        String currentName = chatNameLabel.getText();
        JTextField nameField = new JTextField(currentName, 20);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Enter new group name:"), BorderLayout.NORTH);
        inputPanel.add(nameField, BorderLayout.CENTER);
        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Rename Group", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty() && !newName.equals(currentName)) {
                sendRenameGroupRequest(conversationId, newName);
            }
        }
    }
    
    private void sendRenameGroupRequest(long conversationId, String newName) {
        String token = UserSession.getUser().getToken();
        String url = ApiUrl.GROUP_CONVERSATION + "/" + conversationId + "/groupName";
        new SwingWorker<JSONObject, Void>() {
            @Override protected JSONObject doInBackground() throws Exception {
                JSONObject requestBody = new JSONObject();
                requestBody.put("groupName", newName); 
                return ApiClient.putJSON(url, requestBody, token);
            }
            @Override protected void done() {
                try {
                    JSONObject response = get();
                    if (response.optInt("httpStatus", 500) < 300) {
                        chatNameLabel.setText(newName);
                        ChatSessionManager.fireGroupNameChanged(conversationId, newName);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    private void uploadGroupAvatar() {
        long conversationId = findConversationIdToDelete();
        if (conversationId == -1) return;
        System.out.println("here bro!");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Group Avatar");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG)", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToUpload = fileChooser.getSelectedFile();
            if (fileToUpload != null && fileToUpload.exists()) {
                sendAvatarUploadRequest(conversationId, fileToUpload);
                System.out.println("Here mate");
            }
        }
    }
    
    private void sendAvatarUploadRequest(long conversationId, File file) {
        String token = UserSession.getUser().getToken();
        String url = ApiUrl.GROUP_CONVERSATION + "/" + conversationId + "/avatar"; 
        avatarLabel.setIcon(createPlaceholderIcon("...", true, 80)); 
        new SwingWorker<JSONObject, Void>() {
            @Override protected JSONObject doInBackground() throws Exception {
                return ApiClient.uploadFile(url, file, token);
            }
            @Override protected void done() {
                try {
                    JSONObject response = get();
                    if (response.optInt("httpStatus", 500) < 300) {
                        String newAvatarUrl = response.optString("avatarUrl", null);
                        if (newAvatarUrl != null) {
                            ImageLoader.loadImageAsync(newAvatarUrl, img -> {
                                if (img != null) {
                                    avatarLabel.setIcon(imageEditor.makeCircularImage(img, 80));
                                    avatarLabel.repaint();
                                }
                            });
                            ChatSessionManager.fireGroupAvatarChanged(conversationId, newAvatarUrl);
                            JOptionPane.showMessageDialog(InfoPanel.this, "Avatar updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        avatarLabel.setIcon(createPlaceholderIcon(chatNameLabel.getText(), true, 80)); 
                        JOptionPane.showMessageDialog(InfoPanel.this, response.optString("message", "Something went wrong."), "Failed", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    // --- HELPER: Find IDs ---
    private long findConversationIdToDelete() {
        if (currentChatData == null) return -1;
        if (currentChatData.has("groupConversationId")) return currentChatData.getLong("groupConversationId");
        if (currentChatData.has("privateConversationId")) return currentChatData.getLong("privateConversationId");
        return currentChatData.optLong("id", -1);
    }

    private long findPartnerIdForActions() {
        if (currentChatData == null) return -1;
        if (currentChatData.has("partnerId")) return currentChatData.getLong("partnerId");
        if (currentChatData.has("otherUserId")) return currentChatData.getLong("otherUserId");
        // Fallback cho Private Chat nếu chưa parse partnerId
        User currentUser = UserSession.getUser();
        if (currentUser == null) return -1;
        int myId = currentUser.getId();
        JSONArray members = currentChatData.optJSONArray("groupMemberResponseList");
        if (members == null) members = currentChatData.optJSONArray("members"); // Support key cũ
        if (members != null) {
            for (int i = 0; i < members.length(); i++) {
                JSONObject m = members.getJSONObject(i);
                int uid = m.optInt("userId", -1);
                if (uid != -1 && uid != myId) return uid;
            }
        }
        return -1;
    }

    // --- DELETE / BLOCK / REPORT ---

    // Xóa biến listener Lazy nếu đã khai báo
    // private Consumer<Long> onOptimisticDeleteListener; // -> XÓA DÒNG NÀY

    // ...

    // ==========================================
    // 🚀 LOGIC KICK / LEAVE / DELETE (NO LAZY)
    // ==========================================

    private void confirmLeaveGroup() {
        long groupId = findConversationIdToDelete();
        if (groupId == -1) return;

        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to leave this group?", 
            "Leave Group", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
            
        if (choice == JOptionPane.YES_OPTION) {
            String token = UserSession.getUser().getToken();
            String url = ApiUrl.GROUP_CONVERSATION + "/" + groupId + "/me"; 

            // Hiển thị loading hoặc disable nút nếu muốn (Optional)
            
            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() throws Exception {
                    // Gọi API thực sự
                    return ApiClient.deleteJSON(url, new JSONObject(), token);
                }

                @Override
                protected void done() {
                    try {
                        JSONObject response = get();
                        if (response != null && response.optInt("httpStatus", 500) < 300) {
                            // ✅ THÀNH CÔNG: Mới báo ra ngoài để reload
                            JOptionPane.showMessageDialog(InfoPanel.this, "Left group successfully.");
                            if (onChatActionCompleted != null) {
                                onChatActionCompleted.run(); 
                            }
                        } else {
                            String msg = response != null ? response.optString("message") : "Unknown Error";
                            JOptionPane.showMessageDialog(InfoPanel.this, "Failed: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(InfoPanel.this, "System Error: " + e.getMessage());
                    }
                }
            }.execute();
        }
    }

    private void confirmDelete() {
        long conversationId = findConversationIdToDelete();
        if (conversationId == -1) return;

        String typeName = isGroup ? "group" : "private";
        int choice = JOptionPane.showConfirmDialog(this, 
            "Delete " + typeName + " conversation? This cannot be undone.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
            
        if (choice == JOptionPane.YES_OPTION) {
            String token = UserSession.getUser().getToken();
            String url = isGroup ? ApiUrl.GROUP_CONVERSATION + "/" + conversationId 
                                 : ApiUrl.PRIVATE_CONVERSATION + "/" + conversationId; 

            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() throws Exception {
                    return ApiClient.deleteJSON(url, new JSONObject(), token);
                }

                @Override
                protected void done() {
                    try {
                        JSONObject response = get();
                        if (response != null && response.optInt("httpStatus", 500) < 300) {
                            // ✅ THÀNH CÔNG
                            JOptionPane.showMessageDialog(InfoPanel.this, "Conversation deleted.");
                            if (onChatActionCompleted != null) {
                                onChatActionCompleted.run();
                            }
                        } else {
                            String msg = response != null ? response.optString("message") : "Error";
                            JOptionPane.showMessageDialog(InfoPanel.this, "Failed: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(InfoPanel.this, "System Error: " + e.getMessage());
                    }
                }
            }.execute();
        }
    }
    
    private void confirmBlock() {
        long userId = findPartnerIdForActions();
        if (userId == -1) return;
        int choice = JOptionPane.showConfirmDialog(this, "Block this user?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            String token = UserSession.getUser().getToken();
            String url = ApiUrl.BLOCK_USER + "/" + userId; 
            new SwingWorker<JSONObject, Void>() {
                @Override protected JSONObject doInBackground() throws Exception { return ApiClient.postJSON(url, new JSONObject(), token); }
                @Override protected void done() {
                    try {
                        if (get().optInt("httpStatus", 500) < 300) {
                            JOptionPane.showMessageDialog(InfoPanel.this, "User blocked.");
                            if (onChatActionCompleted != null) onChatActionCompleted.run();
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }.execute();
        }
    }
    
    private void confirmReport() {
        long userId = findPartnerIdForActions(); 
        if (userId == -1) return;
        JTextField reasonField = new JTextField(30);
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Reason:"), BorderLayout.NORTH);
        panel.add(reasonField, BorderLayout.SOUTH);
        if (JOptionPane.showConfirmDialog(this, panel, "Report User", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String reason = reasonField.getText().trim();
            if (!reason.isEmpty()) sendReport(userId, reason);
        }
    }
    
    private void sendReport(long userId, String reason) {
        String token = UserSession.getUser().getToken();
        String url = ApiUrl.REPORT_USER + "/" + userId; 
        new SwingWorker<JSONObject, Void>() {
            @Override protected JSONObject doInBackground() throws Exception {
                JSONObject requestBody = new JSONObject();
                requestBody.put("reason", reason);
                return ApiClient.postJSON(url, requestBody, token);
            }
            @Override protected void done() {
                try {
                    if (get().optInt("httpStatus", 500) < 300) JOptionPane.showMessageDialog(InfoPanel.this, "Report sent.");
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    // --- UI HELPERS ---
    private JPanel createHeaderSection() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(30, 20, 20, 20));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(80, 80));
        p.add(avatarLabel, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(15, 0, 5, 0);
        chatNameLabel = new JLabel("Info");
        chatNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        chatNameLabel.setForeground(TEXT_PRIMARY);
        p.add(chatNameLabel, gbc);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 0, 0);
        chatStatusLabel = new JLabel("Details");
        chatStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chatStatusLabel.setForeground(TEXT_SECONDARY);
        p.add(chatStatusLabel, gbc);
        return p;
    }
    
    private JPanel createStyledSearchBox() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(0, 20, 20, 20));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(INPUT_BG);
        box.setBorder(new EmptyBorder(8, 12, 8, 12));
        p.add(box);
        return p;
    }
    
    private JPanel createMembersSection() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(10, 0, 10, 0));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }
    
    private JLabel createSectionTitle(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(TEXT_SECONDARY);
        l.setBorder(new EmptyBorder(15, 20, 5, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel createMemberTitle(String text) {
        // 1. Tạo Panel bao bọc
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // 🔥 QUAN TRỌNG: setAlignmentX là CENTER để nó thẳng hàng dọc với các MemberCard
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Giới hạn chiều cao
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 

        // 2. Tạo Label chữ
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_SECONDARY); // Dùng lại màu cũ của bạn
        
        // Padding: Cách lề trái 20px để thẳng hàng với thanh Search và Nút
        label.setBorder(new EmptyBorder(15, 20, 5, 0)); 

        // 3. Đặt chữ vào bên TRÁI của Panel
        panel.add(label, BorderLayout.WEST);
        
        return panel;
    }
    
    private JButton createActionBtn(String text, boolean danger, java.awt.event.ActionListener action) {
        JButton b = new JButton(text);
        b.setLayout(new FlowLayout(FlowLayout.LEFT));
        b.setBackground(Color.WHITE);
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        
        // 🔥 QUAN TRỌNG: Dùng font Emoji để hiển thị icon
        b.setFont(EMOJI_FONT); 
        b.setForeground(danger ? BTN_DANGER : BTN_TEXT_NORMAL);
        
        b.addActionListener(action);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setOpaque(true); b.setBackground(BG_HOVER); }
            public void mouseExited(MouseEvent e) { b.setOpaque(false); b.setBackground(Color.WHITE); }
        });
        return b;
    }

    private ImageIcon createPlaceholderIcon(String name, boolean isGroup, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isGroup ? new Color(245, 158, 11) : new Color(59, 130, 246));
        g2.fillOval(0, 0, size, size);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
        String initial = (name != null && !name.isEmpty()) ? name.substring(0, 1).toUpperCase() : "?";
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(initial)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(initial, x, y);
        g2.dispose();
        return new ImageIcon(img);
    }
}