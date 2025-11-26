package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.StatusCellRenderer;

import java.awt.*;

public class SpamReport extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField usernameFilter, emailFilter;
    private JTextField dateFromFilter, dateToFilter;
    private JComboBox<String> sortByCombo;

    public SpamReport() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === TOP: Title ===
        JLabel titleLabel = new JLabel("Spam Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // === CENTER: Filters and Table ===
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);

        // Filters Panel
        JPanel filtersPanel = createFiltersPanel();
        centerPanel.add(filtersPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Reported User", "Email", "Reporter", "Reason", "Time", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 12));
        reportTable.setRowHeight(35);
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        reportTable.getTableHeader().setBackground(new Color(241, 245, 249));
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom renderer for status column
        reportTable.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());

        // Set column widths
        reportTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        reportTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        reportTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        reportTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        reportTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        reportTable.getColumnModel().getColumn(5).setPreferredWidth(130);
        reportTable.getColumnModel().getColumn(6).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // === BOTTOM: Action Buttons ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        JButton viewDetailsBtn = createActionButton("View Details", new Color(59, 130, 246));
        JButton lockAccountBtn = createActionButton("Lock", new Color(239, 68, 68));
        JButton markResolvedBtn = createActionButton("Mark as Proccessed", new Color(34, 197, 94));
        JButton dismissBtn = createActionButton("Dismiss", new Color(100, 116, 139));

        viewDetailsBtn.addActionListener(e -> viewReportDetails());
        lockAccountBtn.addActionListener(e -> lockReportedAccount());
        markResolvedBtn.addActionListener(e -> markAsResolved());
        dismissBtn.addActionListener(e -> dismissReport());

        bottomPanel.add(viewDetailsBtn);
        bottomPanel.add(lockAccountBtn);
        bottomPanel.add(markResolvedBtn);
        bottomPanel.add(dismissBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load sample data
        loadReportData();
    }

    private JPanel createFiltersPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 1: Username and Email filters
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        usernameFilter = new JTextField(15);
        panel.add(usernameFilter, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        emailFilter = new JTextField(15);
        panel.add(emailFilter, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0;
        panel.add(new JLabel("Sort By:"), gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.8;
        sortByCombo = new JComboBox<>(new String[] {
                "Date (Latest)",
                "Date (Oldest)",
                "Username (A-Z)",
                "Username (Z-A)"
        });
        sortByCombo.addActionListener(e -> sortReports());
        panel.add(sortByCombo, gbc);

        // Row 2: Date filters
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("From:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        dateFromFilter = new JTextField("yyyy-MM-dd");
        dateFromFilter.setForeground(Color.GRAY);
        dateFromFilter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (dateFromFilter.getText().equals("yyyy-MM-dd")) {
                    dateFromFilter.setText("");
                    dateFromFilter.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (dateFromFilter.getText().isEmpty()) {
                    dateFromFilter.setText("yyyy-MM-dd");
                    dateFromFilter.setForeground(Color.GRAY);
                }
            }
        });
        panel.add(dateFromFilter, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("To:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        dateToFilter = new JTextField("yyyy-MM-dd");
        dateToFilter.setForeground(Color.GRAY);
        dateToFilter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (dateToFilter.getText().equals("yyyy-MM-dd")) {
                    dateToFilter.setText("");
                    dateToFilter.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (dateToFilter.getText().isEmpty()) {
                    dateToFilter.setText("yyyy-MM-dd");
                    dateToFilter.setForeground(Color.GRAY);
                }
            }
        });
        panel.add(dateToFilter, gbc);

        // Filter and Reset buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(new Color(248, 250, 252));

        JButton filterBtn = createActionButton("Filter", new Color(59, 130, 246));
        JButton resetBtn = createActionButton("Reset", new Color(100, 116, 139));

        filterBtn.addActionListener(e -> applyFilters());
        resetBtn.addActionListener(e -> resetFilters());

        buttonPanel.add(filterBtn);
        buttonPanel.add(resetBtn);
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 32));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private void loadReportData() {
        Object[][] sampleData = {
                { 1, "spam_user123", "spam@example.com", "john_doe", "Gửi tin nhắn spam liên tục",
                        "2024-11-26 10:30:00", "Pending" },
                { 2, "fake_account", "fake@example.com", "jane_smith", "Tài khoản giả mạo", "2024-11-25 15:20:00",
                        "Pending" },
                { 3, "bob_wilson", "bob@example.com", "alice_brown", "Nội dung không phù hợp", "2024-11-25 09:15:00",
                        "Proccessed" },
                { 4, "scammer99", "scam@example.com", "admin", "Lừa đảo tài chính", "2024-11-24 20:45:00", "Locked" },
                { 5, "harasser", "bad@example.com", "john_doe", "Quấy rối người dùng khác", "2024-11-24 14:30:00",
                        "Pending" },
                { 6, "advertiser", "ads@example.com", "jane_smith", "Quảng cáo trái phép", "2024-11-23 11:00:00",
                        "Proccessed" },
                { 7, "toxic_user", "toxic@example.com", "alice_brown", "Ngôn từ độc hại", "2024-11-23 08:20:00",
                        "Locked" }
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }

    private void applyFilters() {
        String username = usernameFilter.getText().trim();
        String email = emailFilter.getText().trim();
        String dateFrom = dateFromFilter.getText();
        String dateTo = dateToFilter.getText();

        StringBuilder filterMsg = new StringBuilder("Áp dụng bộ lọc:");
        if (!username.isEmpty())
            filterMsg.append("\nUsername: ").append(username);
        if (!email.isEmpty())
            filterMsg.append("\nEmail: ").append(email);
        if (!dateFrom.equals("yyyy-MM-dd"))
            filterMsg.append("\nFrom: ").append(dateFrom);
        if (!dateTo.equals("yyyy-MM-dd"))
            filterMsg.append("\nTo: ").append(dateTo);

        JOptionPane.showMessageDialog(this, filterMsg.toString());
    }

    private void resetFilters() {
        usernameFilter.setText("");
        emailFilter.setText("");
        dateFromFilter.setText("yyyy-MM-dd");
        dateFromFilter.setForeground(Color.GRAY);
        dateToFilter.setText("yyyy-MM-dd");
        dateToFilter.setForeground(Color.GRAY);
        sortByCombo.setSelectedIndex(0);
    }

    private void sortReports() {
        String sortOption = (String) sortByCombo.getSelectedItem();
        JOptionPane.showMessageDialog(this, "Sort By: " + sortOption);
    }

    private void viewReportDetails() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report!");
            return;
        }

        int reportId = (int) reportTable.getValueAt(selectedRow, 0);
        String reportedUser = (String) reportTable.getValueAt(selectedRow, 1);
        String reporter = (String) reportTable.getValueAt(selectedRow, 3);
        String reason = (String) reportTable.getValueAt(selectedRow, 4);
        String time = (String) reportTable.getValueAt(selectedRow, 5);

        showReportDetailsDialog(reportId, reportedUser, reporter, reason, time);
    }

    private void lockReportedAccount() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report!");
            return;
        }

        String reportedUser = (String) reportTable.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "You want to lock '" + reportedUser + "'?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setValueAt("Locked", selectedRow, 6);
            JOptionPane.showMessageDialog(this,
                    "Locked '" + reportedUser + "' successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markAsResolved() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report!");
            return;
        }

        String currentStatus = (String) reportTable.getValueAt(selectedRow, 6);
        if (currentStatus.equals("Proccessed")) {
            JOptionPane.showMessageDialog(this, "This report has been proccessed!");
            return;
        }

        tableModel.setValueAt("Pending", selectedRow, 6);
        JOptionPane.showMessageDialog(this, "This report is now proccessed!");
    }

    private void dismissReport() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Do you want to dismis this report?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "This report is now dismissed!");
        }
    }

    private void showReportDetailsDialog(int reportId, String reportedUser, String reporter, String reason,
            String time) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Report Detail #" + reportId,
                true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(createInfoLabel("Reported ID:", String.valueOf(reportId)));
        contentPanel.add(createInfoLabel("Reported User:", reportedUser));
        contentPanel.add(createInfoLabel("Reporter:", reporter));
        contentPanel.add(createInfoLabel("Time:", time));

        JPanel reasonPanel = new JPanel(new BorderLayout(5, 5));
        reasonPanel.setBackground(Color.WHITE);
        reasonPanel.add(new JLabel("Reason:"), BorderLayout.NORTH);
        JTextArea reasonArea = new JTextArea(reason);
        reasonArea.setEditable(false);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setFont(new Font("Arial", Font.PLAIN, 12));
        reasonArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        reasonPanel.add(new JScrollPane(reasonArea), BorderLayout.CENTER);
        contentPanel.add(reasonPanel);

        dialog.add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JPanel createInfoLabel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 13));
        lblLabel.setPreferredSize(new Dimension(150, 25));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 13));

        panel.add(lblLabel, BorderLayout.WEST);
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }
}
