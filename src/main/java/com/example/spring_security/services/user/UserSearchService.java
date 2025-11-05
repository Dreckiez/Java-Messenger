package com.example.spring_security.services;

import com.example.spring_security.dto.response.BaseUserResponse;

import java.util.List;

public interface UserSearchService {
    List<BaseUserResponse> search(Long currentUserId, String keyword);
}
