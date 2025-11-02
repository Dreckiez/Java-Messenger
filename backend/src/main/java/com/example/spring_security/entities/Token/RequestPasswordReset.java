package com.example.spring_security.entities.Token;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "request_password_reset")
public class RequestPasswordReset extends BaseToken {}
