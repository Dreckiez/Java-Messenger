package screens;

import javax.swing.*;
import java.awt.*;
import components.*;
import models.User;
import utils.UserSession;

public class HomeScreen extends JPanel {
    // private JPanel centerPanel;
    private JPanel infoPanel;

    public HomeScreen(BaseScreen screen) {
        setLayout(new BorderLayout());
        User user = new User();
        UserSession.setUser(user);

        // === CENTER: Main Area ===
        CenterPanel centerPanel = new CenterPanel();

        add(centerPanel, BorderLayout.CENTER);

        // === LEFT: Navigation ===
        NavPanel leftPanel = new NavPanel(this, centerPanel);
        add(leftPanel, BorderLayout.WEST);

        // === RIGHT: Chat Info ===
        infoPanel = new InfoPanel();
        add(infoPanel, BorderLayout.EAST);
    }

    // --- Called from LeftPanel navigation ---
    // public void showPanel(String name) {
    // centerLayout.show(centerPanel, name);
    // }

    // --- Toggle chat info visibility ---
    public void toggleInfoPanel(boolean visible) {
        infoPanel.setVisible(visible);
        revalidate();
        repaint();
    }
}
