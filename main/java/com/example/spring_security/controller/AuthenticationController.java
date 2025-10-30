package com.example.spring_security.controller;

import com.example.spring_security.dto.*;
import com.example.spring_security.entities.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final  AuthenticationService authenticationService;

    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationService.signin(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
    @GetMapping("/verify")
    public ResponseEntity<User> verifyEmail(@RequestParam("token") String token) {
        return ResponseEntity.ok(authenticationService.verifyToken(token));
    }
    @PostMapping("/create-verification-token")
    public ResponseEntity<Map<String, String>> createVerificationToken(@RequestBody VerificationRequest verificationRequest) {
        return ResponseEntity.ok(authenticationService.createVerificationToken(verificationRequest));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody VerificationRequest verificationRequest) {
        return ResponseEntity.ok(authenticationService.resetPassword(verificationRequest));
    }

    @GetMapping("/test-api")
    public ResponseEntity<Optional<User>> testApi() {
        return ResponseEntity.ok(userRepository.findByUsername("admin123"));
    }
}
