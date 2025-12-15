package components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.json.JSONObject;
import services.UserAdminService;
import utils.UserSession;

public class UserCreateForm extends JDialog {
    private JTextField txtUsername, txtEmail, txtPassword, txtFirstName, txtLastName, txtAddress, txtBirthday;
    private JComboBox<String> cbGender, cbRole;
    private JCheckBox chkActive;
    private boolean isConfirmed = false;
    private UserAdminService userService;

    // Màu sắc
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color BG_COLOR = new Color(248, 250, 252);

    public UserCreateForm(Window parent, String title) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        this.userService = new UserAdminService();

        setSize(500, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        headerPanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 41, 59));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- FORM CONTENT ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0); // Khoảng cách dọc
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0;

        // 1. Username (Required)
        formPanel.add(createLabel("Username *"), gbc);
        gbc.gridy++;
        txtUsername = createTextField();
        formPanel.add(txtUsername, gbc);
        gbc.gridy++;

        // 2. Email (Required)
        formPanel.add(createLabel("Email *"), gbc);
        gbc.gridy++;
        txtEmail = createTextField();
        formPanel.add(txtEmail, gbc);
        gbc.gridy++;

        // 3. Password (Required)
        formPanel.add(createLabel("Password *"), gbc);
        gbc.gridy++;
        txtPassword = createPasswordField(); 
        formPanel.add(txtPassword, gbc);
        gbc.gridy++;

        // 4. Name Group (First + Last Name)
        JPanel namePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        namePanel.setBackground(Color.WHITE);
        
        JPanel pnlFirst = new JPanel(new BorderLayout());
        pnlFirst.setBackground(Color.WHITE);
        pnlFirst.add(createLabel("First Name"), BorderLayout.NORTH);
        txtFirstName = createTextField();
        pnlFirst.add(txtFirstName, BorderLayout.CENTER);

        JPanel pnlLast = new JPanel(new BorderLayout());
        pnlLast.setBackground(Color.WHITE);
        pnlLast.add(createLabel("Last Name"), BorderLayout.NORTH);
        txtLastName = createTextField();
        pnlLast.add(txtLastName, BorderLayout.CENTER);

        namePanel.add(pnlFirst);
        namePanel.add(pnlLast);
        
        formPanel.add(namePanel, gbc);
        gbc.gridy++;

        // 5. Gender & Role & Birthday
        JPanel metaPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        metaPanel.setBackground(Color.WHITE);

        // Gender
        JPanel pnlGender = new JPanel(new BorderLayout());
        pnlGender.setBackground(Color.WHITE);
        pnlGender.add(createLabel("Gender"), BorderLayout.NORTH);
        cbGender = new JComboBox<>(new String[]{"MALE", "FEMALE", "HIDDEN"});
        styleComboBox(cbGender);
        pnlGender.add(cbGender, BorderLayout.CENTER);

        // Role
        JPanel pnlRole = new JPanel(new BorderLayout());
        pnlRole.setBackground(Color.WHITE);
        pnlRole.add(createLabel("Role"), BorderLayout.NORTH);
        cbRole = new JComboBox<>(new String[]{"USER", "ADMIN"});
        styleComboBox(cbRole);
        pnlRole.add(cbRole, BorderLayout.CENTER);
        
        // Birthday
        JPanel pnlDob = new JPanel(new BorderLayout());
        pnlDob.setBackground(Color.WHITE);
        pnlDob.add(createLabel("Birthday (yyyy-MM-dd)"), BorderLayout.NORTH);
        txtBirthday = createTextField();
        pnlDob.add(txtBirthday, BorderLayout.CENTER);

        metaPanel.add(pnlGender);
        metaPanel.add(pnlRole);
        metaPanel.add(pnlDob);
        
        formPanel.add(metaPanel, gbc);
        gbc.gridy++;

        // 6. Address
        formPanel.add(createLabel("Address"), gbc);
        gbc.gridy++;
        txtAddress = createTextField();
        formPanel.add(txtAddress, gbc);
        gbc.gridy++;

        // 7. Active Checkbox
        chkActive = new JCheckBox("Is Active?");
        chkActive.setSelected(true); // Default true
        chkActive.setBackground(Color.WHITE);
        chkActive.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(chkActive, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- FOOTER (BUTTONS) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footerPanel.setBackground(BG_COLOR);

        JButton btnCancel = createButton("Cancel", new Color(203, 213, 225), Color.BLACK);
        JButton btnSave = createButton("Create User", PRIMARY_COLOR, Color.WHITE);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> handleSave());

        footerPanel.add(btnCancel);
        footerPanel.add(btnSave);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void handleSave() {
        // 1. Validate Frontend
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username, Email, and Password are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Prepare Data
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("email", email);
        json.put("password", password);
        json.put("firstName", txtFirstName.getText().trim());
        json.put("lastName", txtLastName.getText().trim());
        json.put("address", txtAddress.getText().trim());
        json.put("gender", cbGender.getSelectedItem());
        json.put("role", cbRole.getSelectedItem());
        json.put("isActive", chkActive.isSelected());
        
        // Handle Birthday (Optional)
        String dob = txtBirthday.getText().trim();
        if (!dob.isEmpty()) {
            // Validate sơ bộ format ngày (yyyy-MM-dd)
            if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Birthday must be format yyyy-MM-dd", "Format Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            json.put("birthDay", dob);
        }

        // Mặc định các trường khác
        json.put("isAccepted", true);
        json.put("isOnline", false);

        // 3. Call API (Disable button to prevent double click)
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                return userService.createUser(token, json);
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    String error = get();
                    if (error == null) {
                        // Success
                        isConfirmed = true;
                        JOptionPane.showMessageDialog(UserCreateForm.this, "User created successfully!");
                        dispose(); // Đóng form
                    } else {
                        // Failed
                        JOptionPane.showMessageDialog(UserCreateForm.this, error, "API Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    // === UI HELPERS ===
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(new Color(100, 116, 139));
        return l;
    }

    private JTextField createTextField() {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(100, 35));
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return t;
    }
    
    private JTextField createPasswordField() {
        JPasswordField t = new JPasswordField(); // Dùng textfield thường để dễ xem khi test admin
        t.setPreferredSize(new Dimension(100, 35));
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return t;
    }

    private void styleComboBox(JComboBox box) {
        box.setPreferredSize(new Dimension(100, 35));
        box.setBackground(Color.WHITE);
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 38));
        return btn;
    }
}