package screens;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import components.AdminNavBar;
import components.Analytics;
import components.GroupChat;
import components.LoginHistory;
import components.SpamReport;
import components.UserManage;

import java.awt.*;

public class DashboardScreen extends JPanel {
    private JPanel contentPanel;
    private BaseScreen screen;

    public DashboardScreen(BaseScreen screen) {
        this.screen = screen;
        setLayout(new BorderLayout());

        // === TOP: Header ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(37, 99, 235));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // === LEFT: Navigation Menu ===
        AdminNavBar navPanel = new AdminNavBar(this);
        add(navPanel, BorderLayout.WEST);

        // === CENTER: Content Area ===
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        // Load default view (User Management)
        showUserManagement();
    }

    public void showUserManagement() {
        contentPanel.removeAll();
        contentPanel.add(new UserManage(this), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showLoginHistory() {
        contentPanel.removeAll();
        contentPanel.add(new LoginHistory(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showGroupChat() {
        contentPanel.removeAll();
        contentPanel.add(new GroupChat(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showSpamReport() {
        contentPanel.removeAll();
        contentPanel.add(new SpamReport(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showAnalyticsDashboard() {
        contentPanel.removeAll();
        contentPanel.add(new Analytics(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void logout() {
        screen.logout();
    }
}
