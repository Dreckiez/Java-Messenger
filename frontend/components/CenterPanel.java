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
    private long currentChatId = -1;
    private String currentChatType = null;
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
        if (chatData.has("groupConversationId"))
            type = "GROUP";
        boolean isGroup = "GROUP".equalsIgnoreCase(type);

        long conversationId = -1;
        if (isGroup) {
            conversationId = chatData.optLong("groupConversationId", chatData.optLong("id", -1));
        } else {
            conversationId = chatData.optLong("privateConversationId", chatData.optLong("id", -1));
        }

        // Update Header & Info Panel cơ bản
        chatPanel.updateChatHeader(conversationId, name, avatarUrl, isGroup);
        if (infoPanel != null) {
            infoPanel.updateInfo(chatData);
        }

        layout.show(this, "chat");

        chatPanel.setCurrentChatType(type);
        this.currentChatId = conversationId;
        this.currentChatType = type;
        if (conversationId != -1) {
            System.out.println("DEBUG CENTER: Selected ID=" + conversationId + ", Type=" + type);

            if (isGroup) {
                fetchGroupChatDetails(conversationId, chatData, name);
            } else {
                // chatPanel.fetchMessages(conversationId, name);
                fetchPrivateChatDetails(conversationId, chatData, name);
            }
        }
    }

    // 🔥🔥 FETCH GROUP CHAT (ĐÃ SỬA LỖI JSON NULL) 🔥🔥
    private void fetchGroupChatDetails(long id, JSONObject initialData, String groupName) {
        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                if (id != CenterPanel.this.currentChatId
                        || !"GROUP".equalsIgnoreCase(CenterPanel.this.currentChatType)) {
                    System.out.println("DEBUG CENTER: ABORTING Group API call for old ID " + id);
                    return null;
                }
                return userServices.getGroupConversationDetails(id);
            }

            @Override
            protected void done() {
                try {
                    JSONObject response = get();

                    if (response == null || id != CenterPanel.this.currentChatId
                            || !"GROUP".equalsIgnoreCase(CenterPanel.this.currentChatType)) {
                        System.out.println("DEBUG CENTER: ABORT LẦN 2 (Group) - ID " + id + " bị ghi đè.");
                        return; // Thoát khỏi hàm done()
                    }

                    if (response != null) {
                        // 1. Cập nhật tin nhắn
                        if (response.has("groupConversationMessageResponseList")) {
                            chatPanel.loadMessages(response.getJSONArray("groupConversationMessageResponseList"),
                                    groupName);
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
                if (id != currentChatId || !"PRIVATE".equalsIgnoreCase(currentChatType)) { // Dùng biến thành viên
                    System.out.println("DEBUG CENTER: ABORTING Private API call for old ID " + id);
                    return null;
                }
                return userServices.getPrivateConversationDetails((int) id);
            }

            @Override
            protected void done() {
                try {
                    JSONObject response = get();

                    if (response == null || id != CenterPanel.this.currentChatId
                            || !"PRIVATE".equalsIgnoreCase(CenterPanel.this.currentChatType)) {
                        System.out.println("DEBUG CENTER: ABORT LẦN 2 (Private) - ID " + id + " bị ghi đè.");
                        return; // Thoát khỏi hàm done()
                    }

                    if (response != null) {

                        // --- 1. TRÍCH XUẤT VÀ XỬ LÝ DỮ LIỆU ĐỐI TÁC MỚI NHẤT ---

                        String newPartnerName = partnerName; // Mặc định là tên cũ từ ChatList
                        String newPartnerAvatar = response.optString("avatarUrl",
                                chatData.optString("avatarUrl", null));
                        Long convId = response.optLong("privateConversationId");
                        // Xử lý tên: Ưu tiên (firstName + lastName) hoặc username
                        String partnerFirstName = response.optString("firstName", "");
                        String partnerLastName = response.optString("lastName", "");
                        String partnerUsername = response.optString("username", "");

                        if (!partnerFirstName.isEmpty() || !partnerLastName.isEmpty()) {
                            newPartnerName = (partnerFirstName + " " + partnerLastName).trim();
                        } else if (!partnerUsername.isEmpty()) {
                            newPartnerName = partnerUsername;
                        }

                        // Xử lý avatar: Chuyển "null" hoặc rỗng thành null
                        if (newPartnerAvatar != null
                                && (newPartnerAvatar.equals("null") || newPartnerAvatar.isEmpty())) {
                            newPartnerAvatar = null;
                        }

                        // --- 2. CẬP NHẬT CHAT HEADER BẰNG DỮ LIỆU MỚI ---
                        // Cần có hàm updateChatHeader(name, avatar, isPrivate) trong ChatPanel
                        chatPanel.updateChatHeader(convId, newPartnerName, newPartnerAvatar, true);

                        // --- 3. TẢI TIN NHẮN ---
                        if (response.has("privateConversationMessageResponseList")) {
                            JSONArray messages = response.getJSONArray("privateConversationMessageResponseList");
                            // Quan trọng: Truyền tên đối tác mới nhất vào loadMessages
                            chatPanel.loadMessages(messages, newPartnerName);
                        }

                        // --- 4. CẬP NHẬT INFOPANEL ---
                        if (infoPanel != null) {
                            // Khởi tạo enrichedData từ chatData tóm tắt
                            JSONObject enrichedData = new JSONObject(chatData.toString());

                            // 🔥 Ghi đè tên/avatar (Đã làm ở bước trước)
                            enrichedData.put("name", newPartnerName);
                            if (newPartnerAvatar != null)
                                enrichedData.put("avatarUrl", newPartnerAvatar);

                            // 🔥 BỔ SUNG THÔNG TIN CHI TIẾT CỦA ĐỐI TÁC TỪ PHẢN HỒI API (RESPONSE)
                            // Dữ liệu API trả về chi tiết của người dùng đối tác
                            if (response.has("userId")) {
                                enrichedData.put("partnerId", response.optInt("userId", -1));
                                enrichedData.put("partnerUsername", response.optString("username", ""));
                                enrichedData.put("partnerFirstName", response.optString("firstName", ""));
                                enrichedData.put("partnerLastName", response.optString("lastName", ""));

                                // Thêm các trường khác cần thiết cho InfoPanel (giả định chúng có trong
                                // response API)
                                enrichedData.put("email", response.optString("email", "N/A"));
                                enrichedData.put("address", response.optString("address", "N/A"));
                                enrichedData.put("gender", response.optString("gender", "N/A"));
                                enrichedData.put("birthDay", response.optString("birthDay", "N/A"));

                                // Thêm trạng thái online
                                enrichedData.put("isOnline", response.optBoolean("isOnline", false));
                            }

                            // Cần phải có các trường "partner..." để InfoPanel biết đây là thông tin đối
                            // tác
                            // và không nhầm với thông tin chat Group.

                            infoPanel.updateInfo(enrichedData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Lỗi xử lý dữ liệu Private Chat chi tiết: " + e.getMessage());
                }
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

    public long getCurrentChatId() {
        return this.currentChatId;
    }

    public String getCurrentChatType() {
        return this.currentChatType;
    }

    public ChatPanel getChatPanel() {
        return this.chatPanel;
    }

    public void showChat() {
        showWelcome();
    }

    public void showWelcome() {
        createWelcomePanel();
        layout.show(this, "welcome");
    }

    public void resetInfoToggle() {
        if (chatPanel != null)
            chatPanel.setInfoActive(false);
    }

    public void reset() {
        // 1. Dọn dẹp ChatPanel
        if (chatPanel != null) {
            chatPanel.clearChat();
        }

        // 2. Chuyển về màn hình Welcome (Màn hình chờ)
        // Vì tài khoản mới chưa có chat, không nên showChat() ngay
        showWelcome();
    }
}