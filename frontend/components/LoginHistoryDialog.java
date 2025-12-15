package components;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import models.LoginHistoryResponse; // Import class wrapper mới
import models.LoginRecord;
import services.UserAdminService;
import utils.UserSession;

public class LoginHistoryDialog extends JDialog {
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private UserAdminService userService;
    private Long targetUserId;
    
    // Labels thống kê
    private JLabel lblTotal, lblSuccess, lblFailed;

    public LoginHistoryDialog(Window parent, Long userId, String username) {
        super(parent, "Login History - " + username, ModalityType.APPLICATION_MODAL);
        this.targetUserId = userId;
        this.userService = new UserAdminService();

        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // === 1. HEADER & STATS ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 15, 10, 15));

        // Title
        JLabel title = new JLabel("Login Activity: " + username);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(30, 41, 59));
        headerPanel.add(title, BorderLayout.NORTH);

        // Stats Row (Hiển thị số liệu tổng quan)
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblTotal = createStatLabel("Total: ...", Color.BLACK);
        lblSuccess = createStatLabel("Success: ...", new Color(34, 197, 94));
        lblFailed = createStatLabel("Failed: ...", new Color(239, 68, 68));

        statsPanel.add(lblTotal);
        statsPanel.add(lblSuccess);
        statsPanel.add(lblFailed);
        
        headerPanel.add(statsPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // === 2. TABLE ===
        String[] columns = {"ID", "Time", "Status"}; // Bỏ cột User vì đang xem của 1 người
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        historyTable = new JTable(tableModel);
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(new EmptyBorder(0, 15, 0, 15));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // === 3. FOOTER ===
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(241, 245, 249));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load Data
        loadData();
    }

    private void styleTable() {
        historyTable.setRowHeight(40);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        historyTable.setShowVerticalLines(false);
        historyTable.setGridColor(new Color(240, 240, 240));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        historyTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        historyTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Time

        // Custom Renderer cho Status
        historyTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String status = (String) value;
                setHorizontalAlignment(JLabel.CENTER);
                if ("Success".equals(status)) {
                    setForeground(new Color(34, 197, 94)); // Green
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setForeground(new Color(239, 68, 68)); // Red
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                return c;
            }
        });
    }

    private void loadData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Gọi SwingWorker trả về LoginHistoryResponse (Wrapper)
        new SwingWorker<LoginHistoryResponse, Void>() {
            @Override
            protected LoginHistoryResponse doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                // Gọi service với ID cụ thể
                return userService.getLoginHistory(token, targetUserId, null, null, null);
            }

            @Override
            protected void done() {
                try {
                    LoginHistoryResponse response = get();
                    if (response != null) {
                        // 1. Cập nhật số liệu thống kê
                        lblTotal.setText("Total: " + response.getTotal());
                        lblSuccess.setText("Success: " + response.getCountSuccess());
                        lblFailed.setText("Failed: " + response.getCountFailed());

                        // 2. Cập nhật bảng
                        List<LoginRecord> records = response.getRecords();
                        for (LoginRecord r : records) {
                            tableModel.addRow(new Object[]{
                                r.getId(),
                                r.getFormattedTime(), // Dùng hàm format mới trong Model
                                r.isSuccessful() ? "Success" : "Failed"
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(LoginHistoryDialog.this, "Error loading history");
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(color);
        return lbl;
    }
}