package com.example.spring_security;

import com.example.spring_security.entities.Enum.Gender;
import com.example.spring_security.entities.Enum.Role;
import com.example.spring_security.entities.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.services.third.InitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    private InitializationService initializationService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initializationService.initAdmin();
    }
}