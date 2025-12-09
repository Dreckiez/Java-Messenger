package components;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import models.LoginRecord;
import services.UserAdminService;
import utils.UserSession;

public class LoginHistoryDialog extends JDialog {
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private UserAdminService userService;
    private Long targetUserId;
    private String targetUsername;

    public LoginHistoryDialog(Window parent, Long userId, String username) {
        super(parent, "Login History - " + username, ModalityType.APPLICATION_MODAL);
        this.targetUserId = userId;
        this.targetUsername = username;
        this.userService = new UserAdminService();

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel("Recent Login Attempts");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        historyTable = new JTable(tableModel);
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Close Button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load Data
        loadData();
    }

    private void styleTable() {
        historyTable.setRowHeight(30);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Custom Renderer cho cột Status (Màu xanh/đỏ)
        historyTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("Success".equals(status)) {
                    setForeground(new Color(34, 197, 94)); // Green
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setForeground(new Color(239, 68, 68)); // Red
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        
        // Căn giữa cột Time
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        historyTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    }

    private void loadData() {
        new SwingWorker<List<LoginRecord>, Void>() {
            @Override
            protected List<LoginRecord> doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                return userService.getLoginHistory(token, targetUserId);
            }

            @Override
            protected void done() {
                try {
                    List<LoginRecord> records = get();
                    if (records != null) {
                        for (LoginRecord r : records) {
                            tableModel.addRow(new Object[]{
                                r.getSignedInAt(),
                                r.isSuccessful() ? "Success" : "Failed"
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(LoginHistoryDialog.this, "Error loading history");
                }
            }
        }.execute();
    }
}