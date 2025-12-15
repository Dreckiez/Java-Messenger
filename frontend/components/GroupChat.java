package components;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import models.GroupChatModel;
import services.UserAdminService;
import utils.UserSession;

public class GroupChat extends JPanel {
    private JTable groupTable;
    private DefaultTableModel tableModel;
    private UserAdminService userService;
    
    // Filter Components
    private JTextField groupNameFilter;
    private JComboBox<String> sortByCombo;

    // Màu sắc chủ đạo (Modern Palette)
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color BG_COLOR = new Color(241, 245, 249);
    private final Color TABLE_HEADER_COLOR = new Color(248, 250, 252);
    private final Color TEXT_COLOR = new Color(51, 65, 85);

    public GroupChat() {
        this.userService = new UserAdminService();
        setLayout(new BorderLayout(20, 20));
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // === 1. TOP HEADER ===
        add(createHeaderPanel(), BorderLayout.NORTH);

        // === 2. CENTER CONTENT ===
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(BG_COLOR);

        // Filter Panel
        contentContainer.add(createFiltersPanel());
        contentContainer.add(Box.createVerticalStrut(20)); // Khoảng cách

        // Table Panel
        contentContainer.add(createTablePanel());

        add(contentContainer, BorderLayout.CENTER);

        // === 3. BOTTOM ACTIONS ===
        add(createBottomPanel(), BorderLayout.SOUTH);

        // Load sample data
        loadData();
    }

    // ==========================================
    //              UI CREATION
    // ==========================================

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel("Group Chat Management");
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

        // Group Name Filter
        mainPanel.add(createLabel("Search Group:"));
        groupNameFilter = createTextField(15);
        mainPanel.add(groupNameFilter);

        // Sort By
        mainPanel.add(createLabel("Sort By:"));
        sortByCombo = createComboBox(new String[] { "Name (A-Z)", "Name (Z-A)", "Date Created (Latest)", "Date Created (Oldest)" });
        mainPanel.add(sortByCombo);

        // Buttons
        mainPanel.add(Box.createHorizontalStrut(20));
        
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

        String[] columns = { "ID", "Group Name", "Members", "Admins", "Creator", "Date Created" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        groupTable = new JTable(tableModel);
        styleTable(groupTable);

        // Set column widths
        groupTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        groupTable.getColumnModel().getColumn(1).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(groupTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(BG_COLOR);

        JButton viewMembersBtn = new ModernButton("Inspect Members", new Color(59, 130, 246), Color.WHITE);
        JButton viewAdminsBtn = new ModernButton("Inspect Admins", new Color(168, 85, 247), Color.WHITE);

        viewMembersBtn.addActionListener(e -> showGroupMembers());
        viewAdminsBtn.addActionListener(e -> showGroupAdmins());

        panel.add(viewMembersBtn);
        panel.add(viewAdminsBtn);
        
        return panel;
    }

    // ==========================================
    //              LOGIC & DATA
    // ==========================================

    public void loadData() { applyFilters(); }

    private void applyFilters() {
        if (UserSession.getUser() == null) return;

        String token = UserSession.getUser().getToken();
        String keyword = groupNameFilter.getText().trim();
        String sort = (String) sortByCombo.getSelectedItem();

        // Hiện loading cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<List<GroupChatModel>, Void>() {
            @Override
            protected List<GroupChatModel> doInBackground() throws Exception {
                return userService.getGroupChats(token, keyword, sort);
            }

            @Override
            protected void done() {
                try {
                    updateTableData(get());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }

    private void updateTableData(List<GroupChatModel> groups) {
        tableModel.setRowCount(0);
        for (GroupChatModel g : groups) {
            tableModel.addRow(new Object[]{
                g.getId(),
                g.getGroupName(),
                g.getMemberCount(),
                g.getAdminCount(), // Cột mới thay cho "Admins" list
                g.getOwnerUsername(),
                g.getCreatedAt()
            });
        }
        // Ép vẽ lại
        tableModel.fireTableDataChanged();
    }

    private void resetFilters() {
        groupNameFilter.setText("");
        sortByCombo.setSelectedIndex(0);
        applyFilters();
    }

    private void showGroupMembers() {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a group first!");
            return;
        }
        String groupName = (String) groupTable.getValueAt(selectedRow, 1);
        Long groupId = (Long) groupTable.getValueAt(selectedRow, 0);
        
        Window parent = SwingUtilities.getWindowAncestor(this);
        
        // Truyền mode "MEMBER"
        GroupMembersDialog dialog = new GroupMembersDialog(parent, groupId, groupName, "MEMBER");
        dialog.setVisible(true);
    }

    private void showGroupAdmins() {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a group first!");
            return;
        }
        String groupName = (String) groupTable.getValueAt(selectedRow, 1);
        Long groupId = (Long) groupTable.getValueAt(selectedRow, 0);
        
        Window parent = SwingUtilities.getWindowAncestor(this);
        
        // Truyền mode "ADMIN"
        GroupMembersDialog dialog = new GroupMembersDialog(parent, groupId, groupName, "ADMIN");
        dialog.setVisible(true);
    }

    // --- Dialogs (Giữ nguyên logic cũ) ---
    
    private void showMembersDialog(int groupId, String groupName) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Members - " + groupName, true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        String[] columns = { "ID", "Username", "Name", "Role", "Joined" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        styleTable(table); // Tái sử dụng style căn giữa
        
        model.addRow(new Object[]{1, "john_doe", "John Doe", "Member", "2024-01-20"});
        model.addRow(new Object[]{2, "jane_smith", "Jane Smith", "Admin", "2024-01-15"});

        dialog.add(new JScrollPane(table));
        dialog.setVisible(true);
    }

    private void showAdminsDialog(int groupId, String groupName) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Admins - " + groupName, true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        String[] columns = { "ID", "Username", "Name", "Admin Since" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        styleTable(table); // Tái sử dụng style căn giữa
        
        model.addRow(new Object[]{2, "jane_smith", "Jane Smith", "2024-01-15"});

        dialog.add(new JScrollPane(table));
        dialog.setVisible(true);
    }

    // ==========================================
    //           STYLE HELPERS & CLASSES
    // ==========================================

    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(45);
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(241, 245, 249));
        table.setIntercellSpacing(new Dimension(0, 0));

        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(TABLE_HEADER_COLOR);
        table.getTableHeader().setForeground(new Color(100, 116, 139));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // 🔥 FIX: Căn giữa TOÀN BỘ CÁC CỘT (bao gồm Name và Creator)
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
        cb.setPreferredSize(new Dimension(150, 35));
        cb.setBackground(Color.WHITE);
        return cb;
    }

    // --- Custom Components ---

    class ModernButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
        private boolean isHovered = false;

        public ModernButton(String text, Color bg, Color fg) {
            super(text);
            this.normalColor = bg;
            this.hoverColor = bg.darker();
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(fg);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(150, 38));
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { isHovered = true; repaint(); }
                public void mouseExited(java.awt.event.MouseEvent evt) { isHovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isHovered ? hoverColor : normalColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(new Color(226, 232, 240));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        public RoundedBorder(int radius, Color color) { this.radius = radius; this.color = color; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(radius + 1, radius + 1, radius + 2, radius); }
    }
}