package com.example.spring_security.repository;


import com.example.spring_security.entities.RecordSignIn;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RecordSignInRepository extends JpaRepository<RecordSignIn, Long> {

    @Query("""
    SELECT r FROM RecordSignIn r 
    WHERE (:isSuccessful IS NULL OR r.isSuccessful = :isSuccessful)
      AND (:userId IS NULL OR r.user.userId = :userId)
        """)
    List<RecordSignIn> findAll(@Param("isSuccessful") Boolean isSuccessful,
                               @Param("userId") Long userId,
                               Sort sort);
}
