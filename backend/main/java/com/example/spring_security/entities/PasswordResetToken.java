package com.example.spring_security.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class PasswordResetToken extends BaseToken {}
