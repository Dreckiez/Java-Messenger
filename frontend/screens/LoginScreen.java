package screens;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.json.JSONObject;

import utils.ApiClient;
import utils.ApiUrl;

public class LoginScreen extends JPanel {
    private BaseScreen mainScreen;
    private JLabel loginError;

    public LoginScreen(BaseScreen main) {
        mainScreen = main;

        setLayout(null);

        setBackground(Color.lightGray);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(500, 250, 100, 30);
        add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(600, 250, 150, 30);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(500, 300, 100, 30);
        add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(600, 300, 150, 30);
        add(passField);

        loginError = new JLabel("");
        loginError.setBounds(510, 220, 200, 30);
        loginError.setForeground(Color.red);
        add(loginError);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(600, 350, 150, 30);
        loginButton.setFocusable(false);
        loginButton.addActionListener(e -> {
            JSONObject payload = new JSONObject();
            
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            payload.put("username", username);
            payload.put("password", password);

            loginButton.setEnabled(false);
            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() {
                    return ApiClient.postJSON(ApiUrl.LOGIN, payload);
                }

                @Override
                protected void done() {
                    try {
                        JSONObject res = get(); // get() returns result of doInBackground()

                        int status = res.optInt("httpStatus", 0);
                        if (status == 200) {
                            // Success → switch panel
                            mainScreen.showPanel("register");
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
                        loginButton.setEnabled(true);
                    }
                }
            }.execute();
        });
        
        add(loginButton);

        JLabel registerPrefix = new JLabel("Don't have an account?");
        registerPrefix.setBounds(510, 390, 137, 30);
        add(registerPrefix);

        JLabel registerLink = new JLabel("Register");
        registerLink.setForeground(Color.blue);
        registerLink.setBounds(650, 390, 50, 30);
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                mainScreen.showPanel("register");
            }
        });

        add(registerLink);
    }
}
