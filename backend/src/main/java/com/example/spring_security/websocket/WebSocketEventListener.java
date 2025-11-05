package com.example.spring_security.websocket;

import com.example.spring_security.dto.response.UserFriendResponse;
import com.example.spring_security.entities.Friend;
import com.example.spring_security.entities.User;
import com.example.spring_security.repository.FriendRepository;
import com.example.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() == null) return;

        String username = accessor.getUser().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            user.setIsOnline(true);
            userRepository.save(user);

            List<Friend> friends = friendRepository.findByIdUserId1OrIdUserId2(user.getUserId(), user.getUserId());

            friends.forEach(f -> {
                User friend = f.getId().getUserId1().equals(user.getUserId()) ? f.getUser2Entity() : f.getUser1Entity();

                UserFriendResponse payload = UserFriendResponse.builder()
                    .userId(friend.getUserId())
                    .fullName(friend.getLastName() + " " + friend.getFirstName())
                    .avatarUrl(friend.getAvatarUrl())
                    .isOnline(friend.getIsOnline())
                    .address(friend.getAddress())
                    .build();

                messagingTemplate.convertAndSend("/client/status/" + friend.getUserId(), payload);
            });
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() == null) return;

        String username = accessor.getUser().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            user.setIsOnline(false);
            userRepository.save(user);

            List<Friend> friends = friendRepository.findByIdUserId1OrIdUserId2(user.getUserId(), user.getUserId());

            friends.forEach(f -> {
                User friend = f.getId().getUserId1().equals(user.getUserId()) ? f.getUser2Entity() : f.getUser1Entity();

                UserFriendResponse payload = UserFriendResponse.builder()
                    .userId(friend.getUserId())
                    .fullName(friend.getLastName() + " " + friend.getFirstName())
                    .avatarUrl(friend.getAvatarUrl())
                    .isOnline(friend.getIsOnline())
                    .address(friend.getAddress())
                    .build();

                messagingTemplate.convertAndSend("/client/status/" + friend.getUserId(), payload);
            });
        }
    }
}
