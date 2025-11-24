package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import models.User;
import utils.ImageEditor;
import utils.ImageLoader;
import utils.UserSession;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class SettingPanel extends JPanel {
    private JLabel avatarLabel;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField addressField;
    private JComboBox<String> genderCombo;
    private JComboBox<Integer> dayCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    private ImageEditor editor;
    private String currentAvatarUrl;

    // Edit mode flags
    private boolean avatarEditMode = false;
    private boolean infoEditMode = false;
    private boolean emailEditMode = false;
    private boolean passwordEditMode = false;

    // Edit buttons
    private JButton avatarEditBtn;
    private JButton infoEditBtn;
    private JButton emailEditBtn;
    private JButton passwordEditBtn;

    // Action button panels
    private JPanel avatarActionPanel;
    private JPanel infoActionPanel;
    private JPanel emailActionPanel;
    private JPanel passwordActionPanel;

    public SettingPanel() {
        editor = new ImageEditor();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Main content with scroll
        JPanel mainContent = createMainContent();
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Load current user data
        loadUserData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(20, 30, 20, 30)));

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));

        header.add(titleLabel, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Profile Picture Section
        content.add(createProfileSection());
        content.add(Box.createVerticalStrut(30));

        // Personal Information Section
        content.add(createPersonalInfoSection());
        content.add(Box.createVerticalStrut(30));

        // Email Section
        content.add(createEmailSection());
        content.add(Box.createVerticalStrut(30));

        // Account Security Section
        content.add(createSecuritySection());

        return content;
    }

    private JPanel createProfileSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section header with edit button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel sectionTitle = new JLabel("Profile Picture");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));

        avatarEditBtn = createEditButton();
        avatarEditBtn.addActionListener(e -> toggleAvatarEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(avatarEditBtn, BorderLayout.EAST);

        section.add(headerPanel);
        section.add(Box.createVerticalStrut(15));

        // Avatar display
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        avatarPanel.setBackground(Color.WHITE);
        avatarPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(100, 100));
        avatarPanel.add(avatarLabel);

        // Avatar action buttons (hidden by default)
        avatarActionPanel = new JPanel();
        avatarActionPanel.setLayout(new BoxLayout(avatarActionPanel, BoxLayout.Y_AXIS));
        avatarActionPanel.setBackground(Color.WHITE);
        avatarActionPanel.setVisible(false);

        JButton changeAvatarBtn = createSecondaryButton("Change Avatar");
        changeAvatarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        changeAvatarBtn.addActionListener(e -> handleChangeAvatar());

        JButton removeAvatarBtn = createDangerButton("Remove Avatar");
        removeAvatarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeAvatarBtn.addActionListener(e -> handleRemoveAvatar());

        JButton saveAvatarBtn = createPrimaryButton("Save");
        saveAvatarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveAvatarBtn.addActionListener(e -> saveAvatarChanges());

        JButton cancelAvatarBtn = createSecondaryButton("Cancel");
        cancelAvatarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelAvatarBtn.addActionListener(e -> cancelAvatarEdit());

        avatarActionPanel.add(changeAvatarBtn);
        avatarActionPanel.add(Box.createVerticalStrut(10));
        avatarActionPanel.add(removeAvatarBtn);
        avatarActionPanel.add(Box.createVerticalStrut(15));
        avatarActionPanel.add(saveAvatarBtn);
        avatarActionPanel.add(Box.createVerticalStrut(10));
        avatarActionPanel.add(cancelAvatarBtn);

        avatarPanel.add(avatarActionPanel);
        section.add(avatarPanel);

        return section;
    }

    private JPanel createPersonalInfoSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section header with edit button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel sectionTitle = new JLabel("Personal Information");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));

        infoEditBtn = createEditButton();
        infoEditBtn.addActionListener(e -> toggleInfoEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(infoEditBtn, BorderLayout.EAST);

        section.add(headerPanel);
        section.add(Box.createVerticalStrut(15));

        // Info fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;

        int row = 0;

        // Username
        gbc.gridx = 0;
        gbc.gridy = row++;
        fieldsPanel.add(createLabel("Username"), gbc);

        gbc.gridy = row++;
        nameField = createTextField(30);
        nameField.setEnabled(false);
        fieldsPanel.add(nameField, gbc);

        // Gender
        gbc.gridy = row++;
        fieldsPanel.add(createLabel("Gender"), gbc);

        gbc.gridy = row++;
        String[] genders = { "Select Gender", "Male", "Female", "Other", "Prefer not to say" };
        genderCombo = new JComboBox<>(genders);
        genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderCombo.setBackground(Color.WHITE);
        genderCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(8, 10, 8, 10)));
        genderCombo.setMaximumSize(new Dimension(400, 40));
        genderCombo.setEnabled(false);
        fieldsPanel.add(genderCombo, gbc);

        // Birthday
        gbc.gridy = row++;
        fieldsPanel.add(createLabel("Birthday"), gbc);

        gbc.gridy = row++;
        JPanel birthdayPanel = createBirthdayPanel();
        fieldsPanel.add(birthdayPanel, gbc);

        // Address
        gbc.gridy = row++;
        fieldsPanel.add(createLabel("Address"), gbc);

        gbc.gridy = row++;
        addressField = createTextField(30);
        addressField.setEnabled(false);
        fieldsPanel.add(addressField, gbc);

        section.add(fieldsPanel);

        // Action buttons (hidden by default)
        infoActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoActionPanel.setBackground(Color.WHITE);
        infoActionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoActionPanel.setVisible(false);

        JButton saveInfoBtn = createPrimaryButton("Save Changes");
        saveInfoBtn.addActionListener(e -> saveInfoChanges());

        JButton cancelInfoBtn = createSecondaryButton("Cancel");
        cancelInfoBtn.addActionListener(e -> cancelInfoEdit());

        infoActionPanel.add(saveInfoBtn);
        infoActionPanel.add(cancelInfoBtn);

        section.add(Box.createVerticalStrut(10));
        section.add(infoActionPanel);

        return section;
    }

    private JPanel createEmailSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section header with edit button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel sectionTitle = new JLabel("Email Address");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));

        emailEditBtn = createEditButton();
        emailEditBtn.addActionListener(e -> toggleEmailEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(emailEditBtn, BorderLayout.EAST);

        section.add(headerPanel);
        section.add(Box.createVerticalStrut(15));

        // Email field
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;

        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(createLabel("Email"), gbc);

        gbc.gridy = 1;
        emailField = createTextField(30);
        emailField.setEnabled(false);
        fieldsPanel.add(emailField, gbc);

        section.add(fieldsPanel);

        // Action buttons (hidden by default)
        emailActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        emailActionPanel.setBackground(Color.WHITE);
        emailActionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailActionPanel.setVisible(false);

        JButton saveEmailBtn = createPrimaryButton("Save Changes");
        saveEmailBtn.addActionListener(e -> saveEmailChanges());

        JButton cancelEmailBtn = createSecondaryButton("Cancel");
        cancelEmailBtn.addActionListener(e -> cancelEmailEdit());

        emailActionPanel.add(saveEmailBtn);
        emailActionPanel.add(cancelEmailBtn);

        section.add(Box.createVerticalStrut(10));
        section.add(emailActionPanel);

        return section;
    }

    private JPanel createBirthdayPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(Color.WHITE);

        // Day dropdown
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
        dayCombo = new JComboBox<>(days);
        dayCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dayCombo.setPreferredSize(new Dimension(80, 40));
        dayCombo.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        dayCombo.setEnabled(false);

        // Month dropdown
        String[] months = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        monthCombo.setPreferredSize(new Dimension(130, 40));
        monthCombo.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        monthCombo.setEnabled(false);

        // Year dropdown
        Integer[] years = new Integer[100];
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 100; i++) {
            years[i] = currentYear - i;
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearCombo.setPreferredSize(new Dimension(100, 40));
        yearCombo.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        yearCombo.setEnabled(false);

        panel.add(dayCombo);
        panel.add(monthCombo);
        panel.add(yearCombo);

        return panel;
    }

    private JPanel createSecuritySection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section header with edit button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel sectionTitle = new JLabel("Change Password");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));

        passwordEditBtn = createEditButton();
        passwordEditBtn.addActionListener(e -> togglePasswordEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(passwordEditBtn, BorderLayout.EAST);

        section.add(headerPanel);
        section.add(Box.createVerticalStrut(15));

        // Password fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;

        int row = 0;

        // Current Password
        gbc.gridx = 0;
        gbc.gridy = row++;
        fieldsPanel.add(createLabel("Current Password"), gbc);

        gbc.gridy = row++;
        currentPasswordField = createPasswordField(30);
        currentPasswordField.setEnabled(false);
        fieldsPanel.add(currentPasswordField, gbc);

        // New Password
        gbc.gridy = row++;
        fieldsPanel.add(createLabel("New Password"), gbc);

        gbc.gridy = row++;
        newPasswordField = createPasswordField(30);
        newPasswordField.setEnabled(false);
        fieldsPanel.add(newPasswordField, gbc);

        // Confirm New Password
        gbc.gridy = row++;
        fieldsPanel.add(createLabel("Confirm New Password"), gbc);

        gbc.gridy = row++;
        confirmPasswordField = createPasswordField(30);
        confirmPasswordField.setEnabled(false);
        fieldsPanel.add(confirmPasswordField, gbc);

        section.add(fieldsPanel);

        // Action buttons (hidden by default)
        passwordActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        passwordActionPanel.setBackground(Color.WHITE);
        passwordActionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordActionPanel.setVisible(false);

        JButton savePasswordBtn = createPrimaryButton("Change Password");
        savePasswordBtn.addActionListener(e -> savePasswordChanges());

        JButton cancelPasswordBtn = createSecondaryButton("Cancel");
        cancelPasswordBtn.addActionListener(e -> cancelPasswordEdit());

        passwordActionPanel.add(savePasswordBtn);
        passwordActionPanel.add(cancelPasswordBtn);

        section.add(Box.createVerticalStrut(10));
        section.add(passwordActionPanel);

        return section;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(73, 80, 87));
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        field.setMaximumSize(new Dimension(400, 40));
        return field;
    }

    private JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        field.setMaximumSize(new Dimension(400, 40));
        return field;
    }

    private JButton createEditButton() {
        JButton button = new JButton("Edit");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(new Color(13, 110, 253));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(13, 110, 253), 1),
                new EmptyBorder(6, 20, 6, 20)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 247, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(13, 110, 253));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(12, 30, 12, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(11, 94, 215));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(13, 110, 253));
            }
        });

        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(108, 117, 125));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(11, 29, 11, 29)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 242, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(220, 53, 69));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 53, 69), 1),
                new EmptyBorder(11, 29, 11, 29)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 245, 246));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private void loadUserData() {
        User user = UserSession.getUser();
        if (user == null)
            return;

        // Load avatar
        currentAvatarUrl = user.getAvatar();
        if (currentAvatarUrl != null && !currentAvatarUrl.isEmpty()) {
            ImageLoader.loadImageAsync(currentAvatarUrl, new ImageLoader.ImageLoadCallback() {
                @Override
                public void onLoaded(Image img) {
                    if (avatarLabel != null) {
                        avatarLabel.setIcon(editor.makeCircularImage(img, 100));
                    }
                }
            });
        }

        // Load user data
        nameField.setText(user.getUsername() != null ? user.getUsername() : "");
        emailField.setText(""); // TODO: Add email to User model
        addressField.setText(user.getAddress() != null ? user.getAddress() : "");

        // Set gender
        String gender = user.getGender();
        if (gender != null && !gender.isEmpty()) {
            genderCombo.setSelectedItem(gender);
        }

        // Parse and set birthday
        String birthday = user.getBDAY();
        if (birthday != null && !birthday.isEmpty()) {
            try {
                // Assuming format is YYYY-MM-DD
                String[] parts = birthday.split("-");
                if (parts.length == 3) {
                    yearCombo.setSelectedItem(Integer.parseInt(parts[0]));
                    monthCombo.setSelectedIndex(Integer.parseInt(parts[1]) - 1);
                    dayCombo.setSelectedItem(Integer.parseInt(parts[2]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Toggle methods
    private void toggleAvatarEditMode() {
        avatarEditMode = !avatarEditMode;
        avatarActionPanel.setVisible(avatarEditMode);
        avatarEditBtn.setText(avatarEditMode ? "Cancel" : "Edit");
        revalidate();
        repaint();
    }

    private void toggleInfoEditMode() {
        infoEditMode = !infoEditMode;
        nameField.setEnabled(infoEditMode);
        genderCombo.setEnabled(infoEditMode);
        dayCombo.setEnabled(infoEditMode);
        monthCombo.setEnabled(infoEditMode);
        yearCombo.setEnabled(infoEditMode);
        addressField.setEnabled(infoEditMode);
        infoActionPanel.setVisible(infoEditMode);
        infoEditBtn.setText(infoEditMode ? "Cancel" : "Edit");
        revalidate();
        repaint();
    }

    private void toggleEmailEditMode() {
        emailEditMode = !emailEditMode;
        emailField.setEnabled(emailEditMode);
        emailActionPanel.setVisible(emailEditMode);
        emailEditBtn.setText(emailEditMode ? "Cancel" : "Edit");
        revalidate();
        repaint();
    }

    private void togglePasswordEditMode() {
        passwordEditMode = !passwordEditMode;
        currentPasswordField.setEnabled(passwordEditMode);
        newPasswordField.setEnabled(passwordEditMode);
        confirmPasswordField.setEnabled(passwordEditMode);
        passwordActionPanel.setVisible(passwordEditMode);
        passwordEditBtn.setText(passwordEditMode ? "Cancel" : "Edit");

        if (!passwordEditMode) {
            // Clear password fields when canceling
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        }

        revalidate();
        repaint();
    }

    // Save methods
    private void saveAvatarChanges() {
        // TODO: API call to update avatar
        JOptionPane.showMessageDialog(this,
                "Avatar updated successfully! (API integration pending)",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        toggleAvatarEditMode();
    }

    private void saveInfoChanges() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username cannot be empty",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Format birthday
        String birthday = String.format("%d-%02d-%02d",
                (Integer) yearCombo.getSelectedItem(),
                monthCombo.getSelectedIndex() + 1,
                (Integer) dayCombo.getSelectedItem());

        // TODO: API call to update user info
        JOptionPane.showMessageDialog(this,
                "Personal information updated successfully! (API integration pending)",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        toggleInfoEditMode();
    }

    private void saveEmailChanges() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Email cannot be empty",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: API call to update email
        JOptionPane.showMessageDialog(this,
                "Email updated successfully! (API integration pending)",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        toggleEmailEditMode();
    }

    private void savePasswordChanges() {
        String currentPass = new String(currentPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        if (currentPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your current password",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a new password",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,
                    "New passwords do not match",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newPass.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: API call to change password
        JOptionPane.showMessageDialog(this,
                "Password changed successfully! (API integration pending)",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        // Clear password fields
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");

        togglePasswordEditMode();
    }

    // Cancel methods
    private void cancelAvatarEdit() {
        loadUserData(); // Reload original avatar
        toggleAvatarEditMode();
    }

    private void cancelInfoEdit() {
        loadUserData(); // Reload original data
        toggleInfoEditMode();
    }

    private void cancelEmailEdit() {
        loadUserData(); // Reload original email
        toggleEmailEditMode();
    }

    private void cancelPasswordEdit() {
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        togglePasswordEditMode();
    }

    private void handleChangeAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();

            // Load and display the selected image
            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
            Image img = icon.getImage();
            avatarLabel.setIcon(editor.makeCircularImage(img, 100));

            // TODO: Upload to server when save is clicked
        }
    }

    private void handleRemoveAvatar() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove your profile picture?",
                "Remove Avatar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Set default/empty avatar
            avatarLabel.setIcon(null);
            currentAvatarUrl = null;

            // TODO: Remove from server when save is clicked
        }
    }

    // Public method to refresh panel data (call this when showing settings)
    public void refreshData() {
        loadUserData();

        // Reset all edit modes
        if (avatarEditMode)
            toggleAvatarEditMode();
        if (infoEditMode)
            toggleInfoEditMode();
        if (emailEditMode)
            toggleEmailEditMode();
        if (passwordEditMode)
            togglePasswordEditMode();
    }
}