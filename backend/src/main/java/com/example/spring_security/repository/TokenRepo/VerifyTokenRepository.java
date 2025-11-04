package com.example.spring_security.repository;

import com.example.spring_security.entities.Token.VerifyToken;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyTokenRepository extends BaseTokenRepository<VerifyToken> {}
