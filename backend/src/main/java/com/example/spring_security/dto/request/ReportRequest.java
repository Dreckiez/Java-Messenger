package com.example.spring_security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    @NotBlank(message = "Title must not be blank.")
    @Size(max = 50, message = "Title must not exceed 50 characters.")
    private String title;
    @Size(max = 2000, message = "Content must not exceed 2000 characters.")
    private String content;
}
