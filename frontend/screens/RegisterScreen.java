package screens;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.json.JSONObject;

import utils.ApiClient;
import utils.ApiUrl;

public class RegisterScreen extends JPanel {
    private BaseScreen mainScreen;
    private JLabel registerError;
    // --- COLORS ---
    private final Color PRIMARY_COLOR = new Color(59, 130, 246); // Blue
    private final Color HOVER_COLOR = new Color(37, 99, 235);
    private final Color BG_COLOR = new Color(248, 250, 252);     
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private final Color INPUT_BG = new Color(241, 245, 249);     

    public RegisterScreen(BaseScreen main) {
        this.mainScreen = main;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // Split Screen Layout
        JPanel container = new JPanel(new GridLayout(1, 2));
        container.add(createLeftPanel());
        container.add(createRightPanel());
        
        add(container, BorderLayout.CENTER);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setLayout(new GridBagLayout());
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        JLabel logoLabel = new JLabel("Join Us!");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel sloganLabel = new JLabel("Create an account to start chatting.");
        sloganLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        sloganLabel.setForeground(new Color(255, 255, 255, 200));
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(logoLabel);
        content.add(Box.createVerticalStrut(20));
        content.add(sloganLabel);
        
        panel.add(content);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Form Container
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.WHITE);
        formContent.setBorder(new EmptyBorder(40, 60, 40, 60)); 

