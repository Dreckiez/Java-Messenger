package screens;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.*;

import components.ActiveUser;
import components.AdminNavBar;
import components.Analytics;
import components.GroupChat;
import components.LoginHistory;
import components.SpamReport;
import components.UserManage;

public class DashboardScreen extends JPanel {
    private JPanel contentPanel;
    private BaseScreen screen;
    private AdminNavBar navPanel;

    public DashboardScreen(BaseScreen screen) {
        this.screen = screen;
        setLayout(new BorderLayout());

        // === 1. TOP HEADER ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(37, 99, 235));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // === 2. LEFT NAVIGATION ===
        navPanel = new AdminNavBar(this);
        add(navPanel, BorderLayout.WEST);

        // === 3. CENTER CONTENT ===
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(241, 245, 249)); 
        add(contentPanel, BorderLayout.CENTER);

        // Mặc định hiển thị trang Home (Welcome)
        showDashboard("home");
    }

    /**
     * Hàm này chạy sau khi Login thành công.
     * Reset về trang Home để chào mừng.
     */
    public void onLoginSuccess() {
        showDashboard("home");
    }

    public void showDashboard(String name) {
        contentPanel.removeAll();

        switch (name) {
            case "home":
                // --- MÀN HÌNH WELCOME ĐƠN GIẢN ---
                JPanel homePanel = new JPanel(new GridBagLayout());
                homePanel.setBackground(Color.WHITE);
                
                JLabel welcomeLabel = new JLabel("Welcome Back, Admin!", SwingConstants.CENTER);
                welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
                welcomeLabel.setForeground(new Color(200, 200, 200)); // Màu xám nhạt sang trọng
                
                homePanel.add(welcomeLabel);
                contentPanel.add(homePanel, BorderLayout.CENTER);
                break;

            case "user":
                // Chỉ khi bấm vào đây mới tạo và load dữ liệu
                UserManage userPage = new UserManage(this);
                contentPanel.add(userPage, BorderLayout.CENTER);
                userPage.loadData(); 
                break;

            case "active":
                contentPanel.add(new ActiveUser(), BorderLayout.CENTER);
                break;
                
            case "history":
                contentPanel.add(new LoginHistory(), BorderLayout.CENTER);
                break;
                
            case "group":
                contentPanel.add(new GroupChat(), BorderLayout.CENTER);
                break;
                
            case "report":
                contentPanel.add(new SpamReport(), BorderLayout.CENTER);
                break;
                
            case "analytics":
                contentPanel.add(new Analytics(), BorderLayout.CENTER);
                break;

            default:
                break;
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void logout() {
        screen.logout();
    }
}