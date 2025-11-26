package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import utils.StatusCellRenderer;

public class LoginHistory extends JPanel {
    private JTable loginTable;
    private DefaultTableModel tableModel;
    private JTextField userFilter;
    private JTextField dateFromFilter, dateToFilter;

    public LoginHistory() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === TOP: Title ===
        JLabel titleLabel = new JLabel("Login History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // === CENTER: Filters and Table ===
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);

        // Filters Panel
        JPanel filtersPanel = createFiltersPanel();
        centerPanel.add(filtersPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Username", "Name", "Time", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loginTable = new JTable(tableModel);
        loginTable.setFont(new Font("Arial", Font.PLAIN, 12));
        loginTable.setRowHeight(35);
        loginTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        loginTable.getTableHeader().setBackground(new Color(241, 245, 249));
        loginTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom renderer for status column
        loginTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        // Set column widths
        loginTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        loginTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        loginTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        loginTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        loginTable.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(loginTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // === BOTTOM: Statistics ===
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);

        // Load sample data
        loadLoginData();
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

        // Row 1: User filter
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("User:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        userFilter = new JTextField(15);
        userFilter.setPreferredSize(new Dimension(200, 25));
        panel.add(userFilter, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("From:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        dateFromFilter = new JTextField("yyyy-MM-dd");
        dateFromFilter.setForeground(Color.GRAY);
        dateFromFilter.setPreferredSize(new Dimension(150, 25));
        panel.add(dateFromFilter, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0;
        panel.add(new JLabel("To:"), gbc);

        gbc.gridx = 5;
        gbc.weightx = 1;
        dateToFilter = new JTextField("yyyy-MM-dd");
        dateToFilter.setForeground(Color.GRAY);
        dateToFilter.setPreferredSize(new Dimension(150, 25));
        panel.add(dateToFilter, gbc);

        // Filter and Reset buttons
        gbc.gridx = 0;
        gbc.gridy = 1;
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

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Total logins
        JLabel totalLabel = new JLabel("Total Logged In: 156");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(totalLabel);

        // Successful
        JLabel successLabel = new JLabel("Success: 142");
        successLabel.setFont(new Font("Arial", Font.BOLD, 13));
        successLabel.setForeground(new Color(34, 197, 94));
        panel.add(successLabel);

        // Failed
        JLabel failedLabel = new JLabel("Failed: 14");
        failedLabel.setFont(new Font("Arial", Font.BOLD, 13));
        failedLabel.setForeground(new Color(239, 68, 68));
        panel.add(failedLabel);

        // Unique users
        JLabel uniqueLabel = new JLabel("Selected User: 45");
        uniqueLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(uniqueLabel);

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
        btn.setPreferredSize(new Dimension(140, 32));

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

    private void loadLoginData() {
        Object[][] sampleData = {
                { 1, "john_doe", "John Doe", "2024-11-26 09:15:23",
                        "Success" },
                { 2, "jane_smith", "Jane Smith", "2024-11-26 08:45:10",
                        "Success" },
                { 3, "admin", "Admin User", "2024-11-26 07:30:00",
                        "Success" },
                { 4, "bob_wilson", "Bob Wilson", "2024-11-25 22:15:45",
                        "Failed" },
                { 5, "alice_brown", "Alice Brown", "2024-11-25 20:30:12",
                        "Success" },
                { 6, "john_doe", "John Doe", "2024-11-25 18:45:30",
                        "Success" },
                { 7, "unknown", "Unknown", "2024-11-25 17:20:00", "Failed" },
                { 8, "jane_smith", "Jane Smith", "2024-11-25 15:10:25",
                        "Success" },
                { 9, "admin", "Admin User", "2024-11-25 14:00:00",
                        "Success" },
                { 10, "alice_brown", "Alice Brown", "2024-11-25 12:30:40",
                        "Success" }
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }

    private void applyFilters() {
        String selectedUser = (String) userFilter.getText();
        String dateFrom = dateFromFilter.getText();
        String dateTo = dateToFilter.getText();

        JOptionPane.showMessageDialog(this,
                "Filtering:\nUser: " + selectedUser +
                        "\nFrom: " + dateFrom + "\nTo: " + dateTo);
    }

    private void resetFilters() {
        userFilter.setText("");
        dateFromFilter.setText("yyyy-MM-dd");
        dateFromFilter.setForeground(Color.GRAY);
        dateToFilter.setText("yyyy-MM-dd");
        dateToFilter.setForeground(Color.GRAY);
    }
}