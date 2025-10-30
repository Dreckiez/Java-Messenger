package com.example.spring_security.repository;

import com.example.spring_security.entities.User;
import com.example.spring_security.entities.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyTokenRepository extends BaseTokenRepository<VerifyToken> {}
