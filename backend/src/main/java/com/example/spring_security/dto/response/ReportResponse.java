package com.example.spring_security.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class ReportResponse {
    private Long reporterId;
    private Long reportedUserId;
    private String reporterAvtUrl;
    private String reportedUserAvtUrl;
    private String reporterFullName;
    private String reportedUserFullName;
    private String reporterUsername;
    private String reportedUserUsername;
    private String title;
    private String content;
    private LocalDateTime reportedAt;
}
