package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;

import models.User;
import services.UserServices; // Import Service
import utils.ImageEditor;
import utils.ImageLoader;
import utils.UserSession;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class ProfileAvatar extends JPanel {
    private JLabel avatarLabel;
    private ImageEditor editor;
    private String currentAvatarUrl;
    private boolean editMode = false;
    private JButton editBtn;
    private JPanel actionPanel;
    private JPanel avatarContainer;
    
    private UserServices userService; // Add Service
    private File selectedFile; // Store selected file

    // --- COLORS ---
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private final Color BTN_BLUE = new Color(59, 130, 246);
    private final Color BTN_BLUE_HOVER = new Color(37, 99, 235);
    private final Color BTN_RED = new Color(239, 68, 68);
    private final Color BTN_RED_HOVER = new Color(220, 38, 38);

    public ProfileAvatar() {
        this.userService = new UserServices();
        editor = new ImageEditor();

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

        JLabel sectionTitle = new JLabel("Profile Picture");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sectionTitle.setForeground(TEXT_PRIMARY);

        editBtn = createEditButton();
        editBtn.addActionListener(e -> toggleEditMode());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(editBtn, BorderLayout.EAST);

        add(headerPanel);
        add(Box.createVerticalStrut(25));

        // --- AVATAR DISPLAY ---
        avatarContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        avatarContainer.setBackground(Color.WHITE);
        avatarContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(120, 120));
        avatarLabel.setIcon(createPlaceholderAvatar());

        avatarContainer.add(avatarLabel);
        add(avatarContainer);
        add(Box.createVerticalStrut(20));

        // --- ACTION BUTTONS ---
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setVisible(false);

        JPanel btnGroup1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        btnGroup1.setBackground(Color.WHITE);
        btnGroup1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton changeAvatarBtn = createSecondaryButton("Upload Photo");
        changeAvatarBtn.addActionListener(e -> handleChangeAvatar());

        JButton removeAvatarBtn = createDangerButton("Remove");
        // Gọi hàm xử lý xóa avatar mới
        removeAvatarBtn.addActionListener(e -> handleRemoveAvatar()); 

        btnGroup1.add(changeAvatarBtn);
        btnGroup1.add(removeAvatarBtn);

        JPanel btnGroup2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        btnGroup2.setBackground(Color.WHITE);
        btnGroup2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveAvatarBtn = createPrimaryButton("Save Changes");
        saveAvatarBtn.addActionListener(e -> saveChanges());

        JButton cancelAvatarBtn = createSecondaryButton("Cancel");
        cancelAvatarBtn.addActionListener(e -> cancelEdit());

        btnGroup2.add(saveAvatarBtn);
        btnGroup2.add(cancelAvatarBtn);

        actionPanel.add(btnGroup1);
        actionPanel.add(Box.createVerticalStrut(15));
        actionPanel.add(btnGroup2);

        add(actionPanel);

        loadUserData();
    }

    private void loadUserData() {
        User user = UserSession.getUser();
        if (user == null) return;

        currentAvatarUrl = user.getAvatar();
        // Nếu có URL thì load ảnh, nếu null/empty thì hiển thị placeholder mặc định
        if (currentAvatarUrl != null && !currentAvatarUrl.isEmpty()) {
            ImageLoader.loadImageAsync(currentAvatarUrl, new ImageLoader.ImageLoadCallback() {
                @Override
                public void onLoaded(Image img) {
                    if (avatarLabel != null && img != null) {
                        avatarLabel.setIcon(editor.makeCircularImage(img, 120));
                        revalidate(); repaint();
                    }
                }
            });
        } else {
            avatarLabel.setIcon(createPlaceholderAvatar());
        }
    }

    private ImageIcon createPlaceholderAvatar() {
        int size = 120;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(226, 232, 240));
        g2.fillOval(0, 0, size, size);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 40));

        String text = "?";
        if (UserSession.getUser() != null && UserSession.getUser().getUsername() != null) {
            text = UserSession.getUser().getUsername().substring(0, 1).toUpperCase();
        }

        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(text)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, x, y);
        g2.dispose();

        return new ImageIcon(img);
    }

    private void toggleEditMode() {
        editMode = !editMode;
        actionPanel.setVisible(editMode);
        editBtn.setText(editMode ? "Cancel" : "Edit");
        revalidate(); repaint();
    }

    private void saveChanges() {
        // Nếu chưa chọn file thì báo lỗi hoặc thoát edit
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "No new image selected.", "Info", JOptionPane.INFORMATION_MESSAGE);
            // toggleEditMode(); // Có thể giữ edit mode để user chọn lại
            return;
        }

        String token = UserSession.getUser().getToken();

        // Upload file qua API
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                return userService.updateAvatar(token, selectedFile);
            }

            @Override
            protected void done() {
                try {
                    JSONObject result = get();
                    if (result.has("error")) {
                        JOptionPane.showMessageDialog(ProfileAvatar.this, 
                            "Upload Failed:\n" + result.getString("error"), 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Thành công: Cập nhật UI và Session
                        String newUrl = result.optString("avatarUrl");
                        if (newUrl != null && !newUrl.isEmpty()) {
                            UserSession.getUser().setAvatar(newUrl); // Cập nhật session
                            currentAvatarUrl = newUrl; 
                            
                            // 🔥 THÔNG BÁO CHO NAVBAR CẬP NHẬT
                            UserSession.fireUserUpdated();

                            JOptionPane.showMessageDialog(ProfileAvatar.this, 
                                "Avatar updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            
                            toggleEditMode();
                            selectedFile = null; // Xóa file chọn tạm
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ProfileAvatar.this, "An error occurred during upload.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void cancelEdit() {
        selectedFile = null;
        loadUserData(); // Load lại avatar cũ
        toggleEditMode();
    }

    private void handleChangeAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            // Preview ảnh vừa chọn
            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
            Image img = icon.getImage();
            avatarLabel.setIcon(editor.makeCircularImage(img, 120));
        }
    }

    // 🔥 HÀM XÓA AVATAR (GỌI API DELETE)
    private void handleRemoveAvatar() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove your profile picture?", 
            "Confirm Remove", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm != JOptionPane.YES_OPTION) return;

        String token = UserSession.getUser().getToken();

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                return userService.removeAvatar(token);
            }

            @Override
            protected void done() {
                try {
                    JSONObject result = get();
                    
                    if (result.has("error")) {
                        JOptionPane.showMessageDialog(ProfileAvatar.this, 
                            "Remove Failed:\n" + result.getString("error"), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ProfileAvatar.this, 
                            "Avatar removed successfully!", 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);

                        // 1. Reset UI về placeholder
                        avatarLabel.setIcon(createPlaceholderAvatar());
                        
                        // 2. Reset dữ liệu cục bộ
                        currentAvatarUrl = null;
                        selectedFile = null;

                        // 3. Cập nhật Session (set avatar = null)
                        UserSession.getUser().setAvatar(null); 
                        
                        // 🔥 4. THÔNG BÁO CHO NAVBAR CẬP NHẬT
                        UserSession.fireUserUpdated();
                        
                        // Thoát chế độ edit
                        if (editMode) toggleEditMode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ProfileAvatar.this, 
                        "An error occurred.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    public void refreshData() {
        loadUserData();
        if (editMode) toggleEditMode();
    }

    // --- BUTTON CREATION ---

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

    private JButton createDangerButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, new Color(254, 226, 226), BTN_RED, new Color(252, 165, 165), BTN_RED, false);
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
}