package com.example.spring_security.services.admin.impl;


import com.example.spring_security.entities.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.services.admin.ManagementUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementUserServiceImpl implements ManagementUserService {
    private final UserRepository userRepository;

    public List<User> getUserDetailList() {
        return
    }

}
