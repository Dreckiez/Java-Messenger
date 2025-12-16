package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import components.*;
import services.NotificationSocketListener;
import utils.SocketManager;
import utils.UserSession;

public class HomeScreen extends JPanel {
    private InfoPanel infoPanel;
    private BaseScreen screen;
    private NavPanel leftPanel;
    private CenterPanel centerPanel; // 🔥 1. Đưa biến này ra ngoài thành biến toàn cục

    private final Color BG_COLOR = new Color(241, 245, 249);

    public HomeScreen(BaseScreen screen) {
        this.screen = screen;
        setLayout(new BorderLayout(15, 0));
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Khởi tạo các thành phần
        centerPanel = new CenterPanel(); // Đã khai báo ở trên
        infoPanel = new InfoPanel();
        infoPanel.setVisible(false);
        leftPanel = new NavPanel(this, centerPanel);

        NotificationSocketListener.init(
                leftPanel.getNavBar(),
                leftPanel,
                leftPanel.getFriendRequests());

        // --- KẾT NỐI FRIEND PANEL -> NAV PANEL ---
        FriendPanel friendPanel = leftPanel.getFriendPanel();
        if (friendPanel != null) {
            // 1. Create Group: Chuyển tab
            friendPanel.setOnOpenChat((target) -> {
                System.out.println("HomeScreen received ID: " + target.id + ", Type: " + target.type);
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

        // 🔥 2. GỌI HÀM RESET NGAY KHI KHỞI TẠO XONG
        resetToDefaultState();
    }

    // 🔥 3. HÀM RESET TRẠNG THÁI VỀ MẶC ĐỊNH (TAB CHAT)
    private void resetToDefaultState() {
        // A. Chuyển CenterPanel về màn hình Chat (hoặc Welcome)
        if (centerPanel != null) {
            centerPanel.showChat();
        }

        // B. Chuyển NavPanel (Left Panel) về Tab danh sách chat
        // Hàm này sẽ tự động highlight nút Chat trên NavBar nếu bạn đã code logic đó
        // trong NavPanel
        if (leftPanel != null) {
            leftPanel.switchToChatTab();
        }

        // C. Bật Info Panel mặc định (nếu muốn)
        toggleInfoPanel(true);
    }

    public void startConnection() {
        System.out.println("DEBUG: HomeScreen starting connection...");
        resetToDefaultState();
        leftPanel.checkInitialRequests();
        SocketManager.connect();
    }

    public void logout() {
        // 1. Xóa session
        UserSession.clearSession();

        // 2. 🔥 Dọn dẹp giao diện trước khi thoát
        if (centerPanel != null)
            centerPanel.reset();
        if (infoPanel != null)
            infoPanel.reset();
        // 3. Gọi hàm logout của màn hình cha
        screen.logout();

    }

    public void toggleInfoPanel(boolean visible) {
        if (infoPanel != null) {
            infoPanel.setVisible(visible);
            this.revalidate();
            this.repaint();
        }
    }
}