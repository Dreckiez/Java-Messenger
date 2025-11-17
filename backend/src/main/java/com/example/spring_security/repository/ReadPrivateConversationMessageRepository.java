package com.example.spring_security.repository;

import com.example.spring_security.entities.ReadPrivateConversationMessage;
import com.example.spring_security.entities.ReadPrivateConversationMessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadPrivateConversationMessageRepository
        extends JpaRepository<ReadPrivateConversationMessage, ReadPrivateConversationMessageId> {
    Optional<ReadPrivateConversationMessage> findById(ReadPrivateConversationMessageId readPrivateConversationMessageId);
}
