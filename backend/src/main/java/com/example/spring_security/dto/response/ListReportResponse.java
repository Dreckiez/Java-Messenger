package com.example.spring_security.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class ListReportResponse {
    List<ReportResponse> reportResponseList;
    int count;
}
