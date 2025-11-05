package com.example.spring_security.services.admin;

import com.example.spring_security.entities.User;

import java.util.List;

public interface ManagementUserService {
    List<User> getUserDetailList();
}
