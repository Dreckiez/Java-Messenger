package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import screens.DashboardScreen;
import utils.ImageEditor;
import utils.StyleButton;

public class AdminNavBar extends JPanel {
    private DashboardScreen dashboard;

    public AdminNavBar(DashboardScreen dashboard) {
        this.dashboard = dashboard;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 41, 59));
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        ImageEditor editor = new ImageEditor();
        StyleButton st = new StyleButton();

        ImageIcon userIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/friend_white.png"));

        JButton userBtn = new JButton(new ImageIcon(editor.scaleImage(userIcon.getImage(), 24)));

        userBtn.setText("User Management");
        st.styleButton(userBtn);

        userBtn.setIconTextGap(15);
        userBtn.setMaximumSize(new Dimension(200, 50));
        userBtn.setPreferredSize(new Dimension(150, 50));
        userBtn.setForeground(Color.WHITE);

        userBtn.addActionListener(e -> {
            dashboard.showUserManagement();
        });

        ImageIcon historyIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/history_white.png"));

        JButton loginHistoryButton = new JButton(new ImageIcon(editor.scaleImage(historyIcon.getImage(), 24)));

        loginHistoryButton.setText("Login History");

        st.styleButton(loginHistoryButton);

        loginHistoryButton.setIconTextGap(15);
        loginHistoryButton.setMaximumSize(new Dimension(200, 50));
        loginHistoryButton.setPreferredSize(new Dimension(150, 50));
        loginHistoryButton.setForeground(Color.WHITE);

        loginHistoryButton.addActionListener(e -> {
            dashboard.showLoginHistory();
        });

        ImageIcon groupIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/group_white.png"));

        JButton groupBtn = new JButton(new ImageIcon(editor.scaleImage(groupIcon.getImage(), 24)));

        groupBtn.setText("Group Management");

        st.styleButton(groupBtn);

        groupBtn.setIconTextGap(15);
        groupBtn.setMaximumSize(new Dimension(200, 50));
        groupBtn.setPreferredSize(new Dimension(150, 50));
        groupBtn.setForeground(Color.WHITE);

        groupBtn.addActionListener(e -> {
            dashboard.showGroupChat();
        });

        ImageIcon reportIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/report_white.png"));

        JButton reportBtn = new JButton(new ImageIcon(editor.scaleImage(reportIcon.getImage(), 24)));

        reportBtn.setText("Spam Reports");

        st.styleButton(reportBtn);

        reportBtn.setIconTextGap(15);
        reportBtn.setMaximumSize(new Dimension(200, 50));
        reportBtn.setPreferredSize(new Dimension(150, 50));
        reportBtn.setForeground(Color.WHITE);

        reportBtn.addActionListener(e -> {
            dashboard.showSpamReport();
        });

        ImageIcon analyticsIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/analytics_white.png"));

        JButton analyticsBtn = new JButton(new ImageIcon(editor.scaleImage(analyticsIcon.getImage(), 24)));

        analyticsBtn.setText("Analytics");

        st.styleButton(analyticsBtn);

        analyticsBtn.setIconTextGap(15);
        analyticsBtn.setMaximumSize(new Dimension(200, 50));
        analyticsBtn.setPreferredSize(new Dimension(150, 50));
        analyticsBtn.setForeground(Color.WHITE);

        analyticsBtn.addActionListener(e -> {
            dashboard.showAnalyticsDashboard();
        });

        userBtn.setHorizontalTextPosition(SwingConstants.RIGHT); // Places text to the right of the icon
        userBtn.setHorizontalAlignment(SwingConstants.LEFT);

        loginHistoryButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Places text to the right of the icon
        loginHistoryButton.setHorizontalAlignment(SwingConstants.LEFT);

        groupBtn.setHorizontalTextPosition(SwingConstants.RIGHT); // Places text to the right of the icon
        groupBtn.setHorizontalAlignment(SwingConstants.LEFT);

        reportBtn.setHorizontalTextPosition(SwingConstants.RIGHT); // Places text to the right of the icon
        reportBtn.setHorizontalAlignment(SwingConstants.LEFT);

        analyticsBtn.setHorizontalTextPosition(SwingConstants.RIGHT); // Places text to the right of the icon
        analyticsBtn.setHorizontalAlignment(SwingConstants.LEFT);

        add(userBtn);
        add(loginHistoryButton);
        add(groupBtn);
        add(reportBtn);
        add(analyticsBtn);

        // Logout button at bottom
        add(Box.createVerticalGlue());

        ImageIcon logoutIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/logout_white.png"));

        JButton logoutBtn = new JButton(new ImageIcon(editor.scaleImage(logoutIcon.getImage(), 24)));

        logoutBtn.setText("Logout");
        st.styleButton(logoutBtn);

        logoutBtn.setIconTextGap(10);
        logoutBtn.setMaximumSize(new Dimension(200, 50));
        logoutBtn.setPreferredSize(new Dimension(150, 50));
        logoutBtn.setForeground(Color.WHITE);

        logoutBtn.addActionListener(e -> {
            dashboard.logout(); // Call logout on HomeScreen
        });

        add(logoutBtn);

        add(Box.createVerticalStrut(20));
    }
}
