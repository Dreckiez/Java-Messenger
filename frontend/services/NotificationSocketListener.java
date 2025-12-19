package services;

import javax.swing.SwingUtilities;

import components.FriendRequests;
import components.NavBar;
import components.NavPanel;
import models.NotificationWsResponse;

public class NotificationSocketListener {
    private static NavBar navBar;
    private static NavPanel navPanel;
    private static FriendRequests friendRequests;

    public static void init(NavBar nb, NavPanel np, FriendRequests fr) {
        navBar = nb;
        navPanel = np;
        friendRequests = fr;
    }

    public static void onMessage(NotificationWsResponse n) {
        SwingUtilities.invokeLater(() -> handle(n));
    }

    private static void handle(NotificationWsResponse n) {
        if (navBar == null || friendRequests == null)
            return;

        switch (n.getType()) {

            case NotificationType.FRIEND_REQUEST_RECEIVED:
                handleFriendRequestReceived(n);
                break;

            case NotificationType.FRIEND_REQUEST_CANCELLED:
                handleFriendRequestCancelled(n);
                break;
        }
    }

    private static void handleFriendRequestReceived(NotificationWsResponse n) {
        // If user is currently viewing request tab → refresh list
        if ("request".equals(navBar.getCurrentActivePanel())) {
            friendRequests.fetchRequests();
        } else {
            navBar.showRequestDot();
        }
    }

    private static void handleFriendRequestCancelled(NotificationWsResponse n) {

        // If user is viewing requests → remove instantly
        if ("request".equals(navBar.getCurrentActivePanel())) {
            friendRequests.removeRequestByUserId(n.getFromUserId());
        }

        navBar.hideRequestDot();
    }

}

class NotificationType {
    public static final String FRIEND_REQUEST_RECEIVED = "FRIEND_REQUEST_RECEIVED";
    public static final String FRIEND_REQUEST_ACCEPTED = "FRIEND_REQUEST_ACCEPTED";
    public static final String FRIEND_REQUEST_CANCELLED = "FRIEND_REQUEST_CANCELLED";
}
