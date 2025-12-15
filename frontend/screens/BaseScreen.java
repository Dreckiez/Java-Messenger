package screens;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.UserSession;

import java.awt.CardLayout;

public class BaseScreen extends JFrame {

    private DashboardScreen dashboardScreen;
    private CardLayout layout;
    private JPanel panel;

    public BaseScreen() {
        setTitle("W Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1280, 720);
        setResizable(true);

        layout = new CardLayout();
        panel = new JPanel(layout);

        add(panel);

        dashboardScreen = new DashboardScreen(this); 
    

        panel.add(dashboardScreen, "dashboard");

        ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource("assets/wolf-howling.jpg"));
        setIconImage(image.getImage());

        setVisible(true);
    }

    public void addPanel(JPanel p, String name) {
        panel.add(p, name);
    }

    public void logout() {
        UserSession.clearSession();
        showPanel("login");
    }

    public void showPanel(String name) {
        layout.show(panel, name);
    }

    public DashboardScreen getDashboardScreen() {
        return dashboardScreen;
    }
}
