package screens;

import javax.swing.*;
import java.awt.*;
import components.*;

public class HomeScreen extends JPanel {
    // private JPanel centerPanel;
    private JPanel infoPanel;
    private BaseScreen screen;

    public HomeScreen(BaseScreen screen) {
        this.screen = screen;
        setLayout(new BorderLayout());

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
    public void logout() {
        screen.logout();
    }

    // --- Toggle chat info visibility ---
    public void toggleInfoPanel(boolean visible) {
        infoPanel.setVisible(visible);
        revalidate();
        repaint();
    }
}
