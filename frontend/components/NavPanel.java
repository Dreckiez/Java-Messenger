package components;

import javax.swing.*;
import screens.HomeScreen;
import java.awt.*;
import org.json.JSONObject;

public class NavPanel extends JPanel {
    private ChatList chatList;
    private CardLayout centerLayout;
    private JPanel centerPanel;
    
    // Biến toàn cục để lưu tham chiếu
    private HomeScreen homeScreenRef;
    private CenterPanel centerRef; 

    private SearchFriend search;
    private FriendRequests request;
    private FriendPanel friend;
    private BlockedUserPanel blockedUsers; // Đã khởi tạo trong constructor
    
    private final Color BG_COLOR = new Color(248, 250, 252); 
    private final Color BORDER_COLOR = new Color(226, 232, 240);

    public NavPanel(HomeScreen homeScreenArg, CenterPanel center) {
        this.homeScreenRef = homeScreenArg;
        this.centerRef = center; 

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(360, 0)); 
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));
        
        blockedUsers = new BlockedUserPanel(); // Khởi tạo trước khi thêm vào CenterPanel
        
        NavBar navBar = new NavBar(homeScreenRef, center, this);
        add(navBar, BorderLayout.WEST);

        centerLayout = new CardLayout();
        centerPanel = new JPanel(centerLayout);
        centerPanel.setBackground(BG_COLOR);

        chatList = new ChatList((JSONObject chatData) -> {
            center.showChat(chatData); 
        });

        search = new SearchFriend(this);
        request = new FriendRequests();
        friend = new FriendPanel();

        centerPanel.add(chatList, "chatlist");
        centerPanel.add(search, "searchfriend");
        centerPanel.add(request, "request");
        centerPanel.add(friend, "onlinefriend");
        centerPanel.add(blockedUsers, "blockedusers");
        add(centerPanel, BorderLayout.CENTER);
    }

    public void showPanel(String name) {
        System.out.println("DEBUG: Switching to panel -> " + name);

        switch (name) {
            case "chatlist":
                SwingUtilities.invokeLater(() -> {
                    // Ẩn InfoPanel
                    if (homeScreenRef != null) {
                        homeScreenRef.toggleInfoPanel(false);
                    }
                    // Reset nút (i) về trạng thái chưa chọn (màu trắng)
                    if (centerRef != null) {
                        centerRef.resetInfoToggle();
                    }
                });

                if (chatList != null) chatList.loadConversations();
                break;
                
            case "request":
                // Ẩn InfoPanel khi chuyển tab
                SwingUtilities.invokeLater(() -> {
                    if (homeScreenRef != null) homeScreenRef.toggleInfoPanel(false);
                });
                if (request != null) request.fetchRequests(); 
                break;
            case "searchfriend":
                // Ẩn InfoPanel khi chuyển tab
                SwingUtilities.invokeLater(() -> {
                    if (homeScreenRef != null) homeScreenRef.toggleInfoPanel(false);
                });
                if (search != null) search.resetSearch(); 
                break;
            case "onlinefriend":
                // Ẩn InfoPanel khi chuyển tab
                SwingUtilities.invokeLater(() -> {
                    if (homeScreenRef != null) homeScreenRef.toggleInfoPanel(false);
                });
                if (friend != null) friend.fetchRequests();
                break;
            case "blockedusers":
                SwingUtilities.invokeLater(() -> {
                    if (homeScreenRef != null) homeScreenRef.toggleInfoPanel(false); // Ẩn info panel
                    if (centerRef != null) centerRef.showWelcome(); // Hiển thị màn hình chờ ở trung tâm
                });
                // 🔥 Gọi fetch khi tab được chọn
                if (blockedUsers != null) blockedUsers.fetchBlockedUsers();
                break;
        }
        centerLayout.show(centerPanel, name);
    }
    
    // --- CÁC HÀM RELOAD ---
    
    public void reloadChatList() {
        if (chatList != null) {
            chatList.loadConversations();
        }
    }
    
    // 🔥 Hàm tải lại danh sách người dùng bị chặn
    public void reloadBlockedUsers() {
        if (blockedUsers != null) {
            blockedUsers.fetchBlockedUsers();
        }
    }
}