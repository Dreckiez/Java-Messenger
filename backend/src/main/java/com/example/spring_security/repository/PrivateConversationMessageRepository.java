package com.example.spring_security.repository;

import com.example.spring_security.entities.PrivateConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateConversationMessageRepository extends JpaRepository<PrivateConversationMessage, Long> {

}