        // 1. Header
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subTitle = new JLabel("Sign up for free.");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(TEXT_SECONDARY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formContent.add(title);
        formContent.add(Box.createVerticalStrut(10));
        formContent.add(subTitle);
        formContent.add(Box.createVerticalStrut(30));

        // 2. Error Label
        registerError = new JLabel(" ");
        registerError.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerError.setForeground(new Color(239, 68, 68)); // Red
        registerError.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContent.add(registerError);
        formContent.add(Box.createVerticalStrut(10));

        // 3. Form Fields
        // Name Row
        JPanel nameRow = new JPanel(new GridLayout(1, 2, 15, 0));
        nameRow.setBackground(Color.WHITE);
        nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel fNamePanel = new JPanel(new BorderLayout());
        fNamePanel.setBackground(Color.WHITE);
        fNamePanel.add(createLabel("First Name"), BorderLayout.NORTH);
        JTextField fNameField = createStyledTextField();
        fNamePanel.add(wrapInRoundedPanel(fNameField), BorderLayout.CENTER);
        
        JPanel lNamePanel = new JPanel(new BorderLayout());
        lNamePanel.setBackground(Color.WHITE);
        lNamePanel.add(createLabel("Last Name"), BorderLayout.NORTH);
        JTextField lNameField = createStyledTextField();
        lNamePanel.add(wrapInRoundedPanel(lNameField), BorderLayout.CENTER);

        nameRow.add(fNamePanel);
        nameRow.add(lNamePanel);
        formContent.add(nameRow);
        formContent.add(Box.createVerticalStrut(15));

        // Username
        formContent.add(createLabel("Username"));
        formContent.add(Box.createVerticalStrut(5));
        JTextField userField = createStyledTextField();
        formContent.add(wrapInRoundedPanel(userField));
        formContent.add(Box.createVerticalStrut(15));

        // Email
        formContent.add(createLabel("Email"));
        formContent.add(Box.createVerticalStrut(5));
        JTextField emailField = createStyledTextField();
        formContent.add(wrapInRoundedPanel(emailField));
        formContent.add(Box.createVerticalStrut(15));

        // Password
        formContent.add(createLabel("Password"));
        formContent.add(Box.createVerticalStrut(5));
        JPasswordField passField = createStyledPasswordField();
        JPanel passPanel = wrapInRoundedPanel(passField);
        // 🔥 ICON MẮT
        JToggleButton eyeBtn1 = createEyeButton(passField);
        passPanel.add(eyeBtn1, BorderLayout.EAST);
        formContent.add(passPanel);
        formContent.add(Box.createVerticalStrut(15));

        // Confirm Password
        formContent.add(createLabel("Confirm Password"));
        formContent.add(Box.createVerticalStrut(5));
        JPasswordField confirmField = createStyledPasswordField();
        JPanel confirmPanel = wrapInRoundedPanel(confirmField);
        // 🔥 ICON MẮT
        JToggleButton eyeBtn2 = createEyeButton(confirmField);
        confirmPanel.add(eyeBtn2, BorderLayout.EAST);
        formContent.add(confirmPanel);
        formContent.add(Box.createVerticalStrut(30));

        // 4. Register Button
        JButton regBtn = new JButton("Sign Up");
        styleButton(regBtn, PRIMARY_COLOR, Color.WHITE);
        regBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        regBtn.addActionListener(e -> performRegister(
            userField, fNameField, lNameField, emailField, passField, confirmField, regBtn
        ));
        
        formContent.add(regBtn);
        formContent.add(Box.createVerticalStrut(20));

        // 5. Login Link
        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginLinkPanel.setBackground(Color.WHITE);
        loginLinkPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel hasAcc = new JLabel("Already have an account? ");
        hasAcc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hasAcc.setForeground(TEXT_SECONDARY);
        
        JLabel loginLink = new JLabel("Log in");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginLink.setForeground(PRIMARY_COLOR);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { mainScreen.showPanel("login"); }
        });
        
        loginLinkPanel.add(hasAcc);
        loginLinkPanel.add(loginLink);
        formContent.add(loginLinkPanel);

        JScrollPane scrollPane = new JScrollPane(formContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // --- LOGIC ---

    private void performRegister(
            JTextField userF, JTextField fNameF, JTextField lNameF, JTextField emailF,
            JPasswordField passF, JPasswordField confirmF, JButton btn) {
        
        String username = userF.getText().trim();
        String firstname = fNameF.getText().trim();
        String lastname = lNameF.getText().trim();
        String email = emailF.getText().trim();
        String password = new String(passF.getPassword()).trim();
        String confirm = new String(confirmF.getPassword()).trim();

        // 1. Validate Client-side (Lỗi nhập liệu cơ bản)
        // Hiển thị lỗi ngay trên form (inline) vì đây là lỗi thao tác nhanh
        if (username.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || 
            email.isEmpty() || password.isEmpty()) {
            showInlineError("Please fill in all fields."); 
            return;
        }

        if (!password.equals(confirm)) {
            showInlineError("Passwords do not match.");
            return;
        }

        // Xóa lỗi cũ nếu có
        registerError.setText(" "); 

        // 2. UI Loading State
        btn.setText("Creating Account...");
        btn.setEnabled(false);
        btn.setBackground(new Color(147, 197, 253)); // Màu nhạt hơn để báo hiệu đang loading

        // 3. Prepare Data
        JSONObject payload = new JSONObject();
        payload.put("username", username);
        payload.put("firstName", firstname); 
        payload.put("lastName", lastname);  
        payload.put("email", email);
        payload.put("password", password);
        payload.put("confirmPassword", confirm); 

        // 4. Call API Background
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() {
                try {
                    // Giả lập delay mạng xíu để thấy hiệu ứng loading (Optional)
                    // Thread.sleep(500); 
                    return ApiClient.postJSON(ApiUrl.REGISTER, payload, null);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    JSONObject res = get();
                    
                    // Trường hợp mất mạng hoặc lỗi connection
                    if (res == null) {
                        showErrorDialog("Connection Error", "Could not connect to the server.\nPlease check your internet.");
                        return;
                    }

                    int status = res.optInt("httpStatus", 0);
                    
                    // --- TRƯỜNG HỢP THÀNH CÔNG (200 OK) ---
                    if (status == 200) {
                        // Hiển thị thông báo đẹp
                        JOptionPane.showMessageDialog(RegisterScreen.this, 
                            "Account created successfully!\nYou can now log in.", 
                            "Welcome Aboard", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Chuyển màn hình
                        mainScreen.showPanel("login");
                    } 
                    // --- TRƯỜNG HỢP LỖI TỪ BACKEND ---
                    else {
                        // Lấy message lỗi từ JSON trả về
                        // Backend thường trả: { "message": "Username already exists", ... }
                        String errorMsg = res.optString("message", "Registration failed");
                        
                        // Nếu backend trả về lỗi chi tiết trong object "error" (tùy cấu trúc backend của bạn)
                        if (res.has("error") && !res.optString("error").isEmpty()) {
                             // Ưu tiên hiển thị message dễ hiểu nếu có
                             if(errorMsg.equals("Registration failed")) {
                                 errorMsg = res.optString("error");
                             }
                        }

                        // Hiển thị Popup lỗi đẹp
                        showErrorDialog("Registration Failed", errorMsg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorDialog("System Error", "An unexpected error occurred.\nPlease try again later.");
                } finally {
                    // Reset nút bấm về trạng thái ban đầu
                    btn.setText("Sign Up");
                    btn.setEnabled(true);
                    btn.setBackground(PRIMARY_COLOR);
                }
            }
        }.execute();
    }

    // --- CÁC HÀM HIỂN THỊ LỖI HELPER ---

    // 1. Hiển thị lỗi nhỏ ngay trên form (cho validate nhanh)
    private void showInlineError(String msg) {
        registerError.setText(msg);
        registerError.setForeground(new Color(239, 68, 68)); // Màu đỏ
        // Hiệu ứng rung nhẹ (Shake) nếu muốn pro hơn (Optional)
    }

    // 2. Hiển thị lỗi to bằng Dialog (cho lỗi Backend)
    private void showErrorDialog(String title, String content) {
        // Xóa lỗi inline để giao diện sạch sẽ
        registerError.setText(" "); 
        
        JOptionPane.showMessageDialog(this, 
            content, 
            title, 
            JOptionPane.ERROR_MESSAGE);
    }

    // --- UI HELPERS ---

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(INPUT_BG);
        field.setBorder(null);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    // 🔥 FIX: Icon mắt chuẩn (vẽ cả viền oval và đồng tử) + Bỏ border vuông nút bấm
    private JToggleButton createEyeButton(JPasswordField field) {
        JToggleButton eyeBtn = new JToggleButton();
        eyeBtn.setPreferredSize(new Dimension(40, 30));
        
        // --- QUAN TRỌNG: Bỏ toàn bộ viền nút ---
        eyeBtn.setBorder(null);
        eyeBtn.setBorderPainted(false);
        eyeBtn.setContentAreaFilled(false);
        eyeBtn.setFocusPainted(false);
        // ---------------------------------------
        
        eyeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        eyeBtn.setUI(new javax.swing.plaf.basic.BasicToggleButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = c.getWidth();
                int h = c.getHeight();
                int iconW = 18; // Chiều rộng icon
                int iconH = 12; // Chiều cao icon
                int x = (w - iconW) / 2;
                int y = (h - iconH) / 2;

                g2.setColor(TEXT_SECONDARY);
                g2.setStroke(new BasicStroke(1.5f)); // Độ dày nét vẽ

                if (eyeBtn.isSelected()) {
                    // --- Hiện Pass: Vẽ mắt mở ---
                    g2.drawOval(x, y, iconW, iconH); // Vẽ khung mắt (Oval)
                    g2.fillOval(x + 6, y + 3, 6, 6); // Vẽ đồng tử đặc
                } else {
                    // --- Ẩn Pass: Vẽ mắt + Gạch chéo ---
                    g2.drawOval(x, y, iconW, iconH); // Khung mắt
                    g2.fillOval(x + 6, y + 3, 6, 6); // Đồng tử
                    
                    // Gạch chéo
                    g2.setColor(TEXT_SECONDARY);
                    g2.drawLine(x + 2, y + 2, x + iconW - 2, y + iconH - 2);
                }
                g2.dispose();
            }
        });

        eyeBtn.addActionListener(e -> {
            if (eyeBtn.isSelected()) {
                field.setEchoChar((char) 0);
            } else {
                field.setEchoChar('•');
            }
        });
        return eyeBtn;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if(btn.isEnabled()) btn.setBackground(HOVER_COLOR); }
            public void mouseExited(MouseEvent e) { if(btn.isEnabled()) btn.setBackground(bg); }
        });

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                super.paint(g2, c);
                g2.dispose();
            }
        });
    }
}