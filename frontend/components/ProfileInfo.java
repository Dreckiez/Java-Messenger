package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import org.json.JSONObject;

import models.User;
import services.UserServices;
import utils.UserSession;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class ProfileInfo extends JPanel {
    private JTextField usernameField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField addressField;
    private JComboBox<String> genderCombo;
    private JComboBox<String> dayCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<String> yearCombo;

    private boolean editMode = false;
    private JButton editBtn;
    private JPanel actionPanel;
    private UserServices userService;

    // --- COLORS ---
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private final Color INPUT_BG = new Color(226, 232, 240);
    private final Color INPUT_HOVER_BG = new Color(241, 245, 249);
    private final Color BTN_BLUE = new Color(59, 130, 246);
    private final Color BTN_BLUE_HOVER = new Color(37, 99, 235);

    public ProfileInfo() {
        this.userService = new UserServices();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Personal Information");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sectionTitle.setForeground(TEXT_PRIMARY);

        editBtn = createEditButton();
        editBtn.addActionListener(e -> toggleEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(editBtn, BorderLayout.EAST);

        add(headerPanel);
        add(Box.createVerticalStrut(25));

        // --- FIELDS ---
        JPanel fieldsPanel = createFieldsPanel();
        add(fieldsPanel);

        // --- ACTIONS ---
        actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setVisible(false);

        JButton saveBtn = createPrimaryButton("Save Changes");
        saveBtn.addActionListener(e -> saveChanges());

        JButton cancelBtn = createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> cancelEdit());

        actionPanel.add(saveBtn);
        actionPanel.add(cancelBtn);

        add(Box.createVerticalStrut(25));
        add(actionPanel);

        loadUserData();
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
        gbc.weightx = 1.0;

        int row = 0;

        // 1. Username (EDITABLE - Đã bỏ style disabled text color để chuẩn bị cho việc
        // edit)
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 5, 0);
        panel.add(createLabel("Username"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 20, 0);
        usernameField = createStyledTextField();
        usernameField.setEnabled(false);
        panel.add(wrapInRoundedPanel(usernameField), gbc);

        // 2. Email (EDITABLE)
        gbc.gridy = row++;
        gbc.insets = new Insets(8, 0, 5, 0);
        panel.add(createLabel("Email"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 20, 0);
        emailField = createStyledTextField();
        emailField.setEnabled(false);
        panel.add(wrapInRoundedPanel(emailField), gbc);

        // 3. First & Last Name (EDITABLE)
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(8, 0, 5, 10);
        panel.add(createLabel("First Name"), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(8, 10, 5, 0);
        panel.add(createLabel("Last Name"), gbc);

        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 20, 10);
        firstNameField = createStyledTextField();
        firstNameField.setEnabled(false);
        panel.add(wrapInRoundedPanel(firstNameField), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 20, 0);
        lastNameField = createStyledTextField();
        lastNameField.setEnabled(false);
        panel.add(wrapInRoundedPanel(lastNameField), gbc);

        // 4. Gender (EDITABLE)
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 0, 5, 0);
        panel.add(createLabel("Gender"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 20, 0);
        String[] genders = { "Select Gender", "Male", "Female", "Hidden" };
        genderCombo = createStyledComboBox(genders);
        genderCombo.setEnabled(false);
        panel.add(genderCombo, gbc);

        // 5. Birthday (EDITABLE)
        gbc.gridy = row++;
        gbc.insets = new Insets(8, 0, 5, 0);
        panel.add(createLabel("Birthday"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(createBirthdayPanel(), gbc);

        // 6. Address (EDITABLE)
        gbc.gridy = row++;
        gbc.insets = new Insets(8, 0, 5, 0);
        panel.add(createLabel("Address"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 20, 0);
        addressField = createStyledTextField();
        addressField.setEnabled(false);
        panel.add(wrapInRoundedPanel(addressField), gbc);

        return panel;
    }

    private JPanel createBirthdayPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        String[] days = new String[32];
        days[0] = "Day";
        for (int i = 1; i <= 31; i++)
            days[i] = String.valueOf(i);
        dayCombo = createStyledComboBox(days);
        dayCombo.setEnabled(false);

        String[] months = { "Month", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        monthCombo = createStyledComboBox(months);
        monthCombo.setEnabled(false);

        String[] years = new String[101];
        years[0] = "Year";
        int currentYear = LocalDate.now().getYear();
        for (int i = 1; i <= 100; i++)
            years[i] = String.valueOf(currentYear - (i - 1));
        yearCombo = createStyledComboBox(years);
        yearCombo.setEnabled(false);

        panel.add(dayCombo);
        panel.add(monthCombo);
        panel.add(yearCombo);

        return panel;
    }

    // --- HELPER UI ---
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_SECONDARY);
        label.setBorder(new EmptyBorder(0, 5, 0, 0));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
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

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setForeground(TEXT_PRIMARY);
        combo.setBackground(INPUT_BG);

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setForeground(TEXT_PRIMARY);
                if (isSelected && index != -1) {
                    setBackground(new Color(200, 220, 255));
                } else {
                    setBackground(combo.getBackground());
                }
                setBorder(new EmptyBorder(0, 5, 0, 0));
                return this;
            }
        });

        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▼");
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                btn.setForeground(combo.isEnabled() ? TEXT_SECONDARY : new Color(0, 0, 0, 0));
                btn.setBorder(null);
                btn.setContentAreaFilled(false);
                return btn;
            }

            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);
                super.paint(g2, c);
                g2.dispose();
            }
        });

        combo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (combo.isEnabled()) {
                    combo.setBackground(INPUT_HOVER_BG);
                    combo.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                combo.setBackground(INPUT_BG);
                combo.repaint();
            }
        });

        combo.setBorder(new EmptyBorder(5, 10, 5, 10));
        return combo;
    }

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
        btn.setPreferredSize(new Dimension(hasBorder ? 80 : 140, 40));

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

    // --- LOGIC ---

    private void loadUserData() {
        User user = UserSession.getUser();
        if (user == null)
            return;

        usernameField.setText(user.getUsername() != null ? user.getUsername() : "");
        emailField.setText(user.getEmail() != null ? user.getEmail() : "");
        addressField.setText(user.getAddress() != null ? user.getAddress() : "");
        firstNameField.setText(user.getFirstName() != null ? user.getFirstName() : "");
        lastNameField.setText(user.getLastName() != null ? user.getLastName() : "");

        String gender = user.getGender();
        if (gender != null && !gender.isEmpty()) {
            String formattedGender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
            genderCombo.setSelectedItem(formattedGender);
        } else {
            genderCombo.setSelectedIndex(0);
        }

        String birthday = user.getBirthDay();
        if (birthday != null && !birthday.isEmpty()) {
            try {
                String[] parts = birthday.split("-");
                if (parts.length == 3) {
                    yearCombo.setSelectedItem(parts[0]);
                    monthCombo.setSelectedIndex(Integer.parseInt(parts[1]));
                    dayCombo.setSelectedItem(parts[2]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        revalidate();
        repaint();
    }

    private void toggleEditMode() {
        editMode = !editMode;

        // --- CHO PHÉP SỬA TẤT CẢ CÁC TRƯỜNG ---
        firstNameField.setEnabled(editMode);
        lastNameField.setEnabled(editMode);
        emailField.setEnabled(editMode);
        usernameField.setEnabled(editMode); // 🔥 Đã mở khóa
        genderCombo.setEnabled(editMode);
        dayCombo.setEnabled(editMode);
        monthCombo.setEnabled(editMode);
        yearCombo.setEnabled(editMode);
        addressField.setEnabled(editMode);

        resetComboBoxColor(genderCombo, dayCombo, monthCombo, yearCombo);

        actionPanel.setVisible(editMode);
        editBtn.setText(editMode ? "Cancel" : "Edit");
        revalidate();
        repaint();
    }

    private void resetComboBoxColor(JComboBox<String>... combos) {
        for (JComboBox<String> c : combos) {
            c.setBackground(INPUT_BG);
        }
    }

    private void saveChanges() {
        User user = UserSession.getUser();
        if (user == null)
            return;

        // 1. Validate Inputs
        String newFname = firstNameField.getText().trim();
        String newLname = lastNameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newUname = usernameField.getText().trim();

        if (newFname.isEmpty() || newLname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name and Last Name cannot be empty", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newUname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Build Update JSON
        JSONObject updateData = new JSONObject();
        boolean hasChanges = false;

        // Check Username
        if (!newUname.equals(user.getUsername())) {
            updateData.put("username", newUname);
            hasChanges = true;
        }

        // Check Email
        if (!newEmail.equals(user.getEmail())) {
            updateData.put("email", newEmail);
            hasChanges = true;
        }

        // Check Names
        if (!newFname.equals(user.getFirstName())) {
            updateData.put("firstName", newFname);
            hasChanges = true;
        }
        if (!newLname.equals(user.getLastName())) {
            updateData.put("lastName", newLname);
            hasChanges = true;
        }

        // Check Address
        String newAddress = addressField.getText().trim();
        String currentAddress = user.getAddress() != null ? user.getAddress() : "";
        if (!newAddress.equals(currentAddress)) {
            updateData.put("address", newAddress);
            hasChanges = true;
        }

        // Check Gender
        String selectedGender = (String) genderCombo.getSelectedItem();
        if (selectedGender != null && !selectedGender.equals("Select Gender")) {
            String apiGender = selectedGender.toUpperCase();
            String currentGender = user.getGender() != null ? user.getGender().toUpperCase() : "";
            if (!apiGender.equals(currentGender)) {
                updateData.put("gender", apiGender);
                hasChanges = true;
            }
        }

        // Check Birthday
        String y = (String) yearCombo.getSelectedItem();
        int m = monthCombo.getSelectedIndex();
        String d = (String) dayCombo.getSelectedItem();

        if (!"Year".equals(y) && m != 0 && !"Day".equals(d)) {
            String newBday = String.format("%s-%02d-%02d", y, m, Integer.parseInt(d));
            String currentBday = user.getBirthDay();
            if (currentBday == null || !newBday.equals(currentBday)) {
                updateData.put("birthday", newBday);
                hasChanges = true;
            }
        }

        // 3. Gửi Request
        if (hasChanges) {
            String token = user.getToken();

            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() throws Exception {
                    return userService.updateProfile(token, updateData);
                }

                @Override
                protected void done() {
                    try {
                        JSONObject result = get();

                        if (result.has("error")) {
                            JOptionPane.showMessageDialog(ProfileInfo.this,
                                    "Update Failed:\n" + result.getString("error"),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(ProfileInfo.this, "Saved successfully!", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            updateLocalUserSession(result);
                            toggleEditMode();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(ProfileInfo.this, "An error occurred.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        } else {
            toggleEditMode();
        }
    }

    private void updateLocalUserSession(JSONObject responseJson) {
        User user = UserSession.getUser();

        // 1. Cập nhật Tokens (Chỉ cập nhật nếu có giá trị và KHÁC rỗng)
        if (responseJson.has("token")) {
            String t = responseJson.getString("token");
            if (t != null && !t.isEmpty()) {
                user.setToken(t);
            }
        }
        if (responseJson.has("refreshToken")) {
            String rt = responseJson.getString("refreshToken");
            if (rt != null && !rt.isEmpty()) {
                user.setRefreshToken(rt);
            }
        }

        // 2. Cập nhật Profile Info (Nằm trong object "userProfile")
        if (responseJson.has("userProfile")) {
            JSONObject profile = responseJson.getJSONObject("userProfile");

            user.setUsername(profile.optString("username", user.getUsername()));
            user.setEmail(profile.optString("email", user.getEmail()));
            user.setFirstName(profile.optString("firstName", user.getFirstName()));
            user.setLastName(profile.optString("lastName", user.getLastName()));
            user.setAddress(profile.optString("address", user.getAddress()));
            user.setGender(profile.optString("gender", user.getGender()));
            user.setBirthDay(profile.optString("birthday", user.getBirthDay()));
        }
    }

    private void cancelEdit() {
        loadUserData();
        if (editMode)
            toggleEditMode();
    }

    public void refreshData() {
        loadUserData();
        if (editMode)
            toggleEditMode();
    }
}