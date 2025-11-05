package com.example.spring_security.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FriendRequestResponse extends BaseUserResponse {
    private LocalDateTime sentAt;
}
