package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfilePass extends JPanel {
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    private boolean editMode = false;
    private JButton editBtn;
    private JPanel fieldsPanel;
    private JPanel actionPanel;
    
    // --- COLORS ---
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    
    // 🔥 UPDATED: Darker gray for input background (matches ProfileInfo)
    private final Color INPUT_BG = new Color(226, 232, 240); 
    
    private final Color BTN_BLUE = new Color(59, 130, 246);
    private final Color BTN_BLUE_HOVER = new Color(37, 99, 235);

    public ProfilePass() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)), // Softer bottom border
                new EmptyBorder(30, 40, 30, 40))); // Generous padding

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel sectionTitle = new JLabel("Change Password");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sectionTitle.setForeground(TEXT_PRIMARY);

        editBtn = createEditButton();
        editBtn.addActionListener(e -> toggleEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(editBtn, BorderLayout.EAST);

        add(headerPanel);
        add(Box.createVerticalStrut(25));

        // --- PASSWORD FIELDS ---
        fieldsPanel = createFieldsPanel();
        fieldsPanel.setVisible(false); // Hidden by default
        add(fieldsPanel);

        // --- ACTIONS ---
        actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setVisible(false);

        JButton saveBtn = createPrimaryButton("Change Password");
        saveBtn.addActionListener(e -> saveChanges());

        JButton cancelBtn = createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> cancelEdit());

        actionPanel.add(saveBtn);
        actionPanel.add(cancelBtn);

        add(Box.createVerticalStrut(25));
        add(actionPanel);
    }

    private JPanel createFieldsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 20, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;

        int row = 0;

        // Current Password
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(createLabel("Current Password"), gbc);

        gbc.gridy = row++;
        currentPasswordField = createStyledPasswordField();
        panel.add(wrapInRoundedPanel(currentPasswordField), gbc);

        // New Password
        gbc.gridy = row++;
        panel.add(createLabel("New Password"), gbc);

        gbc.gridy = row++;
        newPasswordField = createStyledPasswordField();
        panel.add(wrapInRoundedPanel(newPasswordField), gbc);

        // Confirm Password
        gbc.gridy = row++;
        panel.add(createLabel("Confirm New Password"), gbc);

        gbc.gridy = row++;
        confirmPasswordField = createStyledPasswordField();
        panel.add(wrapInRoundedPanel(confirmPasswordField), gbc);

        return panel;
    }

    // --- HELPER UI METHODS ---

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_SECONDARY);
        label.setBorder(new EmptyBorder(0, 5, 0, 0));
        return label;
    }

    // Password Field without border, transparent background
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(INPUT_BG); // Matches the new darker gray
        field.setBorder(null);
        return field;
    }

    // Wrap Component in Rounded Panel
    private JPanel wrapInRoundedPanel(JComponent component) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG); // Uses the new darker gray
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15)); // Inner padding
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    // --- BUTTONS ---

    private JButton createEditButton() {
        JButton btn = new JButton("Edit");
        // Bình thường: Nền Trắng, Chữ Xanh
        // Hover: Nền Xanh (BTN_BLUE), Chữ Trắng (Color.WHITE) -> 🔥 FIX LỖI Ở ĐÂY
        styleButton(btn, Color.WHITE, BTN_BLUE, BTN_BLUE, Color.WHITE, true);
        return btn;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        // Bình thường: Nền Xanh, Chữ Trắng
        // Hover: Nền Xanh đậm, Chữ vẫn Trắng
        styleButton(btn, BTN_BLUE, Color.WHITE, BTN_BLUE_HOVER, Color.WHITE, false);
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        // Bình thường: Nền Xám nhạt, Chữ Xám
        // Hover: Nền Xám đậm hơn chút, Chữ vẫn Xám
        styleButton(btn, new Color(241, 245, 249), TEXT_SECONDARY, new Color(226, 232, 240), TEXT_SECONDARY, false);
        return btn;
    }

    // 🔥 CẬP NHẬT: Thêm tham số hoverFg để đổi màu chữ khi di chuột
    private void styleButton(JButton btn, Color bg, Color fg, Color hoverBg, Color hoverFg, boolean hasBorder) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(hasBorder ? 80 : 140, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverBg);
                btn.setForeground(hoverFg); // 🔥 Đổi màu chữ khi hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
                btn.setForeground(fg); // 🔥 Trả về màu chữ gốc
            }
        });

        // Custom Paint cho bo tròn (Giữ nguyên)
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                
                if (hasBorder) {
                    // Khi có border (nút Edit), vẽ viền bằng màu chữ hiện tại
                    g2.setColor(btn.getForeground()); 
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, c.getWidth()-1, c.getHeight()-1, 12, 12);
                }
                
                super.paint(g2, c);
                g2.dispose();
            }
        });
    }

    // --- LOGIC (Unchanged) ---

    private void toggleEditMode() {
        editMode = !editMode;
        fieldsPanel.setVisible(editMode);
        actionPanel.setVisible(editMode);
        editBtn.setText(editMode ? "Cancel" : "Edit");

        if (!editMode) {
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        }
        revalidate(); repaint();
    }

    private void saveChanges() {
        String currentPass = new String(currentPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        if (currentPass.isEmpty()) {
            showError("Please enter your current password"); return;
        }
        if (newPass.isEmpty()) {
            showError("Please enter a new password"); return;
        }
        if (!newPass.equals(confirmPass)) {
            showError("New passwords do not match"); return;
        }
        if (newPass.length() < 6) {
            showError("Password must be at least 6 characters"); return;
        }

        JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        toggleEditMode();
    }

    private void cancelEdit() {
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        toggleEditMode();
    }
    
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public void refreshData() {
        if (editMode) toggleEditMode();
    }
}