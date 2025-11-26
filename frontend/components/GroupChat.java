package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class GroupChat extends JPanel {
    private JTable groupTable;
    private DefaultTableModel tableModel;
    private JTextField groupNameFilter;
    private JComboBox<String> sortByCombo;

    public GroupChat() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === TOP: Title ===
        JLabel titleLabel = new JLabel("Group Chat");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // === CENTER: Filters and Table ===
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);

        // Filters Panel
        JPanel filtersPanel = createFiltersPanel();
        centerPanel.add(filtersPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Group Name", "Members", "Admin", "Creator", "Date Created" };
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

        // === BOTTOM: Action Buttons ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        JButton viewMembersBtn = createActionButton("Inspect Member List", new Color(59, 130, 246));
        JButton viewAdminsBtn = createActionButton("Inspect Admin List", new Color(168, 85, 247));
        JButton deleteGroupBtn = createActionButton("Delete Group", new Color(239, 68, 68));

        viewMembersBtn.addActionListener(e -> showGroupMembers());
        viewAdminsBtn.addActionListener(e -> showGroupAdmins());
        deleteGroupBtn.addActionListener(e -> deleteSelectedGroup());

        bottomPanel.add(viewMembersBtn);
        bottomPanel.add(viewAdminsBtn);
        bottomPanel.add(deleteGroupBtn);

        add(bottomPanel, BorderLayout.SOUTH);

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
        panel.add(new JLabel("Group Name:"), gbc);

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
                new String[] { "Name (A-Z)", "Name (Z-A)", "Date Created (Latest)", "Date Created (Oldest)" });
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
                { 1, "Nhóm Java Developers", 45, 3, "admin", "2024-01-15" },
                { 2, "Team Marketing", 23, 2, "jane_smith", "2024-02-20" },
                { 3, "Dự án ABC", 15, 2, "john_doe", "2024-03-10" },
                { 4, "Gia đình", 8, 1, "alice_brown", "2024-01-05" },
                { 5, "Học tập cùng nhau", 67, 5, "bob_wilson", "2023-12-20" },
                { 6, "Bóng đá Việt Nam", 120, 8, "admin", "2023-11-15" },
                { 7, "Công ty XYZ", 89, 4, "jane_smith", "2024-04-01" }
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

    private void showGroupMembers() {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a group!");
            return;
        }

        String groupName = (String) groupTable.getValueAt(selectedRow, 1);
        int groupId = (int) groupTable.getValueAt(selectedRow, 0);

        // Show dialog with members list
        showMembersDialog(groupId, groupName);
    }

    private void showGroupAdmins() {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a group!");
            return;
        }

        String groupName = (String) groupTable.getValueAt(selectedRow, 1);
        int groupId = (int) groupTable.getValueAt(selectedRow, 0);

        // Show dialog with admins list
        showAdminsDialog(groupId, groupName);
    }

    private void deleteSelectedGroup() {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a group!");
            return;
        }

        String groupName = (String) groupTable.getValueAt(selectedRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhóm '" + groupName + "'?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Group Deleted Successfully!");
        }
    }

    private void showMembersDialog(int groupId, String groupName) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Members List - " + groupName, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        // Members table
        String[] columns = { "ID", "Username", "Name", "Email", "Role", "Date Joined" };
        DefaultTableModel membersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable membersTable = new JTable(membersModel);
        membersTable.setFont(new Font("Arial", Font.PLAIN, 12));
        membersTable.setRowHeight(30);

        // Sample members data
        Object[][] sampleMembers = {
                { 1, "john_doe", "John Doe", "john@example.com", "Member", "2024-01-20" },
                { 2, "jane_smith", "Jane Smith", "jane@example.com", "Admin", "2024-01-15" },
                { 3, "alice_brown", "Alice Brown", "alice@example.com", "Member", "2024-02-10" }
        };

        for (Object[] row : sampleMembers) {
            membersModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(membersTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showAdminsDialog(int groupId, String groupName) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Admin List - " + groupName,
                true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(600, 300);
        dialog.setLocationRelativeTo(this);

        // Admins table
        String[] columns = { "ID", "Username", "Name", "Email", "Admin on" };
        DefaultTableModel adminsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable adminsTable = new JTable(adminsModel);
        adminsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        adminsTable.setRowHeight(30);

        // Sample admins data
        Object[][] sampleAdmins = {
                { 2, "jane_smith", "Jane Smith", "jane@example.com", "2024-01-15" },
                { 5, "admin", "Admin User", "admin@example.com", "2024-01-15" }
        };

        for (Object[] row : sampleAdmins) {
            adminsModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(adminsTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
