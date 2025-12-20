package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import utils.UserSession; // Đảm bảo bạn đã có class này để lấy Token

public class ProfilePass extends JPanel {
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    private boolean editMode = false;
    private JButton editBtn;
    private JPanel fieldsPanel;
    private JPanel actionPanel;
    
    // --- API CONFIG ---
    private static final String API_URL = "http://localhost:8080/api/chat/user/profile/change-password";

    // --- COLORS ---
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private final Color INPUT_BG = new Color(226, 232, 240); 
    private final Color BTN_BLUE = new Color(59, 130, 246);
    private final Color BTN_BLUE_HOVER = new Color(37, 99, 235);

    public ProfilePass() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)),
                new EmptyBorder(30, 40, 30, 40)));

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
        fieldsPanel.setVisible(false);
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

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(INPUT_BG);
        field.setBorder(null);
        return field;
    }

    private JPanel wrapInRoundedPanel(JComponent component) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    // --- BUTTONS ---
    private JButton createEditButton() {
        JButton btn = new JButton("Edit");
        styleButton(btn, Color.WHITE, BTN_BLUE, BTN_BLUE, Color.WHITE, true);
        return btn;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, BTN_BLUE, Color.WHITE, BTN_BLUE_HOVER, Color.WHITE, false);
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, new Color(241, 245, 249), TEXT_SECONDARY, new Color(226, 232, 240), TEXT_SECONDARY, false);
        return btn;
    }

    private void styleButton(JButton btn, Color bg, Color fg, Color hoverBg, Color hoverFg, boolean hasBorder) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(hasBorder ? 80 : 160, 40)); // Tăng chiều rộng nút change pass

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverBg);
                btn.setForeground(hoverFg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
                btn.setForeground(fg);
            }
        });

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                
                if (hasBorder) {
                    g2.setColor(btn.getForeground());
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 12, 12);
                }
                super.paint(g2, c);
                g2.dispose();
            }
        });
    }

    // --- LOGIC GỌI API ---

    private void toggleEditMode() {
        editMode = !editMode;
        fieldsPanel.setVisible(editMode);
        actionPanel.setVisible(editMode);
        editBtn.setText(editMode ? "Cancel" : "Edit");

        if (!editMode) {
            clearFields();
        }
        revalidate();
        repaint();
    }

    private void saveChanges() {
        String currentPass = new String(currentPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        // 1. Validate
        if (currentPass.isEmpty()) {
            showError("Please enter your current password.");
            return;
        }
        if (newPass.isEmpty()) {
            showError("Please enter a new password.");
            return;
        }
        if (newPass.length() < 6) {
            showError("Password must be at least 6 characters.");
            return;
        }
        if (!newPass.equals(confirmPass)) {
            showError("New passwords do not match.");
            return;
        }
        if (currentPass.equals(newPass)) {
            showError("New password cannot be the same as old password.");
            return;
        }

        // 2. Call API via SwingWorker
        actionPanel.setEnabled(false); // Disable buttons
        
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                JSONObject payload = new JSONObject();
                payload.put("oldPassword", currentPass);
                payload.put("newPassword", newPass);
                payload.put("confirmPassword", confirmPass);

                // Lấy token từ Session
                String token = UserSession.getUser() != null ? UserSession.getUser().getToken() : "";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return "SUCCESS";
                } else {
                    // Cố gắng đọc lỗi từ body nếu có
                    try {
                        JSONObject errJson = new JSONObject(response.body());
                        return errJson.optString("message", "Change password failed.");
                    } catch (Exception e) {
                        return "Error: " + response.statusCode();
                    }
                }
            }

            @Override
            protected void done() {
                actionPanel.setEnabled(true);
                try {
                    String result = get();
                    if ("SUCCESS".equals(result)) {
                        JOptionPane.showMessageDialog(ProfilePass.this, 
                            "Password changed successfully! Please login with your new password.", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        toggleEditMode(); // Đóng form
                    } else {
                        showError(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Connection error: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void cancelEdit() {
        clearFields();
        toggleEditMode();
    }
    
    private void clearFields() {
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void refreshData() {
        if (editMode) toggleEditMode();
    }
}