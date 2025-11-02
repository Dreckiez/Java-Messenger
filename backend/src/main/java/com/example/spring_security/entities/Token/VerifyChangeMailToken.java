package com.example.spring_security.entities.Token;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "verify_change_mail_token")
public class VerifyChangeMailToken {
    private LocalDateTime expiryDate;
    private String newEmail;
}
