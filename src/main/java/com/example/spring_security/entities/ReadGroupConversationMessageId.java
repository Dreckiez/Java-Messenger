package com.example.spring_security.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadGroupConversationMessageId implements Serializable {

    @Column(name = "group_conversation_id")
    private Long groupConversationId;


    @Column(name = "member_id")
    private Long memberId;
}
