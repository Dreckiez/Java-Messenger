package com.example.spring_security.dto.response;

import lombok.Getter;

@Getter
public class UserSearchResponse extends BaseUserResponse {

    private String status;

    public UserSearchResponse(Long userId, String username, String firstName, String lastName, String avatarUrl, String status) {
        super(userId, username, firstName, lastName, avatarUrl);
        this.status = status;
    }

}
