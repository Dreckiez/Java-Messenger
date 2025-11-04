package com.example.spring_security.repository;

import com.example.spring_security.entities.Token.BaseToken;
import com.example.spring_security.entities.Token.VerifyEmailChangeToken;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyEmailChangeTokenRepository extends BaseTokenRepository<VerifyEmailChangeToken> {}
