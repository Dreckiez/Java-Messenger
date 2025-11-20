package com.example.spring_security.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @EmbeddedId
    private ReportId id;

    @ManyToOne
    @JoinColumn(name = "reporter_id", insertable = false, updatable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id", insertable = false, updatable = false)
    private User reportedUser;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

}
