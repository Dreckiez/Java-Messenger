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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

import utils.ApiClient;
import utils.ApiUrl;

public class ForgotPass extends JPanel {
    private BaseScreen mainScreen;
    private JLabel messageLabel;
    private JPanel forgotPasswordCard;

    public ForgotPass(BaseScreen main) {
        mainScreen = main;

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 242, 245));

        forgotPasswordCard = new JPanel();
        forgotPasswordCard.setLayout(new GridBagLayout());
        forgotPasswordCard.setBackground(Color.WHITE);
        forgotPasswordCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(40, 50, 40, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Title
        JLabel titleLabel = new JLabel("Forgot Password", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        gbc.insets = new Insets(0, 0, 10, 0);
        forgotPasswordCard.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Enter your email", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        gbc.insets = new Insets(0, 0, 10, 0);
        forgotPasswordCard.add(subtitleLabel, gbc);

        // Description
        JLabel descriptionLabel = new JLabel(
                "<html><center>We'll send a new temporary password to your registered email address</center></html>");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionLabel.setForeground(new Color(108, 117, 125));
        gbc.insets = new Insets(0, 0, 30, 0);
        forgotPasswordCard.add(descriptionLabel, gbc);

        // Message label (for success/error messages)
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 0, 15, 0);
        forgotPasswordCard.add(messageLabel, gbc);

        // Username/Email label
        JLabel inputLabel = new JLabel("Email");
        inputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputLabel.setForeground(new Color(73, 80, 87));
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        forgotPasswordCard.add(inputLabel, gbc);

        // Username/Email field
        JTextField inputField = new JTextField(20);
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)));
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        forgotPasswordCard.add(inputField, gbc);

        // Button panel (for Reset and Back buttons)
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);

        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.gridx = 0;
        btnGbc.gridy = 0;
        btnGbc.insets = new Insets(0, 0, 0, 10);
        btnGbc.fill = GridBagConstraints.HORIZONTAL;
        btnGbc.weightx = 1.0;

        // Reset Password button
        JButton resetButton = new JButton("Reset Password");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(new Color(13, 110, 253));
        resetButton.setFocusable(false);
        resetButton.setBorder(new EmptyBorder(12, 20, 12, 20));
        resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetButton.setOpaque(true);
        resetButton.setBorderPainted(false);

        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (resetButton.isEnabled()) {
                    resetButton.setBackground(new Color(11, 94, 215));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (resetButton.isEnabled()) {
                    resetButton.setBackground(new Color(13, 110, 253));
                }
            }
        });

        resetButton.addActionListener(e -> {
            String input = inputField.getText().trim();

            if (input.isEmpty()) {
                showError("Please enter your email");
                return;
            }

            // Disable button and show loading state
            resetButton.setText("Sending...");
            resetButton.setEnabled(false);
            resetButton.setBackground(new Color(108, 117, 125));
            messageLabel.setText("");

            // Make API call in background
            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() {
                    JSONObject payload = new JSONObject();

                    payload.put("email", input);

                    return ApiClient.postJSON(ApiUrl.FORGOT_PASSWORD, payload, null);
                }

                @Override
                protected void done() {
                    try {
                        JSONObject response = get();
                        int status = response.optInt("httpStatus", 0);

                        if (status == 200) {
                            showSuccess("A new password has been sent to your email!");
                            inputField.setText("");

                            // Optionally redirect to login after a delay
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000); // Wait 3 seconds
                                    javax.swing.SwingUtilities.invokeLater(() -> {
                                        mainScreen.showPanel("login");
                                    });
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }).start();
                        } else {
                            String msg = response.optString("message", "Failed to reset password");
                            showError(msg);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showError("Server error. Please try again later.");
                    } finally {
                        // Re-enable button
                        resetButton.setText("Reset Password");
                        resetButton.setEnabled(true);
                        resetButton.setBackground(new Color(13, 110, 253));
                    }
                }
            }.execute();
        });

        buttonPanel.add(resetButton, btnGbc);

        btnGbc.gridx = 1;
        btnGbc.insets = new Insets(0, 0, 0, 0);

        // Back to Login button
        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(new Color(108, 117, 125));
        backButton.setBackground(Color.WHITE);
        backButton.setFocusable(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(11, 19, 11, 19)));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(240, 242, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                mainScreen.showPanel("login");
            }
        });

        buttonPanel.add(backButton, btnGbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        forgotPasswordCard.add(buttonPanel, gbc);

        // Add the card to the main panel (centered)
        GridBagConstraints mainGbc = new GridBagConstraints();
        add(forgotPasswordCard, mainGbc);

        // Add component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setForeground(new Color(220, 53, 69));
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setForeground(new Color(25, 135, 84));
    }
}