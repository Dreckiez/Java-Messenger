package services;

public interface ChatListener {
  
    void onGroupNameChanged(long conversationId, String newName);
    void onConversationDeleted(long conversationId);
    void onGroupAvatarChanged(long conversationId, String newAvatarUrl);
}