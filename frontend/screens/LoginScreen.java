package screens;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.json.JSONObject;

import models.User;
import services.UserServices;
import utils.ApiClient;
import utils.ApiUrl;
import utils.UserSession;

public class LoginScreen extends JPanel {
    private BaseScreen mainScreen;
    private JLabel loginError;
    private boolean passwordVisible = false;
    
    // --- COLORS ---
    private final Color PRIMARY_COLOR = new Color(59, 130, 246); // Blue
    private final Color HOVER_COLOR = new Color(37, 99, 235);
    private final Color BG_COLOR = new Color(248, 250, 252);     // Very light gray
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private final Color INPUT_BG = new Color(241, 245, 249);     // Input background

    public LoginScreen(BaseScreen main) {
        this.mainScreen = main;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // --- LEFT SIDE: ILLUSTRATION / BRANDING ---
        JPanel leftPanel = createLeftPanel();
        
        // --- RIGHT SIDE: LOGIN FORM ---
        JPanel rightPanel = createRightPanel();

        // Responsive split
        JPanel container = new JPanel(new GridLayout(1, 2));
        container.add(leftPanel);
        container.add(rightPanel);
        
        add(container, BorderLayout.CENTER);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setLayout(new GridBagLayout());
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        JLabel logoLabel = new JLabel("ChatApp");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel sloganLabel = new JLabel("Connect with friends effortlessly.");
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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Main Form Container
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(new EmptyBorder(50, 60, 50, 60)); 
        formContainer.setPreferredSize(new Dimension(450, 600)); 

        // 1. Header
        JLabel title = new JLabel("Welcome Back!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subTitle = new JLabel("Please enter your details.");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(TEXT_SECONDARY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formContainer.add(title);
        formContainer.add(Box.createVerticalStrut(10));
        formContainer.add(subTitle);
        formContainer.add(Box.createVerticalStrut(40));

        // 2. Error Label
        loginError = new JLabel(" ");
        loginError.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginError.setForeground(new Color(239, 68, 68)); // Red
        loginError.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(loginError);
        formContainer.add(Box.createVerticalStrut(10));

        // 3. Username Field
        formContainer.add(createLabel("Username"));
        formContainer.add(Box.createVerticalStrut(8));
        JTextField userField = createStyledTextField();
        formContainer.add(wrapInRoundedPanel(userField));
        formContainer.add(Box.createVerticalStrut(20));

        // 4. Password Field
        formContainer.add(createLabel("Password"));
        formContainer.add(Box.createVerticalStrut(8));
        
        JPasswordField passField = createStyledPasswordField();
        JPanel passPanel = wrapInRoundedPanel(passField);
        
        // 🔥 SỬ DỤNG HÀM TẠO ICON MẮT CHUẨN (ĐỒNG BỘ VỚI REGISTER)
        JToggleButton eyeBtn = createEyeButton(passField);
        passPanel.add(eyeBtn, BorderLayout.EAST);
        
        formContainer.add(passPanel);
        formContainer.add(Box.createVerticalStrut(10));

        // 5. Forgot Password Link
        JLabel forgotPass = new JLabel("Forgot password?");
        forgotPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        forgotPass.setForeground(PRIMARY_COLOR);
        forgotPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgotPass.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { mainScreen.showPanel("forgotPassword"); }
        });
        
        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        forgotPanel.setBackground(Color.WHITE);
        forgotPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgotPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        forgotPanel.add(forgotPass);
        formContainer.add(forgotPanel);
        
        formContainer.add(Box.createVerticalStrut(30));

        // 6. Login Button
        JButton loginBtn = new JButton("Log in");
        styleButton(loginBtn, PRIMARY_COLOR, Color.WHITE);
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        loginBtn.addActionListener(e -> performLogin(userField, passField, loginBtn));
        
        formContainer.add(loginBtn);
        formContainer.add(Box.createVerticalStrut(20));

        // 7. Register Link
        JPanel regPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        regPanel.setBackground(Color.WHITE);
        regPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel noAcc = new JLabel("Don't have an account? ");
        noAcc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noAcc.setForeground(TEXT_SECONDARY);
        
