package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class SettingPanel extends JPanel {
    private ProfileAvatar avatarSection;
    private ProfileInfo infoSection;
    private ProfilePass passwordSection;

    public SettingPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Main content with scroll
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);

        avatarSection = new ProfileAvatar();
        infoSection = new ProfileInfo();
        passwordSection = new ProfilePass();

        mainContent.add(avatarSection);
        mainContent.add(infoSection);
        mainContent.add(passwordSection);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(20, 30, 20, 30)));

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));

        header.add(titleLabel, BorderLayout.WEST);

        return header;
    }

    public void refreshData() {
        avatarSection.refreshData();
        infoSection.refreshData();
        passwordSection.refreshData();
    }
}