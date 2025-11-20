package com.example.spring_security.repository;

import com.example.spring_security.dto.response.GroupConversationMessageResponse;
import com.example.spring_security.entities.GroupConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupConversationMessageRepository
        extends JpaRepository<GroupConversationMessage, Long> {

    @Query(value = """
          SELECT
            gcm.sender_id AS senderId,
            gcm.group_conversation_message_id AS groupConversationMessageId,
            gcm.content AS content,
            gcm.sent_at AS sent_at,
            gcm.updated_at AS updated_at,
            gcm.type AS type
          FROM group_conversation_message gcm
          LEFT JOIN delete_group_conversation dgc ON dgc.member_id = :userId 
          AND dgc.group_conversation_id = :groupConversationId
          WHERE gcm.group_conversation_id = :groupConversationId
          AND (:cursorId IS NULL OR gcm.group_conversation_message_id < :cursorId)
          AND (dgc.deleted_at IS NULL OR gcm.sent_at > dgc.deleted_at)
          AND NOT EXISTS (
                            SELECT 1 
                            FROM delete_group_conversation_message dgcm
                            WHERE dgcm.group_conversation_message_id = gcm.group_conversation_message_id
                            AND (dgcm.is_all = TRUE OR dgcm.member_id = :userId)
                         )
          ORDER BY gcm.group_conversation_message_id DESC
          LIMIT 50
           """, nativeQuery = true)
    List<GroupConversationMessageResponse> findMessages(Long userId, Long groupConversationId, Long cursorId);

}
