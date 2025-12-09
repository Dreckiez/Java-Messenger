package com.example.spring_security.repository;

import com.example.spring_security.entities.RecordOnlineUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RecordOnlineUserRepository extends JpaRepository<RecordOnlineUser, String> {

    Optional<RecordOnlineUser> findById(String sessionId);

}
