package components;

import javax.swing.*;
import java.awt.*;
import org.json.JSONArray;
import org.json.JSONObject;
import services.UserServices;

public class CenterPanel extends JPanel {
    private CardLayout layout;
    private ChatPanel chatPanel;
    private SettingPanel settingPanel;
    private JPanel welcomePanel;
    private InfoPanel infoPanel;
    
    private UserServices userServices;

    public CenterPanel() {
        userServices = new UserServices();

        layout = new CardLayout();
        setLayout(layout);

        chatPanel = new ChatPanel();
        settingPanel = new SettingPanel();
        welcomePanel = createWelcomePanel(); 

        add(chatPanel, "chat");
        add(settingPanel, "settings");
        add(welcomePanel, "welcome");

        layout.show(this, "welcome");
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 247, 250));
        
        JLabel icon = new JLabel("💬");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("Welcome to W Chat");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel sub = new JLabel("Select a conversation to start chatting");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setForeground(new Color(100, 116, 139));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalGlue());
        panel.add(icon);
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(sub);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

    public void setInfoPanel(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
    }

    public void setToggleInfoCallback(Runnable onToggle) {
        chatPanel.setOnToggleInfo(onToggle);
    }

    // 🔥 LOGIC SHOW CHAT
    public void showChat(JSONObject chatData) {
        String name = chatData.optString("groupName", chatData.optString("name", "Unknown"));
        String avatarUrl = chatData.optString("avatarUrl", null);
        
        String type = chatData.optString("conversationType", "PRIVATE");
        if (chatData.has("groupConversationId")) type = "GROUP";
        boolean isGroup = "GROUP".equalsIgnoreCase(type);

        // Update Header & Info Panel cơ bản
        chatPanel.updateChatHeader(name, avatarUrl, isGroup);
        if (infoPanel != null) {
            infoPanel.updateInfo(chatData);
        }

        layout.show(this, "chat");

        long conversationId = -1;
        if (isGroup) {
            conversationId = chatData.optLong("groupConversationId", chatData.optLong("id", -1));
        } else {
            conversationId = chatData.optLong("privateConversationId", chatData.optLong("id", -1));
        }
        
        if (conversationId != -1) {
            System.out.println("DEBUG CENTER: Selected ID=" + conversationId + ", Type=" + type);
            
            if (isGroup) {
                fetchGroupChatDetails(conversationId, chatData, name);
            } else {
                fetchPrivateChatDetails(conversationId, chatData, name);
            }
        }
    }

    // 🔥🔥 FETCH GROUP CHAT (ĐÃ SỬA LỖI JSON NULL) 🔥🔥
    private void fetchGroupChatDetails(long id, JSONObject initialData, String groupName) {
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                System.out.println("DEBUG CENTER: Calling Group API for ID " + id);
                return userServices.getGroupConversationDetails(id);
            }

            @Override
            protected void done() {
                try {
                    JSONObject response = get(); 
                    
                    if (response != null) {
                        // 1. Cập nhật tin nhắn
                        if (response.has("groupConversationMessageResponseList")) {
                            chatPanel.loadMessages(response.getJSONArray("groupConversationMessageResponseList"), groupName);
                        }

                        // 2. Cập nhật InfoPanel
                        if (infoPanel != null) {
                            JSONObject fullData = new JSONObject(initialData.toString());
                            
                            // Check các key member khác nhau
                            JSONArray members = null;
                            if (response.has("groupMemberResponseList")) {
                                members = response.getJSONArray("groupMemberResponseList");
                            } else if (response.has("members")) {
                                members = response.getJSONArray("members");
                            }

                            if (members != null) {
                                fullData.put("groupMemberResponseList", members);
                                System.out.println("DEBUG CENTER: Merged " + members.length() + " members.");
                            }

                            // 🔥 SỬA LỖI Ở ĐÂY: Dùng optString thay vì getString
                            String newAvatar = response.optString("avatarUrl", null);
                            if (newAvatar != null && !newAvatar.equals("null")) {
                                fullData.put("avatarUrl", newAvatar);
                            }

                            String newName = response.optString("groupName", null);
                            if (newName != null && !newName.equals("null")) {
                                fullData.put("groupName", newName);
                            }
                            
                            fullData.put("groupConversationId", id);
                            fullData.put("conversationType", "GROUP");

                            infoPanel.updateInfo(fullData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Lỗi xử lý dữ liệu nhóm: " + e.getMessage());
                }
            }
        }.execute();
    }

    // --- FETCH PRIVATE CHAT ---
    private void fetchPrivateChatDetails(long id, JSONObject chatData, String partnerName) {
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                return userServices.getPrivateConversationDetails((int) id);
            }

            @Override
            protected void done() {
                try {
                    JSONObject response = get();
                    if (response != null) {
                        if (response.has("privateConversationMessageResponseList")) {
                            JSONArray messages = response.getJSONArray("privateConversationMessageResponseList");
                            chatPanel.loadMessages(messages, partnerName);
                        }
                        if (infoPanel != null) {
                            JSONObject enrichedData = new JSONObject(chatData.toString());
                            if (response.has("userId")) {
                                enrichedData.put("partnerId", response.getInt("userId"));
                            }
                            infoPanel.updateInfo(enrichedData);
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    public void showSettings() {
        layout.show(this, "settings");
        settingPanel.refreshData();
    }
    
    public void showChat(String user) {
        chatPanel.setChatUser(user);
        layout.show(this, "chat");
    }

    public void showChat() { showWelcome(); }
    public void showWelcome() { layout.show(this, "welcome"); }
    public void resetInfoToggle() { if (chatPanel != null) chatPanel.setInfoActive(false); }
}