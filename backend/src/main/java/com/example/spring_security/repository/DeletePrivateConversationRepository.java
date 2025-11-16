package com.example.spring_security.repository;

import com.example.spring_security.entities.DeletePrivateConversation;
import com.example.spring_security.entities.DeletePrivateConversationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeletePrivateConversationRepository extends JpaRepository<DeletePrivateConversation, DeletePrivateConversationId> {
    Optional<DeletePrivateConversation> findById(DeletePrivateConversationId id);

}
