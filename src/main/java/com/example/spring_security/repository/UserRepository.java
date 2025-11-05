package com.example.spring_security.repository;

import com.example.spring_security.dto.response.BaseUserResponse;
import com.example.spring_security.entities.Enum.Role;
import com.example.spring_security.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findByRole(Role role);

    Optional<User> findById(Long id);

    List<User> findAll();

    boolean existsByUsername(@NotBlank(message = "Username must not be blank") @Pattern(regexp = "^[a-zA-Z0-9]{6,30}$", message = "Username must be 6-30 characters long and contain only letters and digits") String username);

    boolean existsByEmail(@NotBlank(message = "Email must not be blank") @Email(message = "Email must be a valid email address") String email);

    Optional<User> findByEmail(String email);

    @Query(value = """
            SELECT * 
            FROM user_info u
            WHERE u.user_id <> :currentUserId
            AND u.is_active = TRUE AND u.is_accepted = TRUE
            AND (u.username ILIKE CONCAT('%', :keyword, '%')
            OR 
            CONCAT(u.last_name, ' ', u.first_name) ILIKE CONCAT('%', :keyword, '%'))
            AND NOT EXISTS
                (
                SELECT 1 FROM Block B
                WHERE ((B.blocker_id = :currentUserId AND B.blocked_user_id = u.user_id)
                OR (B.blocker_id = u.user_id AND B.blocked_user_id = :currentUserId))
                AND B.is_active = TRUE
                )
            """, nativeQuery = true)
    List<User> searchUserByUsernameOrFullName(@Param("currentUserId") Long currentUserId,
                                              @Param("keyword") String keyword);
}
