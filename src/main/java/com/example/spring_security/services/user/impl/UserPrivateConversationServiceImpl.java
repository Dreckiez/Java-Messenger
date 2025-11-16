package com.example.spring_security.services.user.impl;

import com.example.spring_security.dto.response.PrivateConversationResponse;
import com.example.spring_security.entities.*;
import com.example.spring_security.repository.DeletePrivateConversationRepository;
import com.example.spring_security.repository.PrivateConversationRepository;
import com.example.spring_security.services.user.UserPrivateConversationService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserPrivateConversationServiceImpl implements UserPrivateConversationService {

    private final PrivateConversationRepository privateConversationRepository;

    private final EntityManager entityManager;

    private final DeletePrivateConversationRepository deletePrivateConversationRepository;


    public Map<String, String> create(Long userId1, Long userId2) {

        PrivateConversation privateConversation = PrivateConversation.builder()
                .user1(entityManager.getReference(User.class, userId1))
                .user2(entityManager.getReference(User.class, userId2))
                .createdAt(LocalDateTime.now())
                .previewMessage(null)
                .build();

        privateConversationRepository.save(privateConversation);

        Map<String, String> msg = new HashMap<>();

        msg.put("message", "Created private conversation successfully");


        return msg;
    }

    public Map<String, String> remove(Long removerId, Long privateConversationId) {

        DeletePrivateConversationId id = DeletePrivateConversationId.builder()
                .userId(removerId)
                .privateConversationId(privateConversationId)
                .build();

        DeletePrivateConversation deletePrivateConversation = deletePrivateConversationRepository.findById(id).orElse(null);


         if (deletePrivateConversation == null) deletePrivateConversation = DeletePrivateConversation.builder()
                                                                            .id(id)
                                                                            .deletedAt(LocalDateTime.now())
                                                                            .build();
         else deletePrivateConversation.setDeletedAt(LocalDateTime.now());

         deletePrivateConversationRepository.save(deletePrivateConversation);

         Map<String, String> msg = new HashMap<>();
         msg.put("message", "Removed successfully!");
         return msg;
    }

    public List<PrivateConversationResponse> get(Long userId) {
        List<PrivateConversationResponse> privateConversationList =
                privateConversationRepository.findAllOf(userId);

        return privateConversationList;
    }
}
