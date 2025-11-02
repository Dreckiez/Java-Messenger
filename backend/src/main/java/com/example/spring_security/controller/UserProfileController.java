package com.example.spring_security.controller;


import com.example.spring_security.dto.request.ChangPasswordRequest;
import com.example.spring_security.dto.request.UpdateProfileRequest;
import com.example.spring_security.entities.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.services.UserProfileService;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userProfileService.getProfile(user));
    }

    @PatchMapping("/update-profile")
    public ResponseEntity<User> updateProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userProfileService.updateProfile(updateProfileRequest, user));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangPasswordRequest changPasswordRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userProfileService.changePassword(changPasswordRequest, user));
    }

    @GetMapping("/create-token")
    public ResponseEntity<Map<String, String>> createToken() {
        return new ResponseEntity.ok(userProfileService.)
    }
}


