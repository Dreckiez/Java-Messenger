package com.example.spring_security.dto.response;

import com.example.spring_security.entities.GroupConversationMessage;
import com.example.spring_security.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class GroupConversationItemListResponse {

    private Long groupConversationId;

    private String avatarUrl;

    private String groupName;

    private Long ownerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_encrypted")
    private Boolean isEncrypted;

    public GroupConversationItemListResponse(Long groupConversationId, String avatarUrl, String groupName,
                                             Long ownerId, java.sql.Timestamp createdAt, Boolean isEncrypted) {
        this.groupConversationId = groupConversationId;
        this.avatarUrl = avatarUrl;
        this.groupName = groupName;
        this.ownerId = ownerId;
        this.createdAt = createdAt != null ? createdAt.toLocalDateTime() : null;
        this.isEncrypted = isEncrypted;
    }

}
