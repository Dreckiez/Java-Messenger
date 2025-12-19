package com.example.spring_security.dto.response;

import com.example.spring_security.entities.Enum.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class GroupConversationMessageResponse {

    private Long senderId;

    private String senderName;
    private String senderAvatar;

    private Long groupConversationId;
    private Long groupConversationMessageId;

    private String content;

    private LocalDateTime sentAt;

    private LocalDateTime updatedAt;

    private MessageType type;

    GroupConversationMessageResponse(Long groupConversationId, Long senderId, String senderName, String senderAvatar,
            Long groupConversationMessageId, String content,
            java.sql.Timestamp sentAt, java.sql.Timestamp updatedAt, Short type) {
        this.groupConversationId = groupConversationId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
        this.groupConversationMessageId = groupConversationMessageId;
        this.content = content;
        this.sentAt = sentAt != null ? sentAt.toLocalDateTime() : null;
        this.updatedAt = updatedAt != null ? updatedAt.toLocalDateTime() : null;
        MessageType.Converter converter = new MessageType.Converter();
        this.type = converter.fromShort(type);
    }

}
