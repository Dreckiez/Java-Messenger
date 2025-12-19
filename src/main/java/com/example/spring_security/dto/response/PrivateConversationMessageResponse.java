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
    private Long senderId;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime updatedAt;
    private MessageType type;

    public PrivateConversationMessageResponse(
            Long privateConversationMessageId,
            Long senderId,
            String content,
            java.sql.Timestamp sentAt,
            java.sql.Timestamp updatedAt,
            Short type) {
        this.senderId = senderId;
        this.privateConversationMessageId = privateConversationMessageId;
        this.content = content;
        this.sentAt = sentAt != null ? sentAt.toLocalDateTime() : null;
        this.updatedAt = updatedAt != null ? updatedAt.toLocalDateTime() : null;
        MessageType.Converter converter = new MessageType.Converter();
        this.type = converter.fromShort(type);
    }
}
