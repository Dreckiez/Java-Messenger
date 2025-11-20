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

    Optional<PrivateConversation> findById(Long privateConversationId);
}
