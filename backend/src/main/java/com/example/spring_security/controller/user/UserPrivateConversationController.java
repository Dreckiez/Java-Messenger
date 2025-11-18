package com.example.spring_security.controller.user;
import com.example.spring_security.dto.request.PrivateConversationMessageRequest;
import com.example.spring_security.dto.response.ListPrivateConversationMessageResponse;
import com.example.spring_security.dto.response.PrivateConversationResponse;
import com.example.spring_security.entities.PrivateConversation;
import com.example.spring_security.entities.User;
import com.example.spring_security.services.user.UserPrivateConversationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Map<String, String>> removeConversation(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(
                userPrivateConversationService.removeConversation(user.getUserId(), id));
    }

    @GetMapping("")
    public ResponseEntity<List<PrivateConversationResponse>> getConversationList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userPrivateConversationService.getConversation(user.getUserId()));
    }

    @PostMapping("/{id}/private-conversation-messages")
    public ResponseEntity<Map<String, String>> send
            (@AuthenticationPrincipal User sender,
            @PathVariable("id") Long privateConversationId,
            @Valid @RequestBody PrivateConversationMessageRequest privateConversationMessageRequest) {

        return ResponseEntity.ok(userPrivateConversationService
                .send(sender.getUserId(), privateConversationId, privateConversationMessageRequest));
    }


    @DeleteMapping("/private-conversation-messages/{id}")
    public ResponseEntity<Map<String, String>> removeMessage
            (@AuthenticationPrincipal User remover,
             @PathVariable("id") Long privateConversationMessageId,
            @RequestParam(value = "isAll", required = true) boolean isAll) {

        return ResponseEntity.ok(userPrivateConversationService.removeMessage(remover.getUserId(), privateConversationMessageId, isAll));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListPrivateConversationMessageResponse> getMessageList
            (@AuthenticationPrincipal User user, @PathVariable("id") Long privateConversationId,
            @RequestParam(value = "cursorId", required = false) Long cursorId) {
        return ResponseEntity.ok(userPrivateConversationService.getMessage(user.getUserId(), privateConversationId, cursorId));
    }
}
