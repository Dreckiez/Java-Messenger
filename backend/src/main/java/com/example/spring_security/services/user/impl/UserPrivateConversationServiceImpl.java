package com.example.spring_security.services.user.impl;


import com.example.spring_security.dto.request.SendMessageRequest;
import com.example.spring_security.dto.response.*;
import com.example.spring_security.entities.*;
import com.example.spring_security.entities.Enum.RealTimeAction;
import com.example.spring_security.exception.CustomException;
import com.example.spring_security.repository.*;
import com.example.spring_security.services.user.UserPrivateConversationService;
import com.example.spring_security.websocket.WebSocketMessageService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPrivateConversationServiceImpl implements UserPrivateConversationService {

    private final WebSocketMessageService webSocketMessageService;

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
            System.out.println("here");
            readPrivateConversationMessage = ReadPrivateConversationMessage.builder()
                    .id(readPrivateConversationMessageId)
                    .privateConversationMessage(entityManager.getReference(PrivateConversationMessage.class, privateConversationMessageId))
                    .user(entityManager.getReference(User.class, userId))
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

        PrivateConversation privateConversation = privateConversationRepository.findById(privateConversationId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "This conversation no longer exists."));


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

    public SendMessageResponse sendMessage
            (Long senderId,
             Long privateConversationId,
             SendMessageRequest sendMessageRequest) {

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
                .type(sendMessageRequest.getType())
                .content(sendMessageRequest.getContent())
                .build();

        privateConversationMessageRepository.save(privateConversationMessage);

        privateConversation.setPreviewMessage(privateConversationMessage);

        privateConversationRepository.save(privateConversation);


        //  building for sender's info

        User sender = userRepository.findById(senderId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "Sender not found")
        );

        PrivateMessageWsResponse privateMessageWsResponse = PrivateMessageWsResponse.builder()
                .userId(senderId)
                .username(sender.getUsername())
                .firstName(sender.getFirstName())
                .lastName(sender.getLastName())
                .avatarUrl(sender.getAvatarUrl())
                .privateConversationMessageId(privateConversationMessage.getPrivateConversationMessageId())
                .content(privateConversationMessage.getContent())
                .sentAt(privateConversationMessage.getSentAt())
                .updatedAt(privateConversationMessage.getUpdatedAt())
                .type(privateConversationMessage.getType())
                .realTimeAction(RealTimeAction.SEND)
                .build();

        // push for receiver

        User receiver = !privateConversation.getUser1().getUserId().equals(senderId) ? privateConversation.getUser1() : privateConversation.getUser2();

        webSocketMessageService.sendMessageToUser(receiver.getUsername(), privateMessageWsResponse);

        return SendMessageResponse.builder()
                .messageId(privateConversationMessage.getPrivateConversationMessageId())
                .content(privateConversationMessage.getContent())
                .sentAt(privateConversationMessage.getSentAt())
                .updatedAt(privateConversationMessage.getUpdatedAt())
                .type(privateConversationMessage.getType())
                .build();
    }

    public Map<String, String> removeMessage(Long userId, Long privateConversationId, Long privateConversationMessageId, boolean isAll) {

        PrivateConversation privateConversation = privateConversationRepository.findById(privateConversationId)
                .orElseThrow(
                        () -> new CustomException(HttpStatus.NOT_FOUND, "This conversation no longer exists.")
                );

        PrivateConversationMessage privateConversationMessage = privateConversationMessageRepository
                .findById(privateConversationMessageId).orElseThrow(
                        () -> new CustomException(HttpStatus.NOT_FOUND, "This message is no longer exists.")
                );

        if (privateConversationMessage.getPrivateConversation().getPrivateConversationId() != privateConversationId) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Mismatch conversation and message.");
        }
        else if (userId != privateConversation.getUser1().getUserId() && userId != privateConversation.getUser2().getUserId()) {
            throw new CustomException(HttpStatus.FORBIDDEN,
                    "Illegal behavior. This user is not allowed to perform any actions to this conversation.");
        }
        else if (privateConversationMessage.getSender().getUserId() != userId && isAll) {
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
                .user(userRepository.findById(userId).orElseThrow(
                        () -> new CustomException(HttpStatus.NOT_FOUND, "User no longer exists.")
                ))
                .build();

        deletePrivateConversationMessageRepository.save(deletePrivateConversationMessage);

        if (isAll) {

            User partner = !privateConversation.getUser1().getUserId().equals(userId) ? privateConversation.getUser1() : privateConversation.getUser2();

            User remover = userRepository.findById(userId).orElseThrow(
                    () -> new CustomException(HttpStatus.NOT_FOUND, "Remover not found.")
            );

            DeletePrivateMessageWsResponse deletePrivateMessageWsResponse = DeletePrivateMessageWsResponse.builder()
                    .userId(remover.getUserId())
                    .privateConversationMessageId(privateConversationMessage.getPrivateConversationMessageId())
                    .privateConversationId(privateConversation.getPrivateConversationId())
                    .realTimeAction(RealTimeAction.DELETE)
                    .build();

            webSocketMessageService.sendDeleteMessage(partner.getUsername(), deletePrivateMessageWsResponse);

        }

        Map<String, String> msg = new HashMap<>();

        msg.put("message", "Removed successfully.");

        return msg;

    }

    public ListPrivateConversationMessageResponse getMessages(Long userId, Long privateConversationId, Long cursorId) {
        PrivateConversation privateConversation = privateConversationRepository
                .findById(privateConversationId).orElseThrow(
                        () -> new CustomException(HttpStatus.NOT_FOUND, "Illegal behaivor. This conversation no longer exists.")
                );


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
                =  ListPrivateConversationMessageResponse.builder()
                .privateConversationMessageResponseList(privateConversationMessageResponseList)
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatarUrl(user.getAvatarUrl())
                .isOnline(user.getIsOnline())
                .privateConversationId(privateConversationId)
                .build();

        return listPrivateConversationMessageResponse;
    }

}
