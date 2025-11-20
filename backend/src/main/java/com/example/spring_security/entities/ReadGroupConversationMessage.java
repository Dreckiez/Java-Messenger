package com.example.spring_security.entities;

import com.example.spring_security.entities.Enum.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "read_group_conversation_message")
public class ReadGroupConversationMessage {
    @EmbeddedId
    private ReadGroupConversationMessageId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_conversation_message_id", nullable = false)
    private GroupConversationMessage groupConversationMessage;

    @Column(name = "read_at")
    private LocalDateTime readAt;
}
