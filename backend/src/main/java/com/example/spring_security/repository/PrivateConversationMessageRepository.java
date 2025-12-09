package com.example.spring_security.repository;

import com.example.spring_security.dto.response.MessageSearchResponse;
import com.example.spring_security.dto.response.PrivateConversationMessageResponse;
import com.example.spring_security.entities.PrivateConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PrivateConversationMessageRepository extends JpaRepository<PrivateConversationMessage, Long> {


    @Query(value = """
        SELECT
            pcm.private_conversation_message_id AS privateConversationMessageId,
            pcm.sender_id AS senderId,
            pcm.content AS content,
            pcm.sent_at AS sentAt,
            pcm.updated_at AS updatedAt,
            pcm.type AS type,
            pcm.is_read AS isRead,
            pcm.read_at AS readAt
        FROM private_conversation_message pcm
        LEFT JOIN delete_private_conversation dpc
               ON dpc.private_conversation_id = pcm.private_conversation_id
              AND dpc.user_id = :userId
        WHERE pcm.private_conversation_id = :privateConversationId
        
          AND (:cursorId IS NULL OR pcm.private_conversation_message_id < :cursorId)
          AND NOT EXISTS (
                SELECT 1
                FROM delete_private_conversation_message dpcm
                WHERE dpcm.private_conversation_message_id = pcm.private_conversation_message_id
                  AND (dpcm.is_all = TRUE OR dpcm.user_id = :userId)
          )
    
          AND (dpc.deleted_at IS NULL OR pcm.sent_at > dpc.deleted_at)
    
        ORDER BY pcm.private_conversation_message_id DESC
        LIMIT 50
        """, nativeQuery = true)
    List<PrivateConversationMessageResponse> findMessages(
            Long userId,
            Long privateConversationId,
            Long cursorId
    );





}
