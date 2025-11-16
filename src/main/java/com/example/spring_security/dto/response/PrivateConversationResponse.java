package com.example.spring_security.dto.response;

import com.example.spring_security.entities.PrivateConversation;
import com.example.spring_security.entities.PrivateConversationMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrivateConversationResponse extends BaseUserResponse {
    private Long privateConversationId;
    private Boolean isOnline;
    private LocalDateTime createdAt;
    private PrivateConversationMessage privateConversationMessage;

    PrivateConversationResponse(Long userId, String fullName, String avatarUrl,
                Boolean isOnline, Long privateConversationId, java.sql.Timestamp createdAt,
                PrivateConversationMessage privateConversationMessage) {
        super(userId, fullName, avatarUrl);
        this.privateConversationId = privateConversationId;
        this.isOnline = isOnline;
        this.createdAt = createdAt.toLocalDateTime();
        this.privateConversationMessage = privateConversationMessage;
    }
}
