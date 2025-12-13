package components;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.border.*;
import models.LoginHistoryResponse;
import models.LoginRecord;
import services.UserAdminService;
import utils.UserSession;
import utils.StatusCellRenderer;

public class LoginHistory extends JPanel {
    private JTable loginTable;
    private DefaultTableModel tableModel;
    
    // 🔥 1. KHAI BÁO USER SERVICE
    private UserAdminService userService; 
    
    // Components Filter
    private JTextField userFilter;
    private JTextField dateFromFilter, dateToFilter;

    // 🔥 2. KHAI BÁO LABEL TOÀN CỤC (Để update được dữ liệu)
    private JLabel totalLabel;
    private JLabel successLabel;
    private JLabel failedLabel;

    // Màu sắc
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color BG_COLOR = new Color(241, 245, 249);
    private final Color TABLE_HEADER_COLOR = new Color(248, 250, 252);
    private final Color TEXT_COLOR = new Color(51, 65, 85);

    public LoginHistory() {
        // 🔥 3. KHỞI TẠO SERVICE
        this.userService = new UserAdminService();

        setLayout(new BorderLayout(20, 20));
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // === TOP ===
        add(createHeaderPanel(), BorderLayout.NORTH);

        // === CENTER ===
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(BG_COLOR);

        contentContainer.add(createFiltersPanel());
        contentContainer.add(Box.createVerticalStrut(20));
        contentContainer.add(createTablePanel());

        add(contentContainer, BorderLayout.CENTER);

        // === BOTTOM ===
        add(createStatsPanel(), BorderLayout.SOUTH);

        // Load Data
        loadLoginData();
    }

    // ... (createHeaderPanel, createFiltersPanel giữ nguyên) ...

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        JLabel titleLabel = new JLabel("System Login History");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(15, 23, 42));
        panel.add(titleLabel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createFiltersPanel() {
        RoundedPanel mainPanel = new RoundedPanel(15, Color.WHITE);
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        mainPanel.add(createLabel("Username:"));
        userFilter = createTextField(12);
        mainPanel.add(userFilter);

        mainPanel.add(Box.createHorizontalStrut(15));
        mainPanel.add(createLabel("From:"));
        
        // --- SỬA ĐOẠN NÀY ---
        dateFromFilter = createTextField(10);
        addPlaceholder(dateFromFilter, "yyyy-MM-dd"); // <--- Dùng Placeholder xịn
        mainPanel.add(dateFromFilter);

        mainPanel.add(Box.createHorizontalStrut(15));
        mainPanel.add(createLabel("To:"));
        
        // --- SỬA ĐOẠN NÀY ---
        dateToFilter = createTextField(10);
        addPlaceholder(dateToFilter, "yyyy-MM-dd"); // <--- Dùng Placeholder xịn
        mainPanel.add(dateToFilter);

        mainPanel.add(Box.createHorizontalStrut(30));
        JButton filterBtn = new ModernButton("Filter", PRIMARY_COLOR, Color.WHITE);
        JButton resetBtn = new ModernButton("Reset", new Color(226, 232, 240), TEXT_COLOR);

        filterBtn.addActionListener(e -> applyFilters());
        resetBtn.addActionListener(e -> resetFilters());

        mainPanel.add(filterBtn);
        mainPanel.add(resetBtn);
        return mainPanel;
    }

    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(15, Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = { "ID", "Username", "Full Name", "Login Time", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        loginTable = new JTable(tableModel);
        styleTable(loginTable);

        // Set width & Renderer
        loginTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        loginTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        try {
            StatusCellRenderer statusRenderer = new StatusCellRenderer();
            statusRenderer.setHorizontalAlignment(JLabel.CENTER); // Căn giữa Status
            loginTable.getColumnModel().getColumn(4).setCellRenderer(statusRenderer);
        } catch (Exception e) {}

        JScrollPane scrollPane = new JScrollPane(loginTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatsPanel() {
        RoundedPanel panel = new RoundedPanel(15, Color.WHITE);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 15));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // 🔥 4. KHỞI TẠO BIẾN TOÀN CỤC (Không dùng 'JLabel totalLabel =' nữa)
        totalLabel = new JLabel("Total Logins: 0");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(TEXT_COLOR);
        panel.add(totalLabel);

        successLabel = new JLabel("Success: 0");
        successLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        successLabel.setForeground(new Color(34, 197, 94));
        panel.add(successLabel);

        failedLabel = new JLabel("Failed: 0");
        failedLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        failedLabel.setForeground(new Color(239, 68, 68));
        panel.add(failedLabel);

        return panel;
    }

    // === LOGIC & DATA ===

    private void loadLoginData() {
        if (UserSession.getUser() == null) return;

        String token = UserSession.getUser().getToken();
        String username = userFilter.getText().trim();
        
        String fromDate = dateFromFilter.getText().trim();
        // Nếu text là placeholder "yyyy-MM-dd" thì coi như là rỗng
        if ("yyyy-MM-dd".equals(fromDate)) fromDate = "";
        
        String toDate = dateToFilter.getText().trim();
        // Tương tự với toDate
        if ("yyyy-MM-dd".equals(toDate)) toDate = "";

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        String finalUser = username;
        String finalFrom = fromDate;
        String finalTo = toDate;

        new SwingWorker<LoginHistoryResponse, Void>() {
            @Override
            protected LoginHistoryResponse doInBackground() throws Exception {
                // Gọi API
                return userService.getLoginHistory(token, null, finalUser, finalFrom, finalTo);
            }

            @Override
            protected void done() {
                try {
                    LoginHistoryResponse response = get();
                    if (response != null) {
                        updateTableAndStats(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Có thể bỏ dòng thông báo lỗi này nếu muốn UI sạch hơn khi chưa có dữ liệu
                    // JOptionPane.showMessageDialog(LoginHistory.this, "Error: " + e.getMessage());
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }

    private void updateTableAndStats(LoginHistoryResponse res) {
        // Update Table
        tableModel.setRowCount(0);
        for (LoginRecord r : res.getRecords()) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getUsername(),
                r.getFullName(),
                r.getFormattedTime(),
                r.isSuccessful() ? "Success" : "Failed"
            });
        }
        tableModel.fireTableDataChanged();

        // Update Stats Labels
        if (totalLabel != null) totalLabel.setText("Total Logins: " + res.getTotal());
        if (successLabel != null) successLabel.setText("Success: " + res.getCountSuccess());
        if (failedLabel != null) failedLabel.setText("Failed: " + res.getCountFailed());
    }

    private void applyFilters() {
        loadLoginData(); // Gọi lại hàm load để áp dụng filter
    }

    private void resetFilters() {
        userFilter.setText("");
        
        // Reset Date From về placeholder
        dateFromFilter.setText("yyyy-MM-dd");
        dateFromFilter.setForeground(Color.GRAY);
        
        // Reset Date To về placeholder
        dateToFilter.setText("yyyy-MM-dd");
        dateToFilter.setForeground(Color.GRAY);
        
        loadLoginData();
    }

    // === STYLE HELPERS ===

    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(45);
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
        
        // Căn giữa 4 cột đầu
        for(int i=0; i<4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 35));
        tf.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, new Color(203, 213, 225)),
            new EmptyBorder(0, 10, 0, 10)
        ));
        return tf;
    }

    // === CUSTOM CLASSES ===
    class ModernButton extends JButton {
        private Color normalColor, hoverColor; boolean isHovered=false;
        public ModernButton(String t, Color b, Color f) { super(t); normalColor=b; hoverColor=b.darker(); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false); setOpaque(false); setForeground(f); setFont(new Font("SansSerif", Font.BOLD, 13)); setCursor(new Cursor(Cursor.HAND_CURSOR)); setPreferredSize(new Dimension(120, 38)); addMouseListener(new java.awt.event.MouseAdapter() { public void mouseEntered(java.awt.event.MouseEvent e) { isHovered=true; repaint(); } public void mouseExited(java.awt.event.MouseEvent e) { isHovered=false; repaint(); } }); }
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

    // Hàm hỗ trợ tạo Placeholder (Tự động ẩn/hiện text hướng dẫn)
    private void addPlaceholder(JTextField field, String placeholderText) {
        // Set trạng thái ban đầu
        field.setText(placeholderText);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholderText)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR); // Đổi về màu chữ chính khi gõ
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholderText);
                    field.setForeground(Color.GRAY); // Đổi về màu xám nếu bỏ trống
                }
            }
        });
    }
}