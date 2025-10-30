package com.example.spring_security.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class VerifyToken extends BaseToken { private LocalDateTime expiryDate; }