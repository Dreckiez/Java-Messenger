package com.example.spring_security.dto.response;

import com.example.spring_security.entities.Enum.ConversationType;
import com.example.spring_security.entities.Enum.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.Message;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
public class ConversationListItem {
    private Long id;
    private String conversationType;

    private String name;
    private String avatarUrl;

    private String previewContent;
    private LocalDateTime previewTime;
    private MessageType messageType;

    public ConversationListItem(
            String type,
            Long id,
            String name,
            String avatarUrl,
            String previewContent,
            java.sql.Timestamp previewTime,
            Short previewType
    ) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.previewContent = previewContent;

        this.previewTime = previewTime != null ? previewTime.toLocalDateTime() : null;

        this.conversationType = type;

        MessageType.Converter converter = new MessageType.Converter();
        this.messageType = converter.fromShort(previewType);
    }
}

