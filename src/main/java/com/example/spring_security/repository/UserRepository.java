package com.example.spring_security.repository;

import com.example.spring_security.entities.Enum.Role;
import com.example.spring_security.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findByRole(Role role);

    boolean existsByUsername(@NotBlank(message = "Username must not be blank") @Pattern(regexp = "^[a-zA-Z0-9]{6,30}$", message = "Username must be 6-30 characters long and contain only letters and digits") String username);

    boolean existsByEmail(@NotBlank(message = "Email must not be blank") @Email(message = "Email must be a valid email address") String email);

    Optional<User> findByEmail(String email);
}
