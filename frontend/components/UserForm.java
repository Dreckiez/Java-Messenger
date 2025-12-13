package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.time.LocalDate;

import models.User;

public class UserForm extends JDialog {
    private JTextField usernameField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField addressField;
    private JComboBox<String> genderCombo;
    private JComboBox<String> dayCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<String> yearCombo;
    private JPasswordField passwordField;
    private JComboBox<String> statusBox;
    private boolean confirmed = false;

    public UserForm(Window parent, String title, User existingUser, Boolean type) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        setSize(500, 700);
        setLocationRelativeTo(parent);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        form.add(usernameField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        form.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        form.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        form.add(emailField, gbc);

        // Password (only for new users)
        gbc.gridx = 0;
        gbc.gridy = 4;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        form.add(passwordField, gbc);

        if (!type) { // 0 for new , 1 for edit
            gbc.gridx = 0;
            gbc.gridy = 5;
            form.add(new JLabel("Confirm Password:"), gbc);
            gbc.gridx = 1;
            passwordField = new JPasswordField(20);
            form.add(passwordField, gbc);
        } else {
            gbc.gridx = 0;
            gbc.gridy = 5;
            form.add(new JLabel("Gender:"), gbc);

            gbc.gridx = 1;
            String[] genders = { "Select Gender", "Male", "Female", "Hidden" };
            genderCombo = new JComboBox<>(genders);
            genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            genderCombo.setBackground(Color.WHITE);
            genderCombo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                    new EmptyBorder(8, 10, 8, 10)));
            genderCombo.setMaximumSize(new Dimension(400, 40));
            form.add(genderCombo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            form.add(new JLabel("Birthday:"), gbc);

            gbc.gridx = 1;
            form.add(createBirthdayPanel(), gbc);

            String gender = existingUser.getGender();
            if (gender != null && !gender.isEmpty()) {
                genderCombo.setSelectedItem(
                        gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase());
            }

            String birthday = existingUser.getBirthDay();

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

            gbc.gridx = 0;
            gbc.gridy = 7;
            form.add(new JLabel("Address:"), gbc);

            gbc.gridx = 1;
            form.add(new JTextField(20), gbc);

            // Status
            gbc.gridx = 0;
            gbc.gridy = 8;
            form.add(new JLabel("Status:"), gbc);
            gbc.gridx = 1;
            statusBox = new JComboBox<>(new String[] { "Active", "Locked" });
            form.add(statusBox, gbc);
        }

        // Load existing user info (edit mode)
        if (existingUser != null) {
            usernameField.setText(existingUser.getUsername());
            nameField.setText(existingUser.getFirstName() + existingUser.getLastName());
            emailField.setText(existingUser.getEmail());
            statusBox.setSelectedItem("Active");

            passwordField.setEnabled(false); // Can't change password here
        }

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getName() {
        return nameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getStatus() {
        return (String) statusBox.getSelectedItem();
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

        // Months with placeholder FIRST
        String[] months = {
                "Month", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        monthCombo.setPreferredSize(new Dimension(130, 40));
        monthCombo.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));

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

        panel.add(dayCombo);
        panel.add(monthCombo);
        panel.add(yearCombo);

        return panel;
    }

}
