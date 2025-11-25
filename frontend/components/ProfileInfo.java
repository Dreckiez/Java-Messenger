package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import models.User;
import utils.UserSession;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class ProfileInfo extends JPanel {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField addressField;
    private JComboBox<String> genderCombo;
    private JComboBox<String> dayCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<String> yearCombo;

    private boolean editMode = false;
    private JButton editBtn;
    private JPanel actionPanel;

    public ProfileInfo() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(20, 30, 20, 30)));

        // Section header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel sectionTitle = new JLabel("Personal Information");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));

        editBtn = createEditButton();
        editBtn.addActionListener(e -> toggleEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(editBtn, BorderLayout.EAST);

        add(headerPanel);
        add(Box.createVerticalStrut(15));

        // Info fields
        JPanel fieldsPanel = createFieldsPanel();
        add(fieldsPanel);

        // Action buttons
        actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setVisible(false);

        JButton saveBtn = createPrimaryButton("Save Changes");
        saveBtn.addActionListener(e -> saveChanges());

        JButton cancelBtn = createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> cancelEdit());

        actionPanel.add(saveBtn);
        actionPanel.add(cancelBtn);

        add(Box.createVerticalStrut(10));
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
        gbc.insets = new Insets(5, 0, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;

        int row = 0;

        // Username
        gbc.gridx = 0;
        gbc.gridy = row++;
        panel.add(createLabel("Username"), gbc);

        gbc.gridy = row++;
        nameField = createTextField(30);
        nameField.setEnabled(false);
        panel.add(nameField, gbc);

        // Email
        gbc.gridy = row++;
        panel.add(createLabel("Email"), gbc);

        gbc.gridy = row++;
        emailField = createTextField(30);
        emailField.setEnabled(false);
        panel.add(emailField, gbc);

        // Gender
        gbc.gridy = row++;
        panel.add(createLabel("Gender"), gbc);

        gbc.gridy = row++;
        String[] genders = { "Select Gender", "Male", "Female", "Hidden" };
        genderCombo = new JComboBox<>(genders);
        genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderCombo.setBackground(Color.WHITE);
        genderCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(8, 10, 8, 10)));
        genderCombo.setMaximumSize(new Dimension(400, 40));
        genderCombo.setEnabled(false);
        panel.add(genderCombo, gbc);

        // Birthday
        gbc.gridy = row++;
        panel.add(createLabel("Birthday"), gbc);

        gbc.gridy = row++;
        panel.add(createBirthdayPanel(), gbc);

        // Address
        gbc.gridy = row++;
        panel.add(createLabel("Address"), gbc);

        gbc.gridy = row++;
        addressField = createTextField(30);
        addressField.setEnabled(false);
        panel.add(addressField, gbc);

        return panel;
    }

    private JPanel createBirthdayPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(Color.WHITE);

        // Days with placeholder
        String[] days = new String[32];
        days[0] = "Day";
        for (int i = 1; i <= 31; i++)
            days[i] = String.valueOf(i);
        dayCombo = new JComboBox<>(days);
        dayCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dayCombo.setPreferredSize(new Dimension(80, 40));
        dayCombo.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        dayCombo.setEnabled(false);

        // Months with placeholder FIRST
        String[] months = {
                "Month", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        monthCombo.setPreferredSize(new Dimension(130, 40));
        monthCombo.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        monthCombo.setEnabled(false);

        // Years with placeholder
        String[] years = new String[101];
        years[0] = "Year";
        int currentYear = LocalDate.now().getYear();
        for (int i = 1; i <= 100; i++)
            years[i] = String.valueOf(currentYear - (i - 1));

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

    private void loadUserData() {
        User user = UserSession.getUser();
        if (user == null)
            return;

        nameField.setText(user.getUsername() != null ? user.getUsername() : "");
        emailField.setText(user.getEmail() != null ? user.getEmail() : "");
        addressField.setText(user.getAddress() != null ? user.getAddress() : "");

        String gender = user.getGender();
        if (gender != null && !gender.isEmpty()) {
            genderCombo.setSelectedItem(
                    gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase());
        }

        String birthday = user.getBDAY();

        if (birthday != null && !birthday.isEmpty()) {
            try {
                String[] parts = birthday.split("-");
                if (parts.length == 3) {
                    yearCombo.setSelectedItem(parts[0]); // string works now
                    monthCombo.setSelectedIndex(Integer.parseInt(parts[1])); // because index 0 = "Month"
                    dayCombo.setSelectedItem(parts[2]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Set placeholder
            dayCombo.setSelectedIndex(0);
            monthCombo.setSelectedIndex(0);
            yearCombo.setSelectedIndex(0);
        }

        // Force UI update
        revalidate();
        repaint();
    }

    private void toggleEditMode() {
        editMode = !editMode;
        nameField.setEnabled(editMode);
        emailField.setEnabled(editMode);
        genderCombo.setEnabled(editMode);
        dayCombo.setEnabled(editMode);
        monthCombo.setEnabled(editMode);
        yearCombo.setEnabled(editMode);
        addressField.setEnabled(editMode);
        actionPanel.setVisible(editMode);
        editBtn.setText(editMode ? "Cancel" : "Edit");
        revalidate();
        repaint();
    }

    private void saveChanges() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username cannot be empty",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String birthday = String.format("%d-%02d-%02d",
                (Integer) yearCombo.getSelectedItem(),
                monthCombo.getSelectedIndex() + 1,
                (Integer) dayCombo.getSelectedItem());

        JOptionPane.showMessageDialog(this,
                "Information updated successfully! (API integration pending)",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        toggleEditMode();
    }

    private void cancelEdit() {
        loadUserData();
        toggleEditMode();
    }

    public void refreshData() {
        loadUserData();
        if (editMode)
            toggleEditMode();
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
}