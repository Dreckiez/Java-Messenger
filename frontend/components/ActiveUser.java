package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.StatusCellRenderer;

import java.awt.*;

public class ActiveUser extends JPanel {
    private JTable groupTable;
    private DefaultTableModel tableModel;
    private JTextField groupNameFilter;
    private JComboBox<String> sortByCombo;

    public ActiveUser() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === TOP: Title ===
        JLabel titleLabel = new JLabel("Active Users");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // === CENTER: Filters and Table ===
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);

        // Filters Panel
        JPanel filtersPanel = createFiltersPanel();
        centerPanel.add(filtersPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Username", "Name", "Last Login", "Token Expires", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        groupTable = new JTable(tableModel);
        groupTable.setFont(new Font("Arial", Font.PLAIN, 12));
        groupTable.setRowHeight(35);
        groupTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        groupTable.getTableHeader().setBackground(new Color(241, 245, 249));
        groupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        groupTable.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());

        // Set column widths
        groupTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        groupTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        groupTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        groupTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        groupTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        groupTable.getColumnModel().getColumn(5).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(groupTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Load sample data
        loadGroupData();
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

        // Row 1: Group name filter and sort
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        groupNameFilter = new JTextField(20);
        panel.add(groupNameFilter, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Sort By:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5;
        sortByCombo = new JComboBox<>(
                new String[] { "Name (A-Z)", "Name (Z-A)", "Last Login (Latest)", "Last Login (Oldest)" });
        sortByCombo.addActionListener(e -> sortGroups());
        panel.add(sortByCombo, gbc);

        // Filter and Reset buttons
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
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
        btn.setPreferredSize(new Dimension(180, 32));

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

    private void loadGroupData() {
        Object[][] sampleData = {
                { 1, "john_doe", "John Doe", "2025-11-19 13:03", "2025-11-26", "Active" },
                { 2, "jane_smith", "Jane Smith", "2025-11-17 08:15", "2025-11-18", "Active" },
                { 3, "alice_brown", "Alice Brown", "2025-11-23 20:30", "2025-11-30", "Expired" }
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }

    private void applyFilters() {
        String filterText = groupNameFilter.getText().toLowerCase().trim();

        if (filterText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a group!");
            return;
        }

        JOptionPane.showMessageDialog(this, "Name: " + filterText);
    }

    private void resetFilters() {
        groupNameFilter.setText("");
        sortByCombo.setSelectedIndex(0);
    }

    private void sortGroups() {
        String sortOption = (String) sortByCombo.getSelectedItem();
        JOptionPane.showMessageDialog(this, "Sort By: " + sortOption);
    }
}
