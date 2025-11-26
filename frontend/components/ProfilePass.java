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

    public ProfilePass() {
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

        JLabel sectionTitle = new JLabel("Change Password");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));

        editBtn = createEditButton();
        editBtn.addActionListener(e -> toggleEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(editBtn, BorderLayout.EAST);

        add(headerPanel);
        add(Box.createVerticalStrut(15));

        // Password fields (hidden by default)
        fieldsPanel = createFieldsPanel();
        fieldsPanel.setVisible(false);
        add(fieldsPanel);

        // Action buttons
        actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setVisible(false);

        JButton saveBtn = createPrimaryButton("Change Password");
        saveBtn.addActionListener(e -> saveChanges());

        JButton cancelBtn = createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> cancelEdit());

        actionPanel.add(saveBtn);
        actionPanel.add(cancelBtn);

        add(Box.createVerticalStrut(10));
        add(actionPanel);
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

        // Current Password
        gbc.gridx = 0;
        gbc.gridy = row++;
        panel.add(createLabel("Current Password"), gbc);

        gbc.gridy = row++;
        currentPasswordField = createPasswordField(30);
        panel.add(currentPasswordField, gbc);

        // New Password
        gbc.gridy = row++;
        panel.add(createLabel("New Password"), gbc);

        gbc.gridy = row++;
        newPasswordField = createPasswordField(30);
        panel.add(newPasswordField, gbc);

        // Confirm Password
        gbc.gridy = row++;
        panel.add(createLabel("Confirm New Password"), gbc);

        gbc.gridy = row++;
        confirmPasswordField = createPasswordField(30);
        panel.add(confirmPasswordField, gbc);

        return panel;
    }

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

        revalidate();
        repaint();
    }

    private void saveChanges() {
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

        JOptionPane.showMessageDialog(this,
                "Password changed successfully! (API integration pending)",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

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

    public void refreshData() {
        if (editMode)
            toggleEditMode();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(73, 80, 87));
        return label;
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
}