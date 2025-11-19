package screens;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
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
    private JPanel loginCard;
    private boolean passwordVisible = false;

    public LoginScreen(BaseScreen main) {
        mainScreen = main;

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 242, 245));

        loginCard = new JPanel();
        loginCard.setLayout(new GridBagLayout());
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(40, 50, 40, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        gbc.insets = new Insets(0, 0, 10, 0);
        loginCard.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        gbc.insets = new Insets(0, 0, 30, 0);
        loginCard.add(subtitleLabel, gbc);

        // Error label
        loginError = new JLabel("");
        loginError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginError.setForeground(new Color(220, 53, 69));
        loginError.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 0, 15, 0);
        loginCard.add(loginError, gbc);

        // Username label
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(73, 80, 87));
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        loginCard.add(userLabel, gbc);

        // Username field
        JTextField userField = new JTextField(20);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        loginCard.add(userField, gbc);

        // Password label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setForeground(new Color(73, 80, 87));
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        loginCard.add(passLabel, gbc);

        // Password field container
        JPanel passContainer = new JPanel();
        passContainer.setLayout(new GridBagLayout());
        passContainer.setBackground(Color.WHITE);

        GridBagConstraints passGbc = new GridBagConstraints();
        passGbc.gridx = 0;
        passGbc.gridy = 0;
        passGbc.weightx = 1.0;
        passGbc.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        passContainer.add(passField, passGbc);

        passGbc.gridx = 1;
        passGbc.weightx = 0;
        passGbc.insets = new Insets(0, 5, 0, 0);

        JButton togglePassBtn = new JButton("Show");
        togglePassBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        togglePassBtn.setForeground(new Color(13, 110, 253));
        togglePassBtn.setBackground(Color.WHITE);
        togglePassBtn.setFocusable(false);
        togglePassBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 15, 10, 15)));
        togglePassBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        togglePassBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                togglePassBtn.setBackground(new Color(240, 242, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                togglePassBtn.setBackground(Color.WHITE);
            }
        });

        togglePassBtn.addActionListener(e -> {
            passwordVisible = !passwordVisible;
            if (passwordVisible) {
                passField.setEchoChar((char) 0);
                togglePassBtn.setText("Hide");
            } else {
                passField.setEchoChar('•');
                togglePassBtn.setText("Show");
            }
        });

        passContainer.add(togglePassBtn, passGbc);

        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        loginCard.add(passContainer, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(13, 110, 253));
        loginButton.setFocusable(false);
        loginButton.setBorder(new EmptyBorder(12, 0, 12, 0));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (loginButton.isEnabled()) {
                    loginButton.setBackground(new Color(11, 94, 215));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(13, 110, 253));
            }
        });

        loginButton.addActionListener(e -> {

            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                loginError.setText("Please enter both username and password");
                return;
            }

            JSONObject payload = new JSONObject();

            payload.put("username", username);
            payload.put("password", password);

            loginButton.setText("Logging in...");
            loginButton.setEnabled(false);
            loginButton.setBackground(new Color(108, 117, 125));

            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() {
                    return ApiClient.postJSON(ApiUrl.LOGIN, payload, null);
                }

                @Override
                protected void done() {
                    try {
                        JSONObject res = get(); // get() returns result of doInBackground()

                        int status = res.optInt("httpStatus", 0);
                        if (status == 200) {
                            String token = res.getString("token");
                            String refreshToken = res.getString("refreshToken");

                            User user = new User(token, refreshToken);
                            UserSession.setUser(user);

                            // Start another SwingWorker to fetch user profile
                            new SwingWorker<JSONObject, Void>() {

                                @Override
                                protected JSONObject doInBackground() {
                                    return UserServices.getMyProfile(token);
                                }

                                @Override
                                protected void done() {
                                    try {
                                        JSONObject profile = get(); // result from UserService

                                        String role = profile.getString("role");
                                        String avatar = profile.getString("avatarUrl");
                                        int user_id = profile.getInt("userId");

                                        UserSession.setUserInfo(user_id, username, avatar, role);

                                        if (role.equals("ADMIN")) {
                                            mainScreen.showPanel("dashboard");
                                        } else {
                                            mainScreen.showPanel("home");
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        loginError.setText("⚠️ Failed to load profile.");
                                    }
                                }

                            }.execute();

                        } else {
                            // Failure → show error
                            String msg = res.optString("message", "Login failed");
                            loginError.setText(msg);
                        }
                    } catch (Exception e) {
                        loginError.setText("⚠️ Server unreachable or unknown error");
                        e.printStackTrace();
                    } finally {
                        // Re-enable button
                        loginButton.setText("Login");
                        loginButton.setEnabled(true);
                        loginButton.setBackground(new Color(13, 110, 253));
                    }
                }
            }.execute();
        });

        gbc.insets = new Insets(0, 0, 20, 0);
        loginCard.add(loginButton, gbc);

        // Register section
        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setLayout(new GridBagLayout());

        GridBagConstraints regGbc = new GridBagConstraints();
        regGbc.gridx = 0;
        regGbc.gridy = 0;
        regGbc.insets = new Insets(0, 0, 0, 5);

        JLabel registerPrefix = new JLabel("Don't have an account?");
        registerPrefix.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerPrefix.setForeground(new Color(108, 117, 125));
        registerPanel.add(registerPrefix, regGbc);

        regGbc.gridx = 1;
        regGbc.insets = new Insets(0, 0, 0, 0);

        JLabel registerLink = new JLabel("Register");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerLink.setForeground(new Color(13, 110, 253));
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainScreen.showPanel("register");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registerLink.setForeground(new Color(11, 94, 215));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerLink.setForeground(new Color(13, 110, 253));
            }
        });
        registerPanel.add(registerLink, regGbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        loginCard.add(registerPanel, gbc);

        // Add the card to the main panel (centered)
        GridBagConstraints mainGbc = new GridBagConstraints();
        add(loginCard, mainGbc);

        // Add component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
    }
}
