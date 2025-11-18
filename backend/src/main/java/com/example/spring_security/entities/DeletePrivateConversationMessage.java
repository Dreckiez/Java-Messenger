package com.example.spring_security.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "delete_private_conversation_message")
public class DeletePrivateConversationMessage {

    @EmbeddedId
    DeletePrivateConversationMessageId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable=false, updatable = false)
    User user;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @Column(name = "is_all")
    Boolean isAll;
}