        JLabel regLink = new JLabel("Sign up");
        regLink.setFont(new Font("Segoe UI", Font.BOLD, 14));
        regLink.setForeground(PRIMARY_COLOR);
        regLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { mainScreen.showPanel("register"); }
        });
        
        regPanel.add(noAcc);
        regPanel.add(regLink);
        formContainer.add(regPanel);

        // Center the formContainer in the right panel
        panel.add(formContainer);
        return panel;
    }

    // --- LOGIC ---

    private void performLogin(JTextField userField, JPasswordField passField, JButton loginBtn) {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            loginError.setText("Please enter both username and password");
            return;
        }

        loginBtn.setText("Logging in...");
        loginBtn.setEnabled(false);
        loginBtn.setBackground(new Color(147, 197, 253)); // Lighter blue

        JSONObject payload = new JSONObject();
        payload.put("username", username);
        payload.put("password", password);

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() {
                return ApiClient.postJSON(ApiUrl.LOGIN, payload, null);
            }

            @Override
            protected void done() {
                try {
                    JSONObject res = get();
                    int status = res.optInt("httpStatus", 0);
                    
                    if (status == 200) {
                        String token = res.getString("token");
                        String refreshToken = res.getString("refreshToken");
                        User user = new User(token, refreshToken);
                        UserSession.setUser(user);
                        
                        // Fetch profile
                        fetchUserProfile(token);
                    } else {
                        String msg = res.optString("message", "Login failed");
                        loginError.setText(msg);
                        resetButton(loginBtn);
                    }
                } catch (Exception e) {
                    loginError.setText("Server error. Please try again.");
                    e.printStackTrace();
                    resetButton(loginBtn);
                }
            }
        }.execute();
    }

    private void fetchUserProfile(String token) {
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() {
                return UserServices.getMyProfile(token);
            }
            @Override
            protected void done() {
                try {
                    JSONObject profile = get();
                    
                    int user_id = profile.getInt("userId");
                    String username = profile.optString("username", ""); 
                    String role = profile.getString("role");
                    String avatar = profile.optString("avatarUrl", null);
                    String email = profile.getString("email");
                    String fname = profile.getString("firstName");
                    String lname = profile.getString("lastName");
                    String gender = profile.getString("gender");
                    String address = profile.getString("address");
                    String birthDay = profile.optString("birthDay", "");

                    UserSession.setUserInfo(user_id, username, avatar, role, address, gender, birthDay, email, fname, lname);

                    if (role.equals("ADMIN")) {
                        mainScreen.showPanel("dashboard");
                        if (mainScreen.getDashboardScreen() != null) 
                            mainScreen.getDashboardScreen().onLoginSuccess();;
                           
                    } else {
                        mainScreen.showPanel("home");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loginError.setText("Failed to load profile.");
                }
            }
        }.execute();
    }

    private void resetButton(JButton btn) {
        btn.setText("Log in");
        btn.setEnabled(true);
        btn.setBackground(PRIMARY_COLOR);
    }

    // --- UI HELPERS ---

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
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

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
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
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    // 🔥 HELPER: Tạo icon mắt chuẩn (Hình Oval + Đồng tử + Gạch chéo)
    // Code này giống hệt bên RegisterScreen để đồng bộ
    private JToggleButton createEyeButton(JPasswordField field) {
        JToggleButton eyeBtn = new JToggleButton();
        eyeBtn.setPreferredSize(new Dimension(40, 30));
        
        // --- Bỏ toàn bộ viền nút ---
        eyeBtn.setBorder(null);
        eyeBtn.setBorderPainted(false);
        eyeBtn.setContentAreaFilled(false);
        eyeBtn.setFocusPainted(false);
        // ---------------------------
        
        eyeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        eyeBtn.setUI(new javax.swing.plaf.basic.BasicToggleButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = c.getWidth();
                int h = c.getHeight();
                int x = (w - 20) / 2; // Căn giữa icon
                int y = (h - 12) / 2;

                g2.setColor(TEXT_SECONDARY); // Màu xám icon

                if (eyeBtn.isSelected()) {
                    // --- Trạng thái hiện mật khẩu ---
                    // Vẽ khung mắt (Oval)
                    g2.setStroke(new BasicStroke(1.5f)); // Viền mắt mảnh
                    g2.drawOval(x, y, 18, 12);
                    
                    // Vẽ đồng tử (Hình tròn đặc)
                    g2.fillOval(x + 6, y + 3, 6, 6);
                } else {
                    // --- Trạng thái ẩn mật khẩu ---
                    // Vẽ khung mắt
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawOval(x, y, 18, 12);
                    
                    // Vẽ đồng tử
                    g2.fillOval(x + 6, y + 3, 6, 6);
                    
                    // Vẽ đường gạch chéo
                    g2.setStroke(new BasicStroke(2f)); // Gạch chéo đậm hơn chút
                    g2.drawLine(x + 4, y + 2, x + 16, y + 10);
                }
                g2.dispose();
            }
        });

        eyeBtn.addActionListener(e -> {
            if (eyeBtn.isSelected()) {
                field.setEchoChar((char) 0); // Hiện chữ
            } else {
                field.setEchoChar('•'); // Ẩn chữ
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