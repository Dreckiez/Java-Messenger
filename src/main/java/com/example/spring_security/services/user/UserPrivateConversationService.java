package com.example.spring_security.services.user;

import com.example.spring_security.dto.request.SendMessageRequest;
import com.example.spring_security.dto.response.ListPrivateConversationMessageResponse;
import com.example.spring_security.dto.response.SendMessageResponse;

import java.util.Map;

public interface UserPrivateConversationService {
    Map<String, String> create(Long userId1, Long userId2);
    Map<String, String> removeConversation(Long removerId, Long privateConversationId);
    ListPrivateConversationMessageResponse getMessages(Long userId, Long privateConversationId, Long cursorId);
    SendMessageResponse sendMessage(Long senderId, Long privateConversationId, SendMessageRequest sendMessageRequest);
    Map<String, String> removeMessage(Long userId, Long privateConversationId, Long privateConversationMessageId, boolean isAll);
}
