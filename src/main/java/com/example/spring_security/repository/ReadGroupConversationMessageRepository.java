package com.example.spring_security.repository;

import com.example.spring_security.entities.ReadGroupConversationMessage;
import com.example.spring_security.entities.ReadGroupConversationMessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadGroupConversationMessageRepository
        extends JpaRepository<ReadGroupConversationMessage, ReadGroupConversationMessageId> {
}
