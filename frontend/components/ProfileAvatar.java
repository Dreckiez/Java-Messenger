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

public class ProfileAvatar extends JPanel {
    private JLabel avatarLabel;
    private ImageEditor editor;
    private String currentAvatarUrl;
    private boolean editMode = false;
    private JButton editBtn;
    private JPanel actionPanel;

    public ProfileAvatar() {
        editor = new ImageEditor();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(20, 30, 20, 30)));

        // Section header with edit button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel sectionTitle = new JLabel("Profile Picture");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));

        editBtn = createEditButton();
        editBtn.addActionListener(e -> toggleEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(editBtn, BorderLayout.EAST);

        add(headerPanel);
        add(Box.createVerticalStrut(15));

        // Avatar display
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        avatarPanel.setBackground(Color.WHITE);
        avatarPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(100, 100));
        avatarPanel.add(avatarLabel);

        // Avatar action buttons (hidden by default)
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setVisible(false);

        JButton changeAvatarBtn = createSecondaryButton("Change Avatar");
        changeAvatarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        changeAvatarBtn.addActionListener(e -> handleChangeAvatar());

        JButton removeAvatarBtn = createDangerButton("Remove Avatar");
        removeAvatarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeAvatarBtn.addActionListener(e -> handleRemoveAvatar());

        JButton saveAvatarBtn = createPrimaryButton("Save");
        saveAvatarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveAvatarBtn.addActionListener(e -> saveChanges());

        JButton cancelAvatarBtn = createSecondaryButton("Cancel");
        cancelAvatarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelAvatarBtn.addActionListener(e -> cancelEdit());

        actionPanel.add(changeAvatarBtn);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(removeAvatarBtn);
        actionPanel.add(Box.createVerticalStrut(15));
        actionPanel.add(saveAvatarBtn);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(cancelAvatarBtn);

        avatarPanel.add(actionPanel);
        add(avatarPanel);

        loadUserData();
    }

    private void loadUserData() {
        User user = UserSession.getUser();
        if (user == null)
            return;

        currentAvatarUrl = user.getAvatar();
        if (currentAvatarUrl != null && !currentAvatarUrl.isEmpty()) {
            ImageLoader.loadImageAsync(currentAvatarUrl, new ImageLoader.ImageLoadCallback() {
                @Override
                public void onLoaded(Image img) {
                    if (avatarLabel != null) {
                        avatarLabel.setIcon(editor.makeCircularImage(img, 100));
                        revalidate();
                        repaint();
                    }
                }
            });
        }
    }

    private void toggleEditMode() {
        editMode = !editMode;
        actionPanel.setVisible(editMode);
        editBtn.setText(editMode ? "Cancel" : "Edit");
        revalidate();
        repaint();
    }

    private void saveChanges() {
        JOptionPane.showMessageDialog(this,
                "Avatar updated successfully! (API integration pending)",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        toggleEditMode();
    }

    private void cancelEdit() {
        loadUserData();
        toggleEditMode();
    }

    private void handleChangeAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
            Image img = icon.getImage();
            avatarLabel.setIcon(editor.makeCircularImage(img, 100));
        }
    }

    private void handleRemoveAvatar() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove your profile picture?",
                "Remove Avatar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            avatarLabel.setIcon(null);
            currentAvatarUrl = null;
        }
    }

    public void refreshData() {
        loadUserData();
        if (editMode)
            toggleEditMode();
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
}