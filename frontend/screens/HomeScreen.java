package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import components.*; 

public class HomeScreen extends JPanel {
    private InfoPanel infoPanel; 
    private BaseScreen screen;
    private NavPanel leftPanel; 

    private final Color BG_COLOR = new Color(241, 245, 249); 

    public HomeScreen(BaseScreen screen) {
        this.screen = screen;
        setLayout(new BorderLayout(15, 0)); 
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        CenterPanel centerPanel = new CenterPanel();
        infoPanel = new InfoPanel();
        infoPanel.setVisible(false); 
        leftPanel = new NavPanel(this, centerPanel); 
        
        // --- KẾT NỐI FRIEND PANEL -> NAV PANEL ---
        FriendPanel friendPanel = leftPanel.getFriendPanel();
        if (friendPanel != null) {
            // 1. Create Group: Chuyển tab

            friendPanel.setOnOpenChat((target) -> {
                System.out.println("HomeScreen received ID: " + target.id + ", Type: " + target.type);
                // Gọi NavPanel để chuyển tab và tải ChatList
                leftPanel.switchToChatAndOpen(target.id, target.type); 
                friendPanel.resetPanel();
            });

            friendPanel.setOnNavigateToChat(() -> {
                leftPanel.switchToChatTab();
                friendPanel.resetPanel(); 
            });

        }

        // --- KẾT NỐI INFO PANEL ---
        infoPanel.setOnChatActionCompleted(() -> {
            toggleInfoPanel(false);
            centerPanel.showWelcome();
            leftPanel.reloadChatList(); 
            leftPanel.reloadBlockedUsers();
        });

        centerPanel.setInfoPanel(infoPanel);
        centerPanel.setToggleInfoCallback(() -> {
            boolean currentStatus = infoPanel.isVisible();
            toggleInfoPanel(!currentStatus); 
        });

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
    }

    public void logout() { screen.logout(); }
    public void toggleInfoPanel(boolean visible) {
        if (infoPanel != null) {
            infoPanel.setVisible(visible);
            this.revalidate(); this.repaint();
        }
    }
}