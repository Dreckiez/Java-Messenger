package com.example.spring_security.services.user;

import com.example.spring_security.dto.response.BaseUserResponse;
import com.example.spring_security.dto.response.UserSearchResponse;

import java.util.List;

public interface UserSearchService {
    List<UserSearchResponse> search(Long currentUserId, String keyword);
}
