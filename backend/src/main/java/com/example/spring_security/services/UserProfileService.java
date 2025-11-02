package com.example.spring_security.services;

import com.example.spring_security.dto.request.ChangPasswordRequest;
import com.example.spring_security.dto.request.ChangeEmailRequest;
import com.example.spring_security.dto.request.UpdateProfileRequest;
import com.example.spring_security.entities.User;

import java.util.Map;

public interface UserProfileService {
    public User getProfile(User user);
    public User updateProfile(UpdateProfileRequest updateProfileRequest, User user);
    public Map<String, String> changePassword(ChangPasswordRequest changPasswordRequest, User user);
    public Map<String, String> changeEmail(String token, ChangeEmailRequest changeEmailRequest, User user);
    public Map<String, String> createToken(User user);
    public Map<String, String> applyChangeEmail(String token, ChangeEmailRequest changeEmailRequest);
}
