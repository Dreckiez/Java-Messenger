package com.example.spring_security.dto.response;

import com.example.spring_security.entities.Enum.Gender;
import com.example.spring_security.entities.Enum.Role;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserProfileResponse extends BaseUserResponse {
    private String address;
    private LocalDate birthDay;
    private Gender gender;
    private Role role;
    private LocalDateTime joinedAt;
}
