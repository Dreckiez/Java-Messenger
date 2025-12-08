package com.example.spring_security.controller.user;

import com.example.spring_security.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public class UserConversationController {

    public ResponseEntity<?> getAllConversation(@AuthenticationPrincipal User user) {

    }
}
