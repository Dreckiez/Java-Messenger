package com.example.spring_security.services.user.impl;

import com.example.spring_security.dto.request.PrivateConversationMessageRequest;
import com.example.spring_security.dto.response.ListPrivateConversationMessageResponse;
import com.example.spring_security.dto.response.PrivateConversationMessageResponse;
import com.example.spring_security.dto.response.PrivateConversationResponse;
import com.example.spring_security.entities.*;
import com.example.spring_security.exception.CustomException;
import com.example.spring_security.repository.*;
import com.example.spring_security.services.user.UserPrivateConversationService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPrivateConversationServiceImpl implements UserPrivateConversationService {

    private final ReadPrivateConversationMessageRepository readPrivateConversationMessageRepository;

    private final PrivateConversationRepository privateConversationRepository;

    private final EntityManager entityManager;

    private final DeletePrivateConversationRepository deletePrivateConversationRepository;

    private final PrivateConversationMessageRepository privateConversationMessageRepository;

    private final DeletePrivateConversationMessageRepository deletePrivateConversationMessageRepository;

    private final UserRepository userRepository;


    public void saveRead(Long userId, Long privateConversationId, Long privateConversationMessageId) {

        ReadPrivateConversationMessageId readPrivateConversationMessageId = ReadPrivateConversationMessageId.builder()
                .userId(userId)
                .privateConversationId(privateConversationId)
                .build();

        ReadPrivateConversationMessage readPrivateConversationMessage =
                readPrivateConversationMessageRepository.findById(readPrivateConversationMessageId).orElse(null);

        if (readPrivateConversationMessage == null) {
            readPrivateConversationMessage = ReadPrivateConversationMessage.builder()
                    .Id(readPrivateConversationMessageId)
                    .readAt(LocalDateTime.now()).build();
        }
        else {

            if (readPrivateConversationMessage.getPrivateConversationMessage().getPrivateConversationMessageId() == privateConversationMessageId)
                return;

            readPrivateConversationMessage.setReadAt(LocalDateTime.now());
            readPrivateConversationMessage.setPrivateConversationMessage(entityManager.getReference
                    (PrivateConversationMessage.class, privateConversationMessageId));
        }

        readPrivateConversationMessageRepository.save(readPrivateConversationMessage);
    }

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

    public Map<String, String> removeConversation(Long removerId, Long privateConversationId) {

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

    public List<PrivateConversationResponse> getConversation(Long userId) {
        List<PrivateConversationResponse> privateConversationList =
                privateConversationRepository.findAllOf(userId);

        return privateConversationList;
    }

    public Map<String, String> send
            (Long senderId,
             Long privateConversationId,
             PrivateConversationMessageRequest privateConversationMessageRequest) {

        PrivateConversation privateConversation = privateConversationRepository
                .findById(privateConversationId).orElseThrow(
                        () -> new CustomException(HttpStatus.NOT_FOUND, "Illegal behavior. There is no conversation.")
                );

        PrivateConversationMessage privateConversationMessage
                = PrivateConversationMessage.builder()
                .privateConversation(entityManager.getReference(PrivateConversation.class, privateConversationId))
                .sender(entityManager.getReference(User.class, senderId))
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .type(privateConversationMessageRequest.getType())
                .content(privateConversationMessageRequest.getContent())
                .build();

        privateConversationMessageRepository.save(privateConversationMessage);

        privateConversation.setPreviewMessage(privateConversationMessage);

        privateConversationRepository.save(privateConversation);

        Map<String, String> msg = new HashMap<>();

        msg.put("message", "Send successfully");

        return msg;
    }

    public Map<String, String> removeMessage(Long userId, Long privateConversationMessageId, boolean isAll) {

        PrivateConversationMessage privateConversationMessage = privateConversationMessageRepository
                .findById(privateConversationMessageId).orElseThrow(
                        () -> new CustomException(HttpStatus.NOT_FOUND, "This message is no longer exists.")
                );

        if (privateConversationMessage.getSender().getUserId() != userId && isAll) {
            throw new CustomException(HttpStatus.CONFLICT, "Illegal behavior. This user is not allowed to remove both sides.");
        }

        DeletePrivateConversationMessageId deletePrivateConversationMessageId
                = DeletePrivateConversationMessageId.builder()
                .userId(userId)
                .privateConversationMessageId(privateConversationMessageId)
                .build();

        DeletePrivateConversationMessage deletePrivateConversationMessage = deletePrivateConversationMessageRepository
                .findById(deletePrivateConversationMessageId).orElse(null);

        if (deletePrivateConversationMessage != null)
            throw new CustomException(HttpStatus.CONFLICT, "Illegal behavior. This message is already removed.");

         deletePrivateConversationMessage = DeletePrivateConversationMessage.builder()
                .id(deletePrivateConversationMessageId)
                .deletedAt(LocalDateTime.now())
                .isAll(isAll)
                .user(userRepository.findById(userId).orElse(null))
                .build();

        deletePrivateConversationMessageRepository.save(deletePrivateConversationMessage);

        Map<String, String> msg = new HashMap<>();

        msg.put("message", "Remove successfully.");

        return msg;

    }

    public ListPrivateConversationMessageResponse getMessage(Long userId, Long privateConversationId, Long cursorId) {
        PrivateConversation privateConversation = privateConversationRepository
                .findById(privateConversationId).orElse(null);

        if (privateConversation == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Illegal behavior. There is no private conversation.");
        }

        LocalDateTime deletedAt = deletePrivateConversationRepository.findLastest(userId, privateConversationId).orElse(null);

        List<PrivateConversationMessageResponse> privateConversationMessageResponseList =
                privateConversationMessageRepository.findMessages(userId, privateConversationId, cursorId)
                        .stream().filter(
                                p -> (deletedAt == null || p.getSentAt().isAfter(deletedAt))
                        ).collect(Collectors.toList());

        if (!privateConversationMessageResponseList.isEmpty()) saveRead(userId, privateConversationId, privateConversationMessageResponseList.getFirst().getPrivateConversationMessageId());

        User user = userId == privateConversation.getUser1().getUserId()
                ? privateConversation.getUser2() : privateConversation.getUser1();

        ListPrivateConversationMessageResponse listPrivateConversationMessageResponse
                = ListPrivateConversationMessageResponse.builder()
                .privateConversationMessageResponseList(privateConversationMessageResponseList)
                .userId(user.getUserId())
                .fullName(user.getLastName() + " " + user.getFirstName())
                .avatarUrl(user.getAvatarUrl())
                .isOnline(user.getIsOnline())
                .privateConversationId(privateConversationId)
                .build();

        return listPrivateConversationMessageResponse;
    }

}
