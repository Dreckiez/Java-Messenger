package com.example.spring_security.dto.response;

import com.example.spring_security.entities.Enum.MessageType;
import com.example.spring_security.entities.PrivateConversationMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrivateConversationMessageResponse {
    private Long privateConversationMessageId;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime updatedAt;
    private MessageType type;
    private Boolean isRead;
    private LocalDateTime readAt;

    public PrivateConversationMessageResponse(
            Long privateConversationMessageId,
            String content,
            java.sql.Timestamp sentAt,
            java.sql.Timestamp updatedAt,
            Short type,
            Boolean isRead,
            java.sql.Timestamp readAt
    ) {
        this.privateConversationMessageId = privateConversationMessageId;
        this.content = content;
        this.sentAt = sentAt != null ? sentAt.toLocalDateTime() : null;
        this.updatedAt = updatedAt != null ? updatedAt.toLocalDateTime() : null;
        this.type = type == 0 ? MessageType.TEXT : MessageType.IMAGE;
        this.isRead = isRead;
        this.readAt = readAt != null ? readAt.toLocalDateTime() : null;
    }
}
