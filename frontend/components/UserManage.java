package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import models.User;
import screens.DashboardScreen;
import utils.StatusCellRenderer;

public class UserManage extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField nameFilter;
    private JComboBox<String> statusFilter;
    private JComboBox<String> sortByBox;
    private JSpinner friendCountFilter, activityCountFilter;
    private DashboardScreen screen;
    private JTextField dateFilter;

    public UserManage(DashboardScreen dashboard) {
        this.screen = dashboard;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionButtonsPanel.setBackground(Color.WHITE);

        JButton addBtn = createActionButton("Add", new Color(34, 197, 94));
        JButton editBtn = createActionButton("Edit", new Color(59, 130, 246));
        JButton deleteBtn = createActionButton("Delete", new Color(239, 68, 68));

        addBtn.addActionListener(e -> showAddUserDialog());
        editBtn.addActionListener(e -> showEditUserDialog());
        deleteBtn.addActionListener(e -> deleteSelectedUser());

        actionButtonsPanel.add(addBtn);
        actionButtonsPanel.add(editBtn);
        actionButtonsPanel.add(deleteBtn);

        topPanel.add(actionButtonsPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // === CENTER: Filters and Table ===
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);

        // Filters Panel
        JPanel filtersPanel = createFiltersPanel();
        centerPanel.add(filtersPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Username", "Full Name", "Email", "Status", "Friends", "Date Joined" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 12));
        userTable.setRowHeight(35);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        userTable.getTableHeader().setBackground(new Color(241, 245, 249));
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        userTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // === BOTTOM: Row Actions ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        JButton lockBtn = createActionButton("Lock", new Color(251, 146, 60));
        JButton unlockBtn = createActionButton("Unlock", new Color(34, 197, 94));
        JButton resetPwdBtn = createActionButton("Reset Password", new Color(168, 85, 247));
        JButton loginHistoryBtn = createActionButton("Login History", new Color(59, 130, 246));
        JButton friendListBtn = createActionButton("Friends List", new Color(20, 184, 166));

        lockBtn.addActionListener(e -> lockSelectedUser());
        unlockBtn.addActionListener(e -> unlockSelectedUser());
        resetPwdBtn.addActionListener(e -> resetPasswordForSelectedUser());
        loginHistoryBtn.addActionListener(e -> showLoginHistory());
        friendListBtn.addActionListener(e -> showFriendList());

        bottomPanel.add(lockBtn);
        bottomPanel.add(unlockBtn);
        bottomPanel.add(resetPwdBtn);
        bottomPanel.add(loginHistoryBtn);
        bottomPanel.add(friendListBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load sample data
        loadUserData();
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

        // Row 1: Name, Email, Status
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Search:"), gbc);

        // Create a sub-panel that holds JComboBox + JTextField
        JPanel comboTextPanel = new JPanel(new BorderLayout(5, 0));

        String[] options = { "All", "Username", "Name", "Email" };
        JComboBox<String> filterType = new JComboBox<>(options);

        JTextField filterText = new JTextField(15);

        comboTextPanel.add(filterType, BorderLayout.WEST);
        comboTextPanel.add(filterText, BorderLayout.CENTER);

        // Place the combined panel into the main layout
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(comboTextPanel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Sort By:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        sortByBox = new JComboBox<>(
                new String[] { "Name (A-Z)", "Name (Z-A)", "Date Created (Latest)", "Date Created (Oldest)" });
        sortByBox.addActionListener(e -> sortGroups());
        panel.add(sortByBox, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0;
        panel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.5;
        statusFilter = new JComboBox<>(new String[] { "All", "Active", "Locked" });
        panel.add(statusFilter, gbc);

        // Row 2: Friend count, Activity count, Date
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Friends Count (from):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        friendCountFilter = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        panel.add(friendCountFilter, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Date Joined (from):"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5;
        dateFilter = new JTextField("yyyy-MM-dd");
        dateFilter.setForeground(Color.GRAY);
        panel.add(dateFilter, gbc);

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
        btn.setPreferredSize(new Dimension(150, 32));

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

    private void loadUserData() {
        Object[][] sampleData = {
                { 1, "John Doe", "john_doe", "john@example.com", "Active", 45, "2024-01-15" },
                { 2, "Jane Smith", "jane_smith", "jane@example.com", "Active", 78, "2024-02-20" },
                { 3, "Admin User", "admin", "admin@example.com", "Active", 120, "2023-12-10" },
                { 4, "Bob Wilson", "bob_wilson", "bob@example.com", "Locked", 23, "2024-03-05" },
                { 5, "Alice Brown", "alice_brown", "alice@example.com", "Active", 92, "2024-01-28" }
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }

    private void sortGroups() {
        String sortOption = (String) sortByBox.getSelectedItem();
        JOptionPane.showMessageDialog(this, "Sort By: " + sortOption);
    }

    private void applyFilters() {
        JOptionPane.showMessageDialog(this, "Filtering...");
    }

    private void resetFilters() {
        nameFilter.setText("");
        sortByBox.setSelectedIndex(0);
        statusFilter.setSelectedIndex(0);
        friendCountFilter.setValue(0);
        activityCountFilter.setValue(0);
        dateFilter.setText("yyyy-MM-dd");
        dateFilter.setForeground(Color.GRAY);
    }

    private void showAddUserDialog() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        UserForm dialog = new UserForm(parent, "Add New User", null, false);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            JOptionPane.showMessageDialog(this, "User created successfully!");
        }
    }

    private void showEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user!");
            return;
        }

        User test_User = new User("abc", "abc");
        test_User.setUserInfo(1, "dreckiez", null, "USER", "", "HIDDEN", null, "abc@xyz.com", "Mike", "Hung");

        Window parent = SwingUtilities.getWindowAncestor(this);

        UserForm dialog = new UserForm(parent, "Edit User", test_User, true);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            // test_User.(dialog.getUsername());
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this user?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
        }
    }

    private void lockSelectedUser() {
        updateUserStatus("Locked");
    }

    private void unlockSelectedUser() {
        updateUserStatus("Active");
    }

    private void updateUserStatus(String status) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user!");
            return;
        }
        tableModel.setValueAt(status, selectedRow, 4);
    }

    private void resetPasswordForSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user!");
            return;
        }
        JOptionPane.showMessageDialog(this, "Password has been reset successfully!");
    }

    private void showLoginHistory() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user!");
            return;
        }
        screen.showDashboard("history");
    }

    private void showFriendList() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user!");
            return;
        }
        String username = (String) userTable.getValueAt(selectedRow, 2);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Friends List - " + username, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        // Members table
        String[] columns = { "ID", "Username", "Name", "Email", "Role", "Friend Since" };
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
}
