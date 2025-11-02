package com.example.spring_security.dto.response;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String token;

    private String refreshToken;
}
