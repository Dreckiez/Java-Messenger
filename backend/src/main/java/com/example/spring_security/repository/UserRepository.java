package com.example.spring_security.repository;

import com.example.spring_security.dto.response.BaseUserResponse;
import com.example.spring_security.dto.response.UserSearchResponse;
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

    Optional<User> findByRole(Role role);

    Optional<User> findById(Long id);

    List<User> findAll();

    boolean existsByUsername(@NotBlank(message = "Username must not be blank") @Pattern(regexp = "^[a-zA-Z0-9]{6,30}$", message = "Username must be 6-30 characters long and contain only letters and digits") String username);

    boolean existsByEmail(@NotBlank(message = "Email must not be blank") @Email(message = "Email must be a valid email address") String email);

    Optional<User> findByEmail(String email);

    @Query(value = """
            SELECT 
                u.user_id AS userId,
                u.username AS username,
                u.first_name AS firstName,
                u.last_name AS lastName,
                u.avatar_url AS avatarUrl,
                CASE
                    WHEN f.user_id1 IS NOT NULL THEN 'friend'
                    WHEN fr.sender_id = :currentUserId THEN 'sent'
                    WHEN fr.receiver_id = :currentUserId AND fr.status = 0 THEN 'received'
                    ELSE 'none'
                END AS status
            FROM user_info u
            
            LEFT JOIN friend f ON (
                                    f.user_id1 = :currentUserId AND f.user_id2 = u.user_id
                                    OR 
                                    f.user_id2 = :currentUserId AND f.user_id1 = u.user_id
                                  )
                                  
                                  
            LEFT JOIN friend_request fr ON (    fr.is_active = true
                                                AND
                                                (fr.sender_id = :currentUserId AND fr.receiver_id = u.user_id
                                                OR 
                                                fr.receiver_id = :currentUserId AND fr.sender_id = u.user_id)
                                           )
            
            WHERE u.is_active = TRUE AND u.is_accepted = TRUE
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
            ORDER BY 
                     (u.user_id = :currentUserId) DESC
            """, nativeQuery = true)
    List<UserSearchResponse> searchUserByUsernameOrFullName(@Param("currentUserId") Long currentUserId,
                                                            @Param("keyword") String keyword);



    @Query(value = """
    SELECT * 
    FROM user_info u
    WHERE
        ( :keyword IS NULL 
          OR :keyword = ''
          OR CONCAT(u.last_name, ' ', u.first_name) ILIKE CONCAT('%', :keyword, '%')
          OR u.username ILIKE CONCAT('%', :keyword, '%')
          OR u.email ILIKE CONCAT('%', :keyword, '%')
        )
        AND ( :isActive IS NULL OR u.is_active = :isActive )
        AND ( :isAccepted IS NULL OR u.is_accepted = :isAccepted )
        AND ( :greaterThan IS NULL OR u.friend_count > :greaterThan)
        AND ( :smallerThan IS NULL OR u.friend_count < :smallerThan)
        AND ( :days IS NULL OR u.joined_at  >= NOW() - (:days * INTERVAL '1 day'))
    ORDER BY
        CASE 
            WHEN :sort = 'email' THEN u.email
        END ASC,
        CASE 
            WHEN :sort = '-email' THEN u.email
        END DESC,
        CASE 
            WHEN :sort = 'fullName' THEN LOWER(CONCAT(u.last_name, ' ', u.first_name))
        END ASC,
        CASE 
            WHEN :sort = '-fullName' THEN LOWER(CONCAT(u.last_name, ' ', u.first_name))
        END DESC,
        CASE 
            WHEN :sort = 'joinedAt' THEN u.joined_at
        END ASC,
        CASE 
            WHEN :sort = '-joinedAt' THEN u.joined_at
        END DESC
    """, nativeQuery = true)
    List<User> managementUser(
            @Param("keyword") String keyword,
            @Param("isActive") Boolean isActive,
            @Param("isAccepted") Boolean isAccepted,
            @Param("greaterThan") Integer greaterThan,
            @Param("smallerThan") Integer smallerThan,
            @Param("sort") String sort,
            @Param("days") Integer days);

    void deleteById(Long id);
}
