package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FriendPanel extends JPanel {
    private DefaultListModel<String> friendListModel;
    private JList<String> friendList;
    private JTextField searchField;
    private DefaultListModel<String> requestListModel;
    private JList<String> requestList;

    public FriendPanel() {
        setLayout(new BorderLayout());

        // Tabbed panel for Friends & Requests
        JTabbedPane tabs = new JTabbedPane();

        // === Friends tab ===
        JPanel friendsTab = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchField.setToolTipText("Search friends...");
        friendsTab.add(searchField, BorderLayout.NORTH);

        friendListModel = new DefaultListModel<>();
        friendList = new JList<>(friendListModel);
        friendsTab.add(new JScrollPane(friendList), BorderLayout.CENTER);

        JPanel friendButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton chatButton = new JButton("Chat");
        JButton unfriendButton = new JButton("Unfriend");
        JButton blockButton = new JButton("Block");
        friendButtons.add(chatButton);
        friendButtons.add(unfriendButton);
        friendButtons.add(blockButton);
        friendsTab.add(friendButtons, BorderLayout.SOUTH);

        // === Friend Requests tab ===
        JPanel requestsTab = new JPanel(new BorderLayout());
        requestListModel = new DefaultListModel<>();
        requestList = new JList<>(requestListModel);
        requestsTab.add(new JScrollPane(requestList), BorderLayout.CENTER);

        JPanel requestButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton acceptButton = new JButton("Accept");
        JButton declineButton = new JButton("Decline");
        requestButtons.add(acceptButton);
        requestButtons.add(declineButton);
        requestsTab.add(requestButtons, BorderLayout.SOUTH);

        // Add tabs
        tabs.addTab("Friends", friendsTab);
        tabs.addTab("Requests", requestsTab);

        add(tabs, BorderLayout.CENTER);

        // === Add some demo data ===
        loadDemoData();

        // === Event handlers ===
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterFriends(searchField.getText());
            }
        });

        chatButton.addActionListener(e -> {
            String selected = friendList.getSelectedValue();
            if (selected != null) {
                JOptionPane.showMessageDialog(this, "Opening chat with " + selected);
                // TODO: call callback to switch to ChatPanel
            }
        });

        unfriendButton.addActionListener(e -> {
            String selected = friendList.getSelectedValue();
            if (selected != null) {
                friendListModel.removeElement(selected);
                JOptionPane.showMessageDialog(this, "Unfriended " + selected);
            }
        });

        blockButton.addActionListener(e -> {
            String selected = friendList.getSelectedValue();
            if (selected != null) {
                friendListModel.removeElement(selected);
                JOptionPane.showMessageDialog(this, "Blocked " + selected);
            }
        });

        acceptButton.addActionListener(e -> {
            String selected = requestList.getSelectedValue();
            if (selected != null) {
                requestListModel.removeElement(selected);
                friendListModel.addElement(selected);
                JOptionPane.showMessageDialog(this, "Accepted friend: " + selected);
            }
        });

        declineButton.addActionListener(e -> {
            String selected = requestList.getSelectedValue();
            if (selected != null) {
                requestListModel.removeElement(selected);
                JOptionPane.showMessageDialog(this, "Declined friend: " + selected);
            }
        });
    }

    private void loadDemoData() {
        friendListModel.addAll(Arrays.asList("Alice (Online)", "Bob (Offline)", "Charlie (Online)"));
        requestListModel.addAll(Arrays.asList("David", "Eve"));
    }

    private void filterFriends(String text) {
        // In real app, you'd filter from server data
        friendListModel.clear();
        List<String> all = Arrays.asList("Alice (Online)", "Bob (Offline)", "Charlie (Online)");
        for (String f : all) {
            if (f.toLowerCase().contains(text.toLowerCase())) {
                friendListModel.addElement(f);
            }
        }
    }
}
