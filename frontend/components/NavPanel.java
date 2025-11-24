package components;

import javax.swing.*;

import screens.HomeScreen;

import java.awt.*;

public class NavPanel extends JPanel {
    private ChatList chatList;
    private CardLayout centerLayout;
    private JPanel centerPanel;

    private SearchFriend search;
    private FriendRequests request;
    private FriendPanel friend;

    public NavPanel(HomeScreen home, CenterPanel center) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 0));
        setBackground(new Color(245, 245, 245));

        // === LEFT: Navigation ===
        NavBar navBar = new NavBar(home, center, this);
        add(navBar, BorderLayout.WEST);

        // === CENTER: Main Area ===
        centerLayout = new CardLayout();
        centerPanel = new JPanel(centerLayout);

        chatList = new ChatList(selectedUser -> {
            center.showChat(selectedUser);
        });

        search = new SearchFriend(this);
        request = new FriendRequests();
        friend = new FriendPanel();

        centerPanel.add(chatList, "chatlist");
        centerPanel.add(search, "searchfriend");
        centerPanel.add(request, "request");
        centerPanel.add(friend, "onlinefriend");

        add(centerPanel, BorderLayout.CENTER);
    }

    public void showPanel(String name) {
        switch (name) {
            case "request":
                request.fetchRequests(); // Fetch when showing requests
                break;
            case "searchfriend":
                search.resetSearch(); // Reset search when showing
                break;
            case "onlinefriend":
                friend.fetchRequests();
                break;
            // Add other cases as needed
        }
        centerLayout.show(centerPanel, name);
    }
}
