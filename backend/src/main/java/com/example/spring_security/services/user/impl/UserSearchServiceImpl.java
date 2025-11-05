package com.example.spring_security.services.user.impl;


import com.example.spring_security.dto.response.BaseUserResponse;
import com.example.spring_security.entities.User;
import com.example.spring_security.exception.CustomException;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.services.user.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSearchServiceImpl implements UserSearchService {

    private final UserRepository userRepository;

    public List<BaseUserResponse> search(Long currentUserId, String keyword) {
        if (!userRepository.existsById(currentUserId))
            throw new CustomException(HttpStatus.NOT_FOUND, "User no longer exists.");
        List<User> listUser = userRepository.searchUserByUsernameOrFullName(currentUserId, keyword);
        List<BaseUserResponse> listBaseUserResponse = listUser.stream()
                .map(u -> BaseUserResponse.builder()
                        .userId(u.getUserId())
                        .fullName(u.getLastName() + " " + u.getFirstName())
                        .avatarUrl(u.getAvatarUrl())
                        .build())
                .collect(Collectors.toList());
        return listBaseUserResponse;
    }
}
