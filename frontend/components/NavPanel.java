package components;

import javax.swing.*;
import screens.HomeScreen;
import java.awt.*;
import org.json.JSONObject;

public class NavPanel extends JPanel {
    private ChatList chatList;
    private CardLayout centerLayout;
    private JPanel centerPanel;
    private NavBar navBar;

    private HomeScreen homeScreenRef;
    private CenterPanel centerRef;

    private SearchFriend search;
    private FriendRequests request;
    private FriendPanel friend;
    private BlockedUserPanel blockedUsers;
    private final Color BG_COLOR = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);

    public NavPanel(HomeScreen homeScreenArg, CenterPanel center) {
        this.homeScreenRef = homeScreenArg;
        this.centerRef = center;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(360, 0));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        blockedUsers = new BlockedUserPanel();
        navBar = new NavBar(homeScreenRef, center, this);
        add(navBar, BorderLayout.WEST);

        centerLayout = new CardLayout();
        centerPanel = new JPanel(centerLayout);
        centerPanel.setBackground(BG_COLOR);

        chatList = new ChatList((JSONObject chatData) -> center.showChat(chatData));
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

    public FriendPanel getFriendPanel() {
        return this.friend;
    }

    public NavBar getNavBar() {
        return navBar;
    }

    public FriendRequests getFriendRequests() {
        return request;
    }

    public void switchToChatTab() {
        showPanel("chatlist");
        // reloadChatList();
    }

    public void showPanel(String name) {
        if (navBar != null)
            navBar.setActiveButton(name);

        // Logic ẩn/hiện InfoPanel
        SwingUtilities.invokeLater(() -> {
            if (homeScreenRef != null)
                homeScreenRef.toggleInfoPanel(false);
            if (centerRef != null && !"chatlist".equals(name))
                centerRef.resetInfoToggle();
        });

        // Trigger reload khi chuyển tab
        switch (name) {
            case "chatlist":
                if (chatList != null)
                    chatList.loadConversations();
                break;
            case "request": {
                navBar.hideRequestDot();
                if (request != null)
                    request.fetchRequests();
                break;
            }
            case "searchfriend":
                if (search != null)
                    search.resetSearch();
                break;
            case "onlinefriend":
                if (friend != null)
                    friend.fetchRequests();
                break;
            case "blockedusers":
                if (blockedUsers != null)
                    blockedUsers.fetchBlockedUsers();
                break;
        }
        centerLayout.show(centerPanel, name);
    }

    public void reloadChatList() {
        if (chatList != null)
            chatList.loadConversations();
    }

    public void switchToChatAndOpen(int conversationId, String conversationType) {
        // 1. Chuyển sang tab ChatList
        showPanel("chatlist");

        // 2. Gọi hàm tải/chọn chat trong ChatList
        if (chatList != null) {
            // Hàm này sẽ thiết lập pendingOpenId và pendingOpenType trong ChatList
            chatList.loadConversations(conversationId, conversationType);
        }
    }

    public void checkInitialRequests() {
        if (request != null) {
            request.checkPendingCount(count -> {
                if (count > 0) {
                    navBar.showRequestDot();
                    // If you implement a counter: navBar.setCount(count);
                } else {
                    navBar.hideRequestDot();
                }
            });
        }
    }

    public void reloadBlockedUsers() {
        if (blockedUsers != null)
            blockedUsers.fetchBlockedUsers();
    }
}