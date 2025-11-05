package com.example.spring_security.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserFriendResponse extends BaseUserResponse {
    private Boolean isOnline;
    private String address;
}

