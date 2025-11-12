package components;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends JPanel {
    private CardLayout layout;
    private ChatPanel chatPanel;
    private SettingPanel settingPanel;

    public CenterPanel() {
        layout = new CardLayout();
        setLayout(layout);

        chatPanel = new ChatPanel();
        settingPanel = new SettingPanel();

        add(chatPanel, "chat");
        add(settingPanel, "settings");

        layout.show(this, "friends");
    }

    public void showChat(String user) {
        chatPanel.setChatUser(user);
        layout.show(this, "chat");
    }

    public void showChat() {
        chatPanel.setChatUser("Alice");
        layout.show(this, "chat");
    }

    public void showSettings() {
        layout.show(this, "settings");
    }
}
