package screens;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.json.JSONObject;

import utils.ApiClient;
import utils.ApiUrl;

public class RegisterScreen extends JPanel {
    private BaseScreen mainScreen;
    private JTextArea registerError;

    public RegisterScreen(BaseScreen main) {
        mainScreen = main;

        setLayout(null);

        setBackground(Color.lightGray);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(500, 175, 100, 30);
        add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(600, 175, 150, 30);
        add(userField);

        JLabel fnameLabel = new JLabel("First Name:");
        fnameLabel.setBounds(500, 225, 100, 30);
        add(fnameLabel);

        JTextField fnameField = new JTextField();
        fnameField.setBounds(600, 225, 150, 30);
        add(fnameField);

        JLabel lnameLabel = new JLabel("Last Name:");
        lnameLabel.setBounds(500, 275, 100, 30);
        add(lnameLabel);

        JTextField lnameField = new JTextField();
        lnameField.setBounds(600, 275, 150, 30);
        add(lnameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(500, 325, 100, 30);
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(600, 325, 150, 30);
        add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(500, 375, 100, 30);
        add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(600, 375, 150, 30);
        add(passField);

        registerError = new JTextArea("");
        registerError.setEditable(false);
        registerError.setFocusable(false);
        registerError.setBounds(550, 140, 200, 50);
        registerError.setForeground(Color.red);
        registerError.setBackground(Color.lightGray);
        add(registerError);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(600, 425, 150, 30);
        registerButton.setFocusable(false);
        registerButton.addActionListener(e -> {
            String username = userField.getText(); // Check user exists
            String password = new String(passField.getPassword()); // Check pass
            String email = emailField.getText(); // Check email
            String firstname = fnameField.getText();
            String lastname = lnameField.getText();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || firstname.isEmpty()
                    || lastname.isEmpty()) {
                registerError.setText("Please enter all the information");
                return;
            }

            JSONObject payload = new JSONObject();
            payload.put("username", username);
            payload.put("firstname", firstname);
            payload.put("lastname", lastname);
            payload.put("email", email);
            payload.put("password", password);

            registerButton.setText("Registering");
            registerButton.setEnabled(false);
            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() {
                    return ApiClient.postJSON(ApiUrl.REGISTER, payload);
                }

                @Override
                protected void done() {
                    try {
                        JSONObject res = get(); // get() returns result of doInBackground()

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

        add(registerButton);

        JLabel loginPrefix = new JLabel("Already have an account?");
        loginPrefix.setBounds(510, 465, 145, 30);
        add(loginPrefix);

        JLabel loginLink = new JLabel("Login");
        loginLink.setForeground(Color.blue);
        loginLink.setBounds(660, 465, 35, 30);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainScreen.showPanel("login");
            }
        });

        add(loginLink);
    }
}
