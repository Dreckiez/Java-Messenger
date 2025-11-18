package com.example.spring_security.services.user;

import com.example.spring_security.dto.request.PrivateConversationMessageRequest;
import com.example.spring_security.dto.response.ListPrivateConversationMessageResponse;
import com.example.spring_security.dto.response.PrivateConversationResponse;
import com.example.spring_security.entities.PrivateConversation;
import com.example.spring_security.entities.PrivateConversationMessage;

import java.util.List;
import java.util.Map;

public interface UserPrivateConversationService {
    Map<String, String> create(Long userId1, Long userId2);
    Map<String, String> removeConversation(Long removerId, Long privateConversationId);
    List<PrivateConversationResponse> getConversation(Long userId);
     ListPrivateConversationMessageResponse getMessage(Long userId, Long privateConversationId, Long cursorId);
    Map<String, String> send(Long senderId, Long privateConversationId,PrivateConversationMessageRequest privateConversationMessageRequest);
    Map<String, String> removeMessage(Long userId, Long privateConversationMessageId, boolean isAll);
}
