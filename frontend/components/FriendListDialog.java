package components;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import models.FriendModel;
import services.UserAdminService;
import utils.UserSession;

public class FriendListDialog extends JDialog {
    private JTable friendTable;
    private DefaultTableModel tableModel;
    private UserAdminService userService;
    private Long targetUserId;
    private JLabel titleLabel;

    public FriendListDialog(Window parent, Long userId, String username) {
        super(parent, "Friends List - " + username, ModalityType.APPLICATION_MODAL);
        this.targetUserId = userId;
        this.userService = new UserAdminService();

        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 1. Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        
        titleLabel = new JLabel("Friends List");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);

        // 2. Table
        String[] columns = {"ID", "Username", "Full Name", "Status", "Friend Since"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        friendTable = new JTable(tableModel);
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(friendTable);
        scrollPane.setBorder(new EmptyBorder(0, 15, 0, 15));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Bottom Close Button
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
        friendTable.setRowHeight(40);
        friendTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        friendTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        friendTable.setShowVerticalLines(false);
        friendTable.setGridColor(new Color(240, 240, 240));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        friendTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        
        // Custom Status Renderer
        friendTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("Online".equals(status)) {
                    setForeground(new Color(34, 197, 94)); // Green
                } else {
                    setForeground(Color.GRAY);
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
    }

    private void loadData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<List<FriendModel>, Void>() {
            @Override
            protected List<FriendModel> doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                return userService.getFriendsList(token, targetUserId);
            }

            @Override
            protected void done() {
                try {
                    List<FriendModel> friends = get();
                    if (friends != null) {
                        titleLabel.setText("Friends List (" + friends.size() + ")"); // Cập nhật số lượng
                        for (FriendModel f : friends) {
                            tableModel.addRow(new Object[]{
                                f.getUserId(),
                                f.getUsername(),
                                f.getFullName(),
                                f.isOnline() ? "Online" : "Offline",
                                f.getMadeFriendAt()
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(FriendListDialog.this, "Error loading friends list");
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }
}