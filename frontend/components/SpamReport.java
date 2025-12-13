package components;

import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import models.ReportModel;
import services.UserAdminService;
import utils.StatusCellRenderer;
import utils.UserSession;

public class SpamReport extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private UserAdminService userService;
    
    // Filter Components
    private JComboBox<String> searchTypeCombo;
    private JTextField keywordField;
    private JTextField dateFromFilter, dateToFilter;
    private JComboBox<String> sortCombo;
    // Colors
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color BG_COLOR = new Color(241, 245, 249);
    private final Color TABLE_HEADER_COLOR = new Color(248, 250, 252);
    private final Color TEXT_COLOR = new Color(51, 65, 85);

    // Index cột ẩn chứa Object Report (Giờ là cột thứ 5 vì bỏ Action)
    private final int HIDDEN_MODEL_INDEX = 5; 

    public SpamReport() {
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
        add(createBottomPanel(), BorderLayout.SOUTH);

        // Load Data
        loadReportData();
    }

    // ================= UI CREATION =================

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        JLabel titleLabel = new JLabel("Spam Reports Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(15, 23, 42));
        panel.add(titleLabel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createFiltersPanel() {
        // Sử dụng GridBagLayout
        RoundedPanel mainPanel = new RoundedPanel(15, Color.WHITE);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(15, 10, 15, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8); 

        // --- Hàng 1: Search, Sort, Date Range ---
        
        // Cột 0: Search Type (Combo)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        searchTypeCombo = createComboBox(new String[]{"Username", "Email"});
        searchTypeCombo.setPreferredSize(new Dimension(100, 35));
        mainPanel.add(searchTypeCombo, gbc);

        // Cột 1: Keyword Field
        gbc.gridx = 1; gbc.weightx = 1.0; 
        keywordField = createTextField(15);
        mainPanel.add(keywordField, gbc);

        // Cột 2: Sort Label
        gbc.gridx = 2; gbc.weightx = 0;
        mainPanel.add(createLabel("Sort:"), gbc);
        
        // Cột 3: Sort ComboBox
        gbc.gridx = 3; gbc.weightx = 0.5;
        sortCombo = createComboBox(new String[]{
            "Date (Latest)",
            "Date (Oldest)",
            "Username (A-Z)",
            "Username (Z-A)"
        });
        sortCombo.setPreferredSize(new Dimension(130, 35));
        mainPanel.add(sortCombo, gbc);

        // Cột 4: Date Range (From Label)
        gbc.gridx = 4; gbc.weightx = 0;
        mainPanel.add(createLabel("From:"), gbc);
        
        // Cột 5: Date From Input (Đã sửa)
        gbc.gridx = 5; gbc.weightx = 0.5;
        dateFromFilter = createTextField(8);
        addPlaceholder(dateFromFilter, "yyyy-MM-dd"); // <--- GỌI HÀM PLACEHOLDER
        mainPanel.add(dateFromFilter, gbc);

        // Cột 6: Date Range (To Label)
        gbc.gridx = 6; gbc.weightx = 0;
        mainPanel.add(createLabel("To:"), gbc);

        // Cột 7: Date To Input (Đã sửa)
        gbc.gridx = 7; gbc.weightx = 0.5;
        dateToFilter = createTextField(8);
        addPlaceholder(dateToFilter, "yyyy-MM-dd"); // <--- GỌI HÀM PLACEHOLDER
        mainPanel.add(dateToFilter, gbc);


        // --- Hàng 2: Buttons (Filter và Reset) ---
        
        // Cột 6 (Hàng 2): Nút Filter
        gbc.gridx = 6;
        gbc.gridy = 1; 
        gbc.gridwidth = 1; 
        gbc.weightx = 0;
        gbc.insets = new Insets(15, 5, 0, 5); 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST; 

        JButton filterBtn = new ModernButton("Filter", PRIMARY_COLOR, Color.WHITE);
        filterBtn.setPreferredSize(new Dimension(100, 38)); 
        filterBtn.addActionListener(e -> applyFilters());
        mainPanel.add(filterBtn, gbc);
        
        // Cột 7 (Hàng 2): Nút Reset
        gbc.gridx = 7;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST; 

        JButton resetBtn = new ModernButton("Reset", new Color(226, 232, 240), TEXT_COLOR);
        resetBtn.setPreferredSize(new Dimension(100, 38)); 
        resetBtn.addActionListener(e -> resetFilters());
        mainPanel.add(resetBtn, gbc);
        
        return mainPanel;
    }

    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(15, Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 🔥 BỎ CỘT ACTION
        String[] columns = { "Reported User", "Reporter", "Reason", "Time", "Status", "HiddenData" };
        
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        reportTable = new JTable(tableModel);
        styleTable(reportTable);

        // Cột Status (Index 4) dùng Renderer màu sắc
        try {
            StatusCellRenderer statusRenderer = new StatusCellRenderer();
            statusRenderer.setHorizontalAlignment(JLabel.CENTER);
            reportTable.getColumnModel().getColumn(4).setCellRenderer(statusRenderer);
        } catch (Exception e) {}

        // Ẩn cột chứa Object Model (Index 5)
        reportTable.getColumnModel().removeColumn(reportTable.getColumnModel().getColumn(HIDDEN_MODEL_INDEX));

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(BG_COLOR);

        JButton lockBtn = new ModernButton("Lock Account", new Color(239, 68, 68), Color.WHITE);
        JButton processBtn = new ModernButton("Mark Processed", new Color(34, 197, 94), Color.WHITE);
        JButton pendingBtn = new ModernButton("Mark Pending", new Color(245, 158, 11), Color.WHITE);

        lockBtn.addActionListener(e -> changeReportStatus(2));
        processBtn.addActionListener(e -> changeReportStatus(1));
        pendingBtn.addActionListener(e -> changeReportStatus(0));

        panel.add(lockBtn);
        panel.add(processBtn);
        panel.add(pendingBtn);
        
        return panel;
    }

    // ================= LOGIC & DATA =================

    private void loadReportData() {
        applyFilters(); 
    }

   private void applyFilters() {
        if (UserSession.getUser() == null) return;

        String token = UserSession.getUser().getToken();
        String type = (String) searchTypeCombo.getSelectedItem();
        String keyword = keywordField.getText().trim();
        
        String fromDate = dateFromFilter.getText().trim();
        // Nếu text là placeholder thì coi như rỗng
        if ("yyyy-MM-dd".equals(fromDate)) fromDate = "";
        
        String toDate = dateToFilter.getText().trim();
        // Nếu text là placeholder thì coi như rỗng
        if ("yyyy-MM-dd".equals(toDate)) toDate = "";

        // 🔥 Xử lý Sort
        String uiSort = (String) sortCombo.getSelectedItem();
        String backendSort = mapSortValue(uiSort);

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        String finalFrom = fromDate;
        String finalTo = toDate;

        new SwingWorker<List<ReportModel>, Void>() {
            @Override
            protected List<ReportModel> doInBackground() throws Exception {
                return userService.getReports(token, type, keyword, finalFrom, finalTo, backendSort);
            }

            @Override
            protected void done() {
                try {
                    updateTable(get());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }

    // 🔥 Hàm helper map giá trị Sort
    private String mapSortValue(String uiValue) {
        if (uiValue == null) return "-reportedAt"; // Mặc định: Ngày báo cáo mới nhất

        // Lưu ý: Backend sử dụng 'username' trong API để đại diện cho Reported User Username
        switch (uiValue) {
            case "Date (Latest)": 
                return "-reportedAt"; // Sắp xếp giảm dần theo thời gian báo cáo
            case "Date (Oldest)": 
                return "reportedAt";  // Sắp xếp tăng dần theo thời gian báo cáo
            case "Username (A-Z)": 
                return "username";    // Sắp xếp Reported User Username tăng dần (A-Z)
            case "Username (Z-A)": 
                return "-username";   // Sắp xếp Reported User Username giảm dần (Z-A)
            default: 
                return "-reportedAt";
        }
    }

    private void updateTable(List<ReportModel> list) {
        tableModel.setRowCount(0);
        for (ReportModel r : list) {
            tableModel.addRow(new Object[]{
                r.getReportedUserUsername(), // Cột 0
                r.getReporterUsername(),     // Cột 1
                r.getReason(),               // Cột 2
                r.getReportedAt(),           // Cột 3
                r.getStatus(),               // Cột 4: Status
                r                            // Cột 5: Hidden Object
            });
        }
    }

    private void resetFilters() {
        keywordField.setText("");
        searchTypeCombo.setSelectedIndex(0);
        sortCombo.setSelectedIndex(0);

        // Reset Date From về placeholder
        dateFromFilter.setText("yyyy-MM-dd");
        dateFromFilter.setForeground(Color.GRAY);

        // Reset Date To về placeholder
        dateToFilter.setText("yyyy-MM-dd");
        dateToFilter.setForeground(Color.GRAY);

        loadReportData();
        }

    // ================= ACTIONS =================

    private void lockReportedAccount() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report first!");
            return;
        }
        
        int modelRow = reportTable.convertRowIndexToModel(selectedRow);
        ReportModel report = (ReportModel) tableModel.getValueAt(modelRow, HIDDEN_MODEL_INDEX);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Lock account: " + report.getReportedUserUsername() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    String token = UserSession.getUser().getToken();
                    return userService.updateUserStatus(token, report.getReportedUserId(), false);
                }
                @Override
                protected void done() {
                    try {
                        if (get()) JOptionPane.showMessageDialog(SpamReport.this, "Account Locked!");
                        else JOptionPane.showMessageDialog(SpamReport.this, "Failed to lock.");
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }.execute();
        }
    }

    private void changeReportStatus(int status) {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report first!");
            return;
        }
        
        int modelRow = reportTable.convertRowIndexToModel(selectedRow);
        ReportModel report = (ReportModel) tableModel.getValueAt(modelRow, HIDDEN_MODEL_INDEX);

        // Map String status sang int (Giả định: 0=PENDING, 1=PROCCESSED)
        final int statusValue = status;
        
        Map<Integer, String> msg = new HashMap<>();

        msg.put(0, "PENDING");
        msg.put(1, "PROCESSED");
        msg.put(2, "LOCKED");

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Change status to " + msg.get(status) + " for report by " + report.getReporterUsername() + "?", 
                "Confirm Status Change", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    String token = UserSession.getUser().getToken();
                    return userService.updateReportStatus(
                            token, 
                            report.getReporterId(), 
                            report.getReportedUserId(), 
                            report.getReportedAtRaw(), // Dùng field raw Time
                            statusValue);
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            // Cập nhật UI ngay lập tức
                            tableModel.setValueAt(msg.get(status), selectedRow, 4); 
                            JOptionPane.showMessageDialog(SpamReport.this, "Status updated successfully!");
                            // Tải lại dữ liệu nếu cần
                            // loadReportData(); 
                        } else {
                            JOptionPane.showMessageDialog(SpamReport.this, "Failed to update status.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            }.execute();
        }
    }

    // ================= STYLES =================

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
        
        // Căn giữa toàn bộ
        for (int i = 0; i < table.getColumnCount(); i++) {
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

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setPreferredSize(new Dimension(120, 35));
        cb.setBackground(Color.WHITE);
        return cb;
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

    // Hàm hỗ trợ tạo Placeholder
private void addPlaceholder(JTextField field, String placeholderText) {
    // Set trạng thái ban đầu
    field.setText(placeholderText);
    field.setForeground(Color.GRAY);

    field.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (field.getText().equals(placeholderText)) {
                field.setText("");
                field.setForeground(TEXT_COLOR); // Đổi màu chữ về màu chính
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (field.getText().isEmpty()) {
                field.setText(placeholderText);
                field.setForeground(Color.GRAY); // Đổi màu chữ về màu mờ
            }
        }
    });
}
}