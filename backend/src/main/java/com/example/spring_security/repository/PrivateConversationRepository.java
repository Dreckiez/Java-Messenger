package com.example.spring_security.repository;

import com.example.spring_security.dto.response.PrivateConversationResponse;
import com.example.spring_security.entities.PrivateConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrivateConversationRepository extends JpaRepository<PrivateConversation, Long> {

    List<PrivateConversation> findAll();

    List<PrivateConversation> findByUser1UserIdOrUser2UserId(Long user1Id, Long user2Id);

    @Query(value = """
            SELECT *
            FROM private_conversation pc
            WHERE (
                    (pc.user1_id = :user1Id AND pc.user2_id = :user2Id)
                    OR
                    (pc.user1_id = :user2Id AND pc.user2_id = :user1Id)           
                  )
            """, nativeQuery = true)
    Optional<PrivateConversation> findByUser1UserIdAndUser2UserId(Long user1Id, Long user2Id);

    @Query(value = """
            SELECT
               u.user_id AS userId,
               CONCAT(u.last_name, ' ', u.first_name) AS fullName,
               u.avatar_url AS avatarUrl,
               u.is_online AS isOnline,
               pc.private_conversation_id AS privateConversationId,
               pc.created_at AS createdAt,
               pcm
            FROM private_conversation pc
            JOIN user_info u ON (
                                    (pc.user1_id = :userId AND pc.user2_id = u.user_id)
                                    OR
                                    (pc.user2_id = :userId AND pc.user1_id = u.user_id)
                                )
            LEFT JOIN private_conversation_message pcm ON pcm.private_conversation_message_id = pc.preview_message_id
            """, nativeQuery = true)
    List<PrivateConversationResponse> findAllOf(Long userId);

    Optional<PrivateConversation> findById(Long privateConversationId);
}
