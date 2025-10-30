package com.example.spring_security.services;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
