package screens;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.json.JSONObject;

import utils.ApiClient;
import utils.ApiUrl;

public class ForgotPass extends JPanel {
    private BaseScreen mainScreen;
    private JLabel messageLabel;
    
    // --- COLORS ---
    private final Color PRIMARY_COLOR = new Color(59, 130, 246); // Blue
    private final Color HOVER_COLOR = new Color(37, 99, 235);
    private final Color BG_COLOR = new Color(248, 250, 252);     
    private final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private final Color INPUT_BG = new Color(241, 245, 249);     

    public ForgotPass(BaseScreen main) {
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
        
        JLabel iconLabel = new JLabel("🔒"); // Hoặc dùng icon ảnh khóa
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Forgot Password?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel("No worries, we'll send you reset instructions.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(new Color(255, 255, 255, 200));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(iconLabel);
        content.add(Box.createVerticalStrut(20));
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(descLabel);
        
        panel.add(content);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Form Container
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.WHITE);
        formContent.setBorder(new EmptyBorder(50, 60, 50, 60)); 
        formContent.setPreferredSize(new Dimension(450, 500)); 

        // 1. Header (Mobile friendly / Small screen fallback)
        JLabel title = new JLabel("Reset Password");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subTitle = new JLabel("Enter the email associated with your account.");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(TEXT_SECONDARY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formContent.add(title);
        formContent.add(Box.createVerticalStrut(10));
        formContent.add(subTitle);
        formContent.add(Box.createVerticalStrut(30));

        // 2. Message Label
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContent.add(messageLabel);
        formContent.add(Box.createVerticalStrut(10));

        // 3. Email Field
        formContent.add(createLabel("Email Address"));
        formContent.add(Box.createVerticalStrut(8));
        JTextField emailField = createStyledTextField();
        formContent.add(wrapInRoundedPanel(emailField));
        formContent.add(Box.createVerticalStrut(30));

        // 4. Reset Button
        JButton resetBtn = new JButton("Send Instructions");
        styleButton(resetBtn, PRIMARY_COLOR, Color.WHITE, false);
        resetBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        resetBtn.addActionListener(e -> performReset(emailField, resetBtn));
        
        formContent.add(resetBtn);
        formContent.add(Box.createVerticalStrut(20));

        // 5. Back to Login
        JButton backBtn = new JButton("← Back to Log in");
        styleButton(backBtn, Color.WHITE, TEXT_SECONDARY, true); // Ghost button style
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        backBtn.addActionListener(e -> mainScreen.showPanel("login"));
        
        formContent.add(backBtn);

        panel.add(formContent);
        return panel;
    }

    // --- LOGIC ---

    private void performReset(JTextField emailField, JButton btn) {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showError("Please enter your email address.");
            return;
        }

        btn.setText("Sending...");
        btn.setEnabled(false);
        btn.setBackground(new Color(147, 197, 253));
        messageLabel.setText(" ");

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() {
                JSONObject payload = new JSONObject();
                payload.put("email", email);
                return ApiClient.postJSON(ApiUrl.FORGOT_PASSWORD, payload, null);
            }

            @Override
            protected void done() {
                try {
                    JSONObject res = get();
                    int status = res.optInt("httpStatus", 0);

                    if (status == 200) {
                        showSuccess("Reset instructions sent! Check your email.");
                        emailField.setText("");
                        
                        // Auto redirect
                        Timer timer = new Timer(3000, e -> mainScreen.showPanel("login"));
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        String msg = res.optString("message", "Failed to send email.");
                        showError(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Server error. Please try again.");
                } finally {
                    btn.setText("Send Instructions");
                    btn.setEnabled(true);
                    btn.setBackground(PRIMARY_COLOR);
                }
            }
        }.execute();
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setForeground(new Color(220, 53, 69)); // Red
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setForeground(new Color(34, 197, 94)); // Green
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

    private void styleButton(JButton btn, Color bg, Color fg, boolean isGhost) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                if(btn.isEnabled()) {
                    if (isGhost) btn.setForeground(PRIMARY_COLOR); // Ghost hover text color
                    else btn.setBackground(HOVER_COLOR); // Solid hover bg color
                }
            }
            public void mouseExited(MouseEvent e) { 
                if(btn.isEnabled()) {
                    if (isGhost) btn.setForeground(fg);
                    else btn.setBackground(bg); 
                }
            }
        });

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                if (!isGhost) { // Chỉ vẽ nền nếu không phải nút Ghost
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(btn.getBackground());
                    g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                    super.paint(g2, c);
                    g2.dispose();
                } else {
                    super.paint(g, c); // Nút Ghost chỉ vẽ chữ
                }
            }
        });
    }
}