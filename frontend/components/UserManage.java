package components;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder; 
import javax.swing.border.CompoundBorder;
import models.UserManagementItemList;
import screens.DashboardScreen;
import services.UserAdminService;
import utils.StatusCellRenderer;
import utils.UserSession;

public class UserManage extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;

    // Components
    private JComboBox<String> filterTypeBox;
    private JTextField filterTextField;
    private JComboBox<String> sortByBox;
    private JComboBox<String> statusFilter;
    private JSpinner minFriendSpinner; // Greater Than
    private JSpinner maxFriendSpinner;
    private JTextField dateFilter;

    private DashboardScreen screen;
    private UserAdminService userService;
    
    // Màu sắc
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color BG_COLOR = new Color(241, 245, 249);
    private final Color TABLE_HEADER_COLOR = new Color(248, 250, 252);
    private final Color TEXT_COLOR = new Color(51, 65, 85);

    // Index của cột ẩn chứa Object User (Cột thứ 8 - tính từ 0 là index 7)
    private final int HIDDEN_DATA_COLUMN_INDEX = 7;

    public UserManage(DashboardScreen dashboard) {
        this.userService = new UserAdminService();
        this.screen = dashboard;

        setLayout(new BorderLayout(20, 20));
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // 1. HEADER
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. CENTER
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(BG_COLOR);

        contentContainer.add(createFiltersPanel());
        contentContainer.add(Box.createVerticalStrut(20));
        contentContainer.add(createTablePanel()); // <- Lưu ý logic tạo bảng ở đây

        add(contentContainer, BorderLayout.CENTER);

        // 3. BOTTOM
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    // === 1. UI CREATION ===

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(15, 23, 42));
        panel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonsPanel.setBackground(BG_COLOR);

        JButton addBtn = new ModernButton("Add User", new Color(34, 197, 94), Color.WHITE);
        JButton editBtn = new ModernButton("Edit", new Color(59, 130, 246), Color.WHITE);
        JButton deleteBtn = new ModernButton("Delete", new Color(239, 68, 68), Color.WHITE);
        
        addBtn.addActionListener(e -> showAddUserDialog());
        editBtn.addActionListener(e -> showEditUserDialog());
        deleteBtn.addActionListener(e -> deleteSelectedUser());

        buttonsPanel.add(addBtn);
        buttonsPanel.add(editBtn);
        buttonsPanel.add(deleteBtn);

        panel.add(buttonsPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createFiltersPanel() {
        RoundedPanel mainPanel = new RoundedPanel(15, Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        mainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        // Row 1
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        row1.setOpaque(false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);

        row1.add(createLabel("Search:"));
        filterTypeBox = createComboBox(new String[] { "All", "Username", "Name", "Email" });
        filterTextField = createTextField(15);
        row1.add(filterTypeBox);
        row1.add(filterTextField);

        row1.add(createLabel("Sort:"));
        sortByBox = createComboBox(new String[] { "Name (A-Z)", "Name (Z-A)", "Date (Latest)", "Date (Oldest)"});
        row1.add(sortByBox);

        row1.add(createLabel("Status:"));
        statusFilter = createComboBox(new String[] { "All", "Active", "Locked" });
        row1.add(statusFilter);
        mainPanel.add(row1);

        // Row 2
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label
        JLabel friendLabel = createLabel("Friends:");
        friendLabel.setForeground(PRIMARY_COLOR);
        row2.add(friendLabel);
        
        // Min Spinner
        row2.add(createLabel("Min:"));
        minFriendSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        minFriendSpinner.setPreferredSize(new Dimension(60, 35));
        minFriendSpinner.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        row2.add(minFriendSpinner);

        // Max Spinner
        row2.add(createLabel("Max:"));
        maxFriendSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        maxFriendSpinner.setPreferredSize(new Dimension(60, 35));
        maxFriendSpinner.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        row2.add(maxFriendSpinner);

        row2.add(Box.createHorizontalStrut(30));
        JButton filterBtn = new ModernButton("Apply Filter", PRIMARY_COLOR, Color.WHITE);
        JButton resetBtn = new ModernButton("Reset", new Color(226, 232, 240), TEXT_COLOR);

        filterBtn.addActionListener(e -> applyFilters());
        resetBtn.addActionListener(e -> resetFilters());

        row2.add(filterBtn);
        row2.add(resetBtn);
        mainPanel.add(row2);

        return mainPanel;
    }

    // 🔥 LOGIC TẠO BẢNG CÓ CỘT ẨN 🔥
    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(15, Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Thêm cột "HiddenData" vào cuối mảng
        String[] columns = { "ID", "Username", "Full Name", "Email", "Status", "Friends", "Date Joined", "HiddenData" };
        
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        userTable = new JTable(tableModel);
        styleTable(userTable);
        userTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        // Ẩn cột thứ 8 (index 7) đi - Cột chứa Object User
        userTable.getColumnModel().removeColumn(userTable.getColumnModel().getColumn(HIDDEN_DATA_COLUMN_INDEX));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(BG_COLOR);
        
        JButton lockBtn = new ModernButton("Lock Account", new Color(245, 158, 11), Color.WHITE);
        JButton unlockBtn = new ModernButton("Unlock", new Color(16, 185, 129), Color.WHITE);
        JButton resetPwdBtn = new ModernButton("Reset Password", new Color(139, 92, 246), Color.WHITE);
        JButton loginHistoryBtn = new ModernButton("Login History", new Color(100, 116, 139), Color.WHITE);
        JButton friendListBtn = new ModernButton("View Friends", new Color(14, 165, 233), Color.WHITE);
        
        lockBtn.addActionListener(e -> lockSelectedUser());
        unlockBtn.addActionListener(e -> unlockSelectedUser());
        resetPwdBtn.addActionListener(e -> resetPasswordForSelectedUser());
        loginHistoryBtn.addActionListener(e -> showLoginHistory());
        friendListBtn.addActionListener(e -> showFriendList());
        
        panel.add(lockBtn);
        panel.add(unlockBtn);
        panel.add(resetPwdBtn);
        panel.add(loginHistoryBtn);
        panel.add(friendListBtn);
        return panel;
    }

    // === 2. LOGIC LOAD DATA ===

    public void loadData() { applyFilters(); }

    private void applyFilters() {
        // 1. Kiểm tra đăng nhập
        if (UserSession.getUser() == null) return;

        String token = UserSession.getUser().getToken();
        
        // 2. Lấy giá trị từ các bộ lọc cơ bản
        String type = (String) filterTypeBox.getSelectedItem();
        String keyword = filterTextField.getText().trim();
        String sort = (String) sortByBox.getSelectedItem();
        String status = (String) statusFilter.getSelectedItem();
        
        // 3. 🔥 LOGIC MỚI: Lấy giá trị từ 2 ô Min/Max Spinner
        int minVal = (Integer) minFriendSpinner.getValue();
        int maxVal = (Integer) maxFriendSpinner.getValue();
        
        // Nếu giá trị > 0 thì mới gửi đi, ngược lại để null (không lọc)
        Integer greaterThan = (minVal > 0) ? minVal : null;
        Integer smallerThan = (maxVal > 0) ? maxVal : null;

        // 4. Gọi API
        userTable.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Biến final để dùng trong SwingWorker
        Integer finalGreater = greaterThan;
        Integer finalSmaller = smallerThan;

        new SwingWorker<List<UserManagementItemList>, Void>() {
            @Override
            protected List<UserManagementItemList> doInBackground() throws Exception {
                return userService.getUsers(token, type, keyword, sort, status, finalGreater, finalSmaller);
            }

            @Override
            protected void done() {
                try {
                    updateTableData(get());
                } catch (Exception e) {
                    e.printStackTrace();
                    // Có thể thêm log hoặc thông báo lỗi nhẹ ở đây nếu cần
                } finally {
                    userTable.setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }

    private void resetFilters() {
        // 1. Reset các ô tìm kiếm text
        if (filterTextField != null) filterTextField.setText("");
        if (filterTypeBox != null) filterTypeBox.setSelectedIndex(0);

        // 2. Reset các bộ lọc dropdown khác về mặc định
        if (sortByBox != null) sortByBox.setSelectedIndex(0);
        if (statusFilter != null) statusFilter.setSelectedIndex(0); // Về "All"

        // 3. 🔥 LOGIC MỚI: Reset 2 ô Spinner Min/Max về 0
        if (minFriendSpinner != null) minFriendSpinner.setValue(0);
        if (maxFriendSpinner != null) maxFriendSpinner.setValue(0);

        // 4. (Tùy chọn) Reset Date nếu có
        if (dateFilter != null) {
            dateFilter.setText("yyyy-MM-dd");
            dateFilter.setForeground(Color.GRAY);
        }

        // 5. Tải lại dữ liệu gốc
        applyFilters();
    }

    private void updateTableData(List<UserManagementItemList> users) {
        tableModel.setRowCount(0);
        for (UserManagementItemList u : users) {
            tableModel.addRow(new Object[]{
                u.getUserId(), 
                u.getUsername(), 
                u.getFullName(), 
                u.getEmail(),
                u.isActive() ? "Active" : "Locked",
                u.getFriendCount(),
                formatDate(u.getJoinedAt()),
                u
            });
        }
        tableModel.fireTableDataChanged();
    }
    
    // === 3. ACTIONS LOGIC ===

    private void showEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
            return;
        }

        // Lấy dữ liệu từ Model thông qua row index của View (cần convert nếu có sort)
        int modelRow = userTable.convertRowIndexToModel(selectedRow);
        
        // 🔥 Lấy Object từ cột ẩn (HIDDEN_DATA_COLUMN_INDEX = 7)
        UserManagementItemList user = (UserManagementItemList) tableModel.getValueAt(modelRow, HIDDEN_DATA_COLUMN_INDEX);
        
        Window parent = SwingUtilities.getWindowAncestor(this);
        
        // Truyền object vào form (Class UserEditForm đã được sửa để nhận object)
        UserEditForm dialog = new UserEditForm(parent, user);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            loadData();
        }
    }

    private void showAddUserDialog() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        UserCreateForm dialog = new UserCreateForm(parent, "Create New User");
        dialog.setVisible(true);
        if (dialog.isConfirmed()) loadData();
    }

    private void resetPasswordForSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) return;

        Long userId = (Long) userTable.getValueAt(selectedRow, 0);
        String username = (String) userTable.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to RESET password for user: " + username + "?", 
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    String token = UserSession.getUser().getToken();
                    return userService.resetPassword(token, userId);
                }
                @Override
                protected void done() {
                    try {
                        String newPass = get();
                        if (newPass != null) {
                            JTextArea ta = new JTextArea("New Password: " + newPass);
                            ta.setEditable(false);
                            JOptionPane.showMessageDialog(UserManage.this, ta, "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(UserManage.this, "Failed.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }.execute();
        }
    }

    // Các hàm khác giữ nguyên
    private void lockSelectedUser() { processUserStatusChange(false); }
    private void unlockSelectedUser() { processUserStatusChange(true); }
    
    private void processUserStatusChange(boolean isActive) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) return;
        Long userId = (Long) userTable.getValueAt(selectedRow, 0);
        
        // ... (Logic gọi API Lock/Unlock giữ nguyên) ...
        int confirm = JOptionPane.showConfirmDialog(this, "Confirm change status?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
             new SwingWorker<Boolean, Void>() {
                protected Boolean doInBackground() throws Exception {
                    return userService.updateUserStatus(UserSession.getUser().getToken(), userId, isActive);
                }
                protected void done() {
                    try { if(get()) loadData(); } catch(Exception e) {}
                }
             }.execute();
        }
    }

    private void deleteSelectedUser() {
        // 1. Kiểm tra chọn dòng
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Lấy thông tin từ bảng
        Long userId = (Long) userTable.getValueAt(selectedRow, 0); // Cột ID
        String username = (String) userTable.getValueAt(selectedRow, 1); // Cột Username

        // 3. Hỏi xác nhận (Rất quan trọng)
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to PERMANENTLY DELETE user: " + username + "?\nThis action cannot be undone!", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE); // Icon đỏ cảnh báo

        if (confirm == JOptionPane.YES_OPTION) {
            // Hiện loading
            userTable.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // 4. Gọi API ngầm
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    String token = UserSession.getUser().getToken();
                    return userService.deleteUser(token, userId);
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            // Xóa thành công -> Load lại dữ liệu
                            JOptionPane.showMessageDialog(UserManage.this, "User deleted successfully.");
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(UserManage.this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(UserManage.this, "Error: " + e.getMessage());
                    } finally {
                        userTable.setCursor(Cursor.getDefaultCursor());
                    }
                }
            }.execute();
        }
    }
    
    private void showLoginHistory() { 
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) return;
        Long userId = (Long) userTable.getValueAt(selectedRow, 0);
        String username = (String) userTable.getValueAt(selectedRow, 1);
        Window parent = SwingUtilities.getWindowAncestor(this);
        LoginHistoryDialog d = new LoginHistoryDialog(parent, userId, username);
        d.setVisible(true);
    }
    
    private void showFriendList() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) return;
        Long userId = (Long) userTable.getValueAt(selectedRow, 0);
        String username = (String) userTable.getValueAt(selectedRow, 1);
        Window parent = SwingUtilities.getWindowAncestor(this);
        FriendListDialog d = new FriendListDialog(parent, userId, username);
        d.setVisible(true);
    }

    // === UI HELPERS & CUSTOM CLASSES ===
    // (Giữ nguyên các class ModernButton, RoundedPanel, styleTable...)
    // Copy lại các hàm helper UI từ các version trước để file chạy được
    
    private String formatDate(String rawDate) {
        if(rawDate == null) return "";
        try { return rawDate.replace("T", " ").substring(0, 16); } catch(Exception e) { return rawDate; }
    }
    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(50);
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(241, 245, 249));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(TABLE_HEADER_COLOR);
        table.getTableHeader().setForeground(new Color(100, 116, 139));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 
    }
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(new Font("SansSerif", Font.BOLD, 13)); l.setForeground(TEXT_COLOR); return l;
    }
    private JTextField createTextField(int c) {
        JTextField t = new JTextField(c); t.setPreferredSize(new Dimension(100, 35));
        t.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(8, new Color(203, 213, 225)), new EmptyBorder(0, 10, 0, 10))); return t;
    }
    private JComboBox<String> createComboBox(String[] i) {
        JComboBox<String> c = new JComboBox<>(i); c.setPreferredSize(new Dimension(100, 35)); c.setBackground(Color.WHITE); return c;
    }
    class ModernButton extends JButton {
        private Color normalColor, hoverColor; boolean isHovered=false;
        public ModernButton(String t, Color b, Color f) { super(t); normalColor=b; hoverColor=b.darker(); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false); setOpaque(false); setForeground(f); setFont(new Font("SansSerif", Font.BOLD, 13)); setCursor(new Cursor(Cursor.HAND_CURSOR)); setPreferredSize(new Dimension(140, 38)); addMouseListener(new java.awt.event.MouseAdapter() { public void mouseEntered(java.awt.event.MouseEvent e) { isHovered=true; repaint(); } public void mouseExited(java.awt.event.MouseEvent e) { isHovered=false; repaint(); } }); }
        protected void paintComponent(Graphics g) { Graphics2D g2=(Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(isHovered?hoverColor:normalColor); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10); g2.dispose(); super.paintComponent(g); }
    }
    class RoundedPanel extends JPanel {
        private int r; private Color c;
        public RoundedPanel(int r, Color c) { this.r=r; this.c=c; setOpaque(false); }
        protected void paintComponent(Graphics g) { Graphics2D g2=(Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(c); g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r); g2.setColor(new Color(226,232,240)); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,r,r); g2.dispose(); super.paintComponent(g); }
    }
    class RoundedBorder extends AbstractBorder {
        private int r; private Color c;
        public RoundedBorder(int r, Color c) { this.r=r; this.c=c; }
        public void paintBorder(Component cmp, Graphics g, int x, int y, int w, int h) { Graphics2D g2=(Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(c); g2.drawRoundRect(x,y,w-1,h-1,r,r); g2.dispose(); }
        public Insets getBorderInsets(Component c) { return new Insets(r+1,r+1,r+2,r); }
    }
}