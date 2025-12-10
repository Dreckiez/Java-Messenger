package components;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import models.GroupMemberModel;
import services.UserAdminService;
import utils.UserSession;

public class GroupMembersDialog extends JDialog {
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private UserAdminService userService;
    private Long groupId;
    private JLabel titleLabel;
    private String mode;

    public GroupMembersDialog(Window parent, Long groupId, String groupName, String mode) {
        super(parent, (mode.equals("ADMIN") ? "Admins of: " : "Members of: ") + groupName, ModalityType.APPLICATION_MODAL);
        this.groupId = groupId;
        this.mode = mode; // Lưu lại mode
        this.userService = new UserAdminService();

        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 1. Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        
        titleLabel = new JLabel(mode.equals("ADMIN") ? "Admin List" : "Member List");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Table
        String[] columns = {"ID", "Username", "Full Name", "Role", "Status", "Joined At"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        memberTable = new JTable(tableModel);
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(new EmptyBorder(0, 15, 0, 15));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Footer
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
        memberTable.setRowHeight(40);
        memberTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        memberTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        memberTable.setShowVerticalLines(false);
        memberTable.setGridColor(new Color(240, 240, 240));
        
        // 1. Tạo Renderer căn giữa chung cho các cột thường
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Áp dụng cho ID, Username, FullName, JoinedAt
        memberTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); 
        memberTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); 
        memberTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); 
        memberTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

        // 2. Custom Renderer cho Role (ADMIN màu đỏ)
        memberTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String role = (String) value;
                setHorizontalAlignment(JLabel.CENTER);
                if ("ADMIN".equalsIgnoreCase(role)) {
                    setForeground(new Color(220, 38, 38)); // Red
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setForeground(Color.BLACK);
                }
                return c;
            }
        });

        // 3. Custom Renderer cho Status (Online xanh)
        memberTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String status = (String) value;
                setHorizontalAlignment(JLabel.CENTER);
                if ("Online".equals(status)) setForeground(new Color(34, 197, 94));
                else setForeground(Color.GRAY);
                return c;
            }
        });
    }

    private void loadData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<List<GroupMemberModel>, Void>() {
            @Override
            protected List<GroupMemberModel> doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                
                // 👇 Logic rẽ nhánh dựa trên mode
                if ("ADMIN".equals(mode)) {
                    return userService.getGroupAdmins(token, groupId);
                } else {
                    return userService.getGroupMembers(token, groupId);
                }
            }

            @Override
            protected void done() {
                try {
                    List<GroupMemberModel> members = get();
                    if (members != null) {
                        // Cập nhật số lượng lên tiêu đề
                        String type = mode.equals("ADMIN") ? "Admins" : "Members";
                        titleLabel.setText(type + " List (" + members.size() + ")");
                        
                        for (GroupMemberModel m : members) {
                            tableModel.addRow(new Object[]{
                                m.getUserId(),
                                m.getUsername(),
                                m.getFullName(),
                                m.getGroupRole(),
                                m.isOnline() ? "Online" : "Offline",
                                m.getJoinedAt()
                            });
                        }
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