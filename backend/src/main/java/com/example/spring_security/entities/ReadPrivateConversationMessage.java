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
    private ReadPrivateConversationMessageId Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "private_conversation_message_id")
    private PrivateConversationMessage privateConversationMessage;

    @Column(name = "read_at")
    LocalDateTime readAt;
}
