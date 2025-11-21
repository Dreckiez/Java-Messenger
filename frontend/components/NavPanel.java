package components;

import javax.swing.*;

import screens.HomeScreen;

import java.awt.*;

public class NavPanel extends JPanel {
    private ChatList chatList;
    private CardLayout centerLayout;
    private JPanel centerPanel;

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

        SearchFriend searchFriend = new SearchFriend(this);
        FriendRequests request = new FriendRequests();

        centerPanel.add(chatList, "chatlist");
        centerPanel.add(searchFriend, "searchfriend");
        centerPanel.add(request, "request");

        add(centerPanel, BorderLayout.CENTER);
    }

    public void showPanel(String name) {
        centerLayout.show(centerPanel, name);
    }

    public void showChat() {
        showPanel("chatlist");
        chatList.selectFirstChat();
    }
}
