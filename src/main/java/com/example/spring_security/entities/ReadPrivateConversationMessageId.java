package com.example.spring_security.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadPrivateConversationMessageId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "private_conversation_id")
    private Long privateConversationId;
}
