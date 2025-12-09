package components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.UserManagementItemList;
import org.json.JSONObject;
import services.UserAdminService;
import utils.UserSession;

public class UserEditForm extends JDialog {
    // UI Components
    private JTextField txtUsername, txtEmail, txtPassword, txtFirstName, txtLastName, txtAddress;
    private JComboBox<String> cbGender, cbRole;
    private boolean isConfirmed = false;
    
    // Data & Service
    private UserAdminService userService;
    private Long userId;

    // Biến lưu giá trị gốc (Original Values) để so sánh
    private String origUsername;
    private String origEmail;
    private String origFirstName;
    private String origLastName;
    private String origAddress;
    private String origGender;
    private String origRole;

    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color BG_COLOR = new Color(248, 250, 252);

    // 👇 Constructor nhận UserManagementItemList
    public UserEditForm(Window parent, UserManagementItemList user) {
        super(parent, "Edit User: " + user.getUsername(), ModalityType.APPLICATION_MODAL);
        this.userId = user.getUserId();
        this.userService = new UserAdminService();

        // 1. LƯU GIÁ TRỊ GỐC TỪ OBJECT
        this.origUsername = user.getUsername();
        this.origEmail = user.getEmail();
        this.origFirstName = user.getFirstName();
        this.origLastName = user.getLastName();
        
        // Xử lý null an toàn cho các trường optional
        this.origAddress = (user.getAddress() != null) ? user.getAddress() : "";
        this.origGender = (user.getGender() != null) ? user.getGender() : "";
        this.origRole = (user.getRole() != null) ? user.getRole() : "";

        setSize(500, 650); // Tăng chiều cao xíu vì form dài
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        headerPanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("Edit User Information");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 41, 59));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- FORM ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0;

        // 1. Username
        formPanel.add(createLabel("Username"), gbc);
        gbc.gridy++;
        txtUsername = createTextField();
        txtUsername.setText(origUsername); // Pre-fill
        formPanel.add(txtUsername, gbc);
        gbc.gridy++;

        // 2. Email
        formPanel.add(createLabel("Email"), gbc);
        gbc.gridy++;
        txtEmail = createTextField();
        txtEmail.setText(origEmail); // Pre-fill
        formPanel.add(txtEmail, gbc);
        gbc.gridy++;

        // 3. Name
        JPanel namePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        namePanel.setBackground(Color.WHITE);
        
        JPanel pnlFirst = new JPanel(new BorderLayout());
        pnlFirst.setBackground(Color.WHITE);
        pnlFirst.add(createLabel("First Name"), BorderLayout.NORTH);
        txtFirstName = createTextField();
        txtFirstName.setText(origFirstName); // Pre-fill
        pnlFirst.add(txtFirstName, BorderLayout.CENTER);

        JPanel pnlLast = new JPanel(new BorderLayout());
        pnlLast.setBackground(Color.WHITE);
        pnlLast.add(createLabel("Last Name"), BorderLayout.NORTH);
        txtLastName = createTextField();
        txtLastName.setText(origLastName); // Pre-fill
        pnlLast.add(txtLastName, BorderLayout.CENTER);

        namePanel.add(pnlFirst);
        namePanel.add(pnlLast);
        formPanel.add(namePanel, gbc);
        gbc.gridy++;

        // 4. Password
        formPanel.add(createLabel("New Password (Leave blank to keep)"), gbc);
        gbc.gridy++;
        txtPassword = createTextField();
        formPanel.add(txtPassword, gbc);
        gbc.gridy++;

        // 5. Gender & Role
        JPanel metaPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        metaPanel.setBackground(Color.WHITE);

        // Gender
        JPanel pnlGender = new JPanel(new BorderLayout());
        pnlGender.setBackground(Color.WHITE);
        pnlGender.add(createLabel("Gender"), BorderLayout.NORTH);
        // Lưu ý: Item rỗng "" ở đầu để handle trường hợp chưa set hoặc user muốn bỏ trống
        cbGender = new JComboBox<>(new String[]{"", "MALE", "FEMALE", "HIDDEN"});
        styleComboBox(cbGender);
        // 👇 Pre-fill Gender (Chọn đúng item trong list)
        if (!origGender.isEmpty()) {
            cbGender.setSelectedItem(origGender);
        }
        pnlGender.add(cbGender, BorderLayout.CENTER);

        // Role
        JPanel pnlRole = new JPanel(new BorderLayout());
        pnlRole.setBackground(Color.WHITE);
        pnlRole.add(createLabel("Role"), BorderLayout.NORTH);
        cbRole = new JComboBox<>(new String[]{"", "USER", "ADMIN"});
        styleComboBox(cbRole);
        // 👇 Pre-fill Role
        if (!origRole.isEmpty()) {
            cbRole.setSelectedItem(origRole);
        }
        pnlRole.add(cbRole, BorderLayout.CENTER);

        metaPanel.add(pnlGender);
        metaPanel.add(pnlRole);
        formPanel.add(metaPanel, gbc);
        gbc.gridy++;
        
        // 6. Address
        formPanel.add(createLabel("Address"), gbc);
        gbc.gridy++;
        txtAddress = createTextField();
        txtAddress.setText(origAddress); // Pre-fill
        formPanel.add(txtAddress, gbc);
        
        add(formPanel, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footerPanel.setBackground(BG_COLOR);

        JButton btnCancel = createButton("Cancel", new Color(203, 213, 225), Color.BLACK);
        JButton btnSave = createButton("Update", PRIMARY_COLOR, Color.WHITE);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> handleUpdate());

        footerPanel.add(btnCancel);
        footerPanel.add(btnSave);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void handleUpdate() {
        JSONObject json = new JSONObject();
        boolean hasChange = false;

        // 🔥 LOGIC KIỂM TRA THAY ĐỔI (DIRTY CHECK) CỰC KỲ CHI TIẾT 🔥

        // 1. Username
        String newUser = txtUsername.getText().trim();
        if (!newUser.isEmpty() && !newUser.equals(origUsername)) {
            json.put("username", newUser);
            hasChange = true;
        }
        
        // 2. Email
        String newEmail = txtEmail.getText().trim();
        if (!newEmail.isEmpty() && !newEmail.equals(origEmail)) {
            json.put("email", newEmail);
            hasChange = true;
        }
        
        // 3. First Name
        String newFirst = txtFirstName.getText().trim();
        // So sánh chuỗi, xử lý null an toàn bằng cách so với chuỗi gốc đã clean
        if (!newFirst.equals(origFirstName != null ? origFirstName : "")) {
            json.put("firstName", newFirst);
            hasChange = true;
        }
        
        // 4. Last Name
        String newLast = txtLastName.getText().trim();
        if (!newLast.equals(origLastName != null ? origLastName : "")) {
            json.put("lastName", newLast);
            hasChange = true;
        }
        
        // 5. Password (Chỉ gửi nếu có nhập)
        String newPass = txtPassword.getText().trim();
        if (!newPass.isEmpty()) {
            json.put("password", newPass);
            hasChange = true;
        }
        
        // 6. Address
        String newAddr = txtAddress.getText().trim();
        if (!newAddr.equals(origAddress)) {
            json.put("address", newAddr);
            hasChange = true;
        }
        
        // 7. Gender
        String newGender = (String) cbGender.getSelectedItem();
        if (newGender == null) newGender = "";
        // Nếu khác giá trị gốc thì gửi
        if (!newGender.equals(origGender)) {
            json.put("gender", newGender);
            hasChange = true;
        }
        
        // 8. Role
        String newRole = (String) cbRole.getSelectedItem();
        if (newRole == null) newRole = "";
        if (!newRole.equals(origRole)) {
            json.put("role", newRole);
            hasChange = true;
        }

        // Nếu không có gì thay đổi -> Thông báo và không gọi API
        if (!hasChange) {
            JOptionPane.showMessageDialog(this, "No changes detected. Nothing to update.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // --- GỌI API ---
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String token = UserSession.getUser().getToken();
                return userService.updateUser(token, userId, json);
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    String error = get();
                    if (error == null) {
                        isConfirmed = true;
                        JOptionPane.showMessageDialog(UserEditForm.this, "Updated successfully!");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(UserEditForm.this, error, "Update Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public boolean isConfirmed() { return isConfirmed; }

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
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
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
        btn.setPreferredSize(new Dimension(100, 38));
        return btn;
    }
}