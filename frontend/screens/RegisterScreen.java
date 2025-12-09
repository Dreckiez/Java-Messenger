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

import utils.ApiClient;
import utils.ApiUrl;

public class RegisterScreen extends JPanel {
    private BaseScreen mainScreen;
    private JLabel registerError;
    private JPanel registerCard;
    private boolean passwordVisible = false;
    private boolean confirmPasswordVisible = false;

    public RegisterScreen(BaseScreen main) {
        mainScreen = main;

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 242, 245));

        registerCard = new JPanel();
        registerCard.setLayout(new GridBagLayout());
        registerCard.setBackground(Color.WHITE);
        registerCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(40, 50, 40, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        gbc.insets = new Insets(0, 0, 10, 0);
        registerCard.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Sign up to get started", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        gbc.insets = new Insets(0, 0, 30, 0);
        registerCard.add(subtitleLabel, gbc);

        registerError = new JLabel("");
        registerError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerError.setForeground(new Color(220, 53, 69));
        registerError.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 0, 15, 0);
        registerCard.add(registerError, gbc);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(73, 80, 87));
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        registerCard.add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        registerCard.add(userField, gbc);

        JLabel fnameLabel = new JLabel("First Name");
        fnameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fnameLabel.setForeground(new Color(73, 80, 87));
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        registerCard.add(fnameLabel, gbc);

        JTextField fnameField = new JTextField(20);
        fnameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fnameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        registerCard.add(fnameField, gbc);

        JLabel lnameLabel = new JLabel("Last Name");
        lnameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lnameLabel.setForeground(new Color(73, 80, 87));
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        registerCard.add(lnameLabel, gbc);

        JTextField lnameField = new JTextField(20);
        lnameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lnameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        registerCard.add(lnameField, gbc);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(73, 80, 87));
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        registerCard.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        registerCard.add(emailField, gbc);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setForeground(new Color(73, 80, 87));
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        registerCard.add(passLabel, gbc);

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
        registerCard.add(passContainer, gbc);

        JLabel confirmPassLabel = new JLabel("Confirm Password");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPassLabel.setForeground(new Color(73, 80, 87));
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        registerCard.add(confirmPassLabel, gbc);

        // Confirm Password field container
        JPanel confirmPassContainer = new JPanel();
        confirmPassContainer.setLayout(new GridBagLayout());
        confirmPassContainer.setBackground(Color.WHITE);

        GridBagConstraints confirmPassGbc = new GridBagConstraints();
        confirmPassGbc.gridx = 0;
        confirmPassGbc.gridy = 0;
        confirmPassGbc.weightx = 1.0;
        confirmPassGbc.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField confirmPassField = new JPasswordField(20);
        confirmPassField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPassField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        confirmPassContainer.add(confirmPassField, confirmPassGbc);

        confirmPassGbc.gridx = 1;
        confirmPassGbc.weightx = 0;
        confirmPassGbc.insets = new Insets(0, 5, 0, 0);

        JButton toggleConfirmPassBtn = new JButton("Show");
        toggleConfirmPassBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        toggleConfirmPassBtn.setForeground(new Color(13, 110, 253));
        toggleConfirmPassBtn.setBackground(Color.WHITE);
        toggleConfirmPassBtn.setFocusable(false);
        toggleConfirmPassBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 15, 10, 15)));
        toggleConfirmPassBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        toggleConfirmPassBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                toggleConfirmPassBtn.setBackground(new Color(240, 242, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                toggleConfirmPassBtn.setBackground(Color.WHITE);
            }
        });

        toggleConfirmPassBtn.addActionListener(e -> {
            confirmPasswordVisible = !confirmPasswordVisible;
            if (confirmPasswordVisible) {
                confirmPassField.setEchoChar((char) 0);
                toggleConfirmPassBtn.setText("Hide");
            } else {
                confirmPassField.setEchoChar('•');
                toggleConfirmPassBtn.setText("Show");
            }
        });

        confirmPassContainer.add(toggleConfirmPassBtn, confirmPassGbc);

        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        registerCard.add(confirmPassContainer, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(13, 110, 253));
        registerButton.setFocusable(false);
        registerButton.setBorder(new EmptyBorder(12, 0, 12, 0));
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);

        registerButton.addActionListener(e -> {
            String username = userField.getText(); // Check user exists
            String password = new String(passField.getPassword()); // Check pass
            String confirmPassword = new String(confirmPassField.getPassword()); // Check pass
            String email = emailField.getText(); // Check email
            String firstname = fnameField.getText();
            String lastname = lnameField.getText();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || firstname.isEmpty()
                    || lastname.isEmpty()) {
                registerError.setText("Please enter all the information");
                return;
            }

            if (!password.equals(confirmPassword)) {
                registerError.setText("Passwords not matching");
                return;
            }

            JSONObject payload = new JSONObject();
            payload.put("username", username);
            payload.put("firstname", firstname);
            payload.put("lastname", lastname);
            payload.put("email", email);
            payload.put("password", password);
            payload.put("confirmPassword", confirmPassword);

            registerButton.setText("Registering");
            registerButton.setEnabled(false);
            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() {
                    return ApiClient.postJSON(ApiUrl.REGISTER, payload, null);
                }

                @Override
                protected void done() {
                    try {
                        JSONObject res = get(); 

                        int status = res.optInt("httpStatus", 0);
                        if (status == 200) {
                            // Success → switch panel
                            mainScreen.showPanel("login");
                        } else {
                            // Failure → show error
                            String msg = res.optString("message", "Register failed");
                            registerError.setText(msg);
                        }
                    } catch (Exception e) {
                        registerError.setText("⚠️ Server unreachable or unknown error");
                        e.printStackTrace();
                    } finally {
                        // Re-enable button
                        registerButton.setText("Register");
                        registerButton.setEnabled(true);
                    }
                }
            }.execute();
        });

        gbc.insets = new Insets(0, 0, 20, 0);
        registerCard.add(registerButton, gbc);

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(new GridBagLayout());

        GridBagConstraints loginGbc = new GridBagConstraints();
        loginGbc.gridx = 0;
        loginGbc.gridy = 0;
        loginGbc.insets = new Insets(0, 0, 0, 5);

        JLabel loginPrefix = new JLabel("Already have an account?");
        loginPrefix.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginPrefix.setForeground(new Color(108, 117, 125));
        loginPanel.add(loginPrefix, loginGbc);

        loginGbc.gridx = 1;
        loginGbc.insets = new Insets(0, 0, 0, 0);

        JLabel loginLink = new JLabel("Login");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginLink.setForeground(new Color(13, 110, 253));
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainScreen.showPanel("login");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setForeground(new Color(11, 94, 215));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setForeground(new Color(13, 110, 253));
            }
        });

        loginPanel.add(loginLink, loginGbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        registerCard.add(loginPanel, gbc);

        GridBagConstraints mainGbc = new GridBagConstraints();
        add(registerCard, mainGbc);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
    }
}
