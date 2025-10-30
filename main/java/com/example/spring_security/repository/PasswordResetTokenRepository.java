package com.example.spring_security.repository;

import com.example.spring_security.entities.PasswordResetToken;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends BaseTokenRepository<PasswordResetToken> {}