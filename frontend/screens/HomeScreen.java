package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import components.*; 

public class HomeScreen extends JPanel {
    private InfoPanel infoPanel; 
    private BaseScreen screen;
    private NavPanel leftPanel; 

    // --- MÀU SẮC CHỦ ĐẠO ---
    private final Color BG_COLOR = new Color(241, 245, 249); 

    public HomeScreen(BaseScreen screen) {
        this.screen = screen;
        
        // 1. Layout setup
        setLayout(new BorderLayout(15, 0)); 
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // === CENTER: Main Chat Area ===
        CenterPanel centerPanel = new CenterPanel();

        // === RIGHT: Chat Info ===
        infoPanel = new InfoPanel();
        infoPanel.setVisible(false); 
        
        // === LEFT: Navigation ===
        leftPanel = new NavPanel(this, centerPanel); 
        
        // 🔥🔥🔥 KẾT NỐI RELOAD SAU ACTION (BLOCK/DELETE) 🔥🔥🔥
        infoPanel.setOnChatActionCompleted(() -> {
            // 1. Ẩn Info Panel
            toggleInfoPanel(false);
            
            // 2. Chuyển Center Panel về màn hình Welcome/Trống
            centerPanel.showWelcome();
            
            // 3. YÊU CẦU NavPanel tải lại danh sách
            leftPanel.reloadChatList(); 
        });


        // 2. KẾT NỐI (WIRING)
        centerPanel.setInfoPanel(infoPanel);

        centerPanel.setToggleInfoCallback(() -> {
            boolean currentStatus = infoPanel.isVisible();
            toggleInfoPanel(!currentStatus); 
        });

        // === ADD TO LAYOUT ===
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        
        // 🔥 GỌI LOAD CHATLIST LẦN ĐẦU TIÊN
    }

    public void logout() {
        screen.logout();
    }

    // --- Toggle chat info visibility ---
    public void toggleInfoPanel(boolean visible) {
        if (infoPanel != null) {
            infoPanel.setVisible(visible);
            
            // Quan trọng: Gọi revalidate trên chính HomeScreen để bố cục lại
            this.revalidate();
            this.repaint();
        }
    }
}