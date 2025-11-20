package com.example.spring_security.dto.response;

import lombok.Getter;

@Getter
public class BasicUserResponse extends BaseUserResponse{

    private Boolean isOnline;

    BasicUserResponse(Long userId, String fullName, String avatarUrl, Boolean isOnline) {
        super(userId, fullName, avatarUrl);
        this.isOnline = isOnline;
    }

}
