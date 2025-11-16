package com.example.spring_security.services.user;

import com.example.spring_security.dto.response.PrivateConversationResponse;
import com.example.spring_security.entities.PrivateConversation;

import java.util.List;
import java.util.Map;

public interface UserPrivateConversationService {
    Map<String, String> create(Long userId1, Long userId2);
    Map<String, String> remove(Long removerId, Long privateConversationId);
    List<PrivateConversationResponse> get(Long userId);
    // Map<String, String>
}
