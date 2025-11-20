package com.example.spring_security.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "read_private_conversation_message")
@Builder
public class ReadPrivateConversationMessage {
    @EmbeddedId
    private ReadPrivateConversationMessageId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "private_conversation_message_id", nullable = false)
    private PrivateConversationMessage privateConversationMessage;

    @Column(name = "read_at")
    private LocalDateTime readAt;
}
