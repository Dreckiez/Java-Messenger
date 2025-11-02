package com.example.spring_security.services.impl;

import com.example.spring_security.dto.request.ChangPasswordRequest;
import com.example.spring_security.dto.request.ChangeEmailRequest;
import com.example.spring_security.dto.request.UpdateProfileRequest;
import com.example.spring_security.entities.Enum.Gender;
import com.example.spring_security.entities.Token.VerifyToken;
import com.example.spring_security.entities.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.repository.VerifyTokenRepository;
import com.example.spring_security.services.EmailService;
import com.example.spring_security.services.UserProfileService;
import com.example.spring_security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final VerifyTokenRepository verifyTokenRepository;

    private final EmailService emailService;

    public User getProfile(User user) {
        return userRepository.save(user);
    }
    public User updateProfile(UpdateProfileRequest updateProfileRequest, User user) {

        if (updateProfileRequest.getFirstName() != null) {
            user.setFirstName(updateProfileRequest.getFirstName());
        }

        if (updateProfileRequest.getLastName() != null) {
            user.setLastName(updateProfileRequest.getFirstName());
        }

        if (updateProfileRequest.getCity() != null) {
            user.setCity(updateProfileRequest.getCity());
        }

        if (updateProfileRequest.getDistrict() != null) {
            user.setDistrict(updateProfileRequest.getDistrict());
        }

        if (updateProfileRequest.getStreet() != null) {
            user.setStreet(updateProfileRequest.getStreet());
        }

        if (updateProfileRequest.getGender() != null) {
            user.setGender(updateProfileRequest.getGender());
        }

        if (updateProfileRequest.getBirthday() != null) {
            user.setBirthday(updateProfileRequest.getBirthday());
        }

        if (updateProfileRequest.getAvatarUrl() != null) {
            user.setAvatarUrl(updateProfileRequest.getAvatarUrl());
        }

        return userRepository.save(user);
    }

    public Map<String, String> changPassword(ChangPasswordRequest changPasswordRequest, User user) {
        if (!passwordEncoder.matches(changPasswordRequest.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Password is incorrect.");
        }

        if (!changPasswordRequest.getNewPassword().equals(changPasswordRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }
        user.setHashPassword(passwordEncoder.encode(changPasswordRequest.getNewPassword()));
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Password changed successfully. Please use your new password for future logins.");
        return msg;
    }



    public Map<String, String> changeEmail(String token, ChangeEmailRequest changeEmailRequest, User user) {

        VerifyToken verifyToken = verifyTokenRepository.findByToken(token).orElse(null);

        if (verifyToken == null || !verifyToken.getUser().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("Your email verification request could not be completed. Please request a new verification token.");
        }

        if (verifyToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("This email verification token is no longer valid. Please request a new verification link.");
        }

        if (changeEmailRequest.getEmail().equals(user.getEmail())) {
            throw new RuntimeException("The new email address must be different from the current one.");
        }
        if (userRepository.existsByEmail(changeEmailRequest.getEmail())) {
            throw new RuntimeException("This email address is already in use by another account.");
        }

        String randomToken = UUID.randomUUID().toString();
        verifyToken.setToken(randomToken);

        LocalDateTime expiryDate = verifyToken.getExpiryDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, MMM dd, yyyy");
        String formattedExpiry = expiryDate.format(formatter);

        String link = "http://localhost:8080/api/chat/user/profile" + randomToken;

        emailService.sendEmail(
                user.getEmail(),
                "Verify your new email",
                "Hi " + user.getUsername() + ",\n\n" +
                        "Please click the link below to verify your new email:\n" +
                        link + "\n\n" +
                        "This link will expire at " + formattedExpiry + "."
        );
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Please check your new email to verify the change.");
        return msg;
    }



    public Map<String, String> createToken(User user) {
        VerifyToken verifyToken = verifyTokenRepository.findByUser(user).orElse(new VerifyToken());

        if (verifyToken.getId() != null && verifyToken.getDateTimeCreated().plusMinutes(10).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Please wait before sending another change email request.");
        }

        String randomToken = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        verifyToken.setUser(user);
        verifyToken.setToken(randomToken);
        verifyToken.setDateTimeCreated(LocalDateTime.now());
        verifyToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        Map<String, String> msg = new HashMap<>();
        msg.put("message", randomToken);
        return msg;
    }

    public Map<String, String> applyChangeEmail(String token) {
        VerifyToken verifyToken = verifyTokenRepository.findByToken(token).orElseThrow("")
    }
}
