package com.example.spring_security;

import com.example.spring_security.entities.Role;
import com.example.spring_security.entities.User;
import com.example.spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User adminAccount = userRepository.findByRole(Role.ADMIN);
        if (adminAccount == null) {
            User user = new User();
            user.setUsername("admin123");
            user.setEmail("admin123@gmail.com");
            user.setFirstName("User");
            user.setLastName("Super");
            user.setRole(Role.ADMIN);
            user.setHashPassword(new BCryptPasswordEncoder().encode("admin123"));
            user.setIsAccepted(true);
            user.setIsActive(true);
            userRepository.save(user);
        }
    }
}