package com.example.spring_security.controller.user;
import com.example.spring_security.dto.response.PrivateConversationResponse;
import com.example.spring_security.entities.PrivateConversation;
import com.example.spring_security.entities.User;
import com.example.spring_security.services.user.UserPrivateConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/user/private-conversations")
public class UserPrivateConversationController {

    private final UserPrivateConversationService userPrivateConversationService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> remove(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(
                userPrivateConversationService.remove(user.getUserId(), id));
    }

    @GetMapping("")
    public ResponseEntity<List<PrivateConversationResponse>> getList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userPrivateConversationService.get(user.getUserId()));
    }

//    @PostMapping("/private-messages/{id}")
//    public ResponseEntity<Map<String, String>> send(@AuthenticationPrincipal User sender, @PathVariable("id") Long receiverId) {
//        return
//    }
}
