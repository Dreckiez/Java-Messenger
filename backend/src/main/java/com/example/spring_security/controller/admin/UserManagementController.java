package com.example.spring_security.controller.admin;


import com.example.spring_security.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat/admin")
@RequiredArgsConstructor
public class UserManagementController {
    @GetMapping("")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hi admin!");
    }
    @GetMapping("/get-user")
    public ResponseEntity<List<User>> getUserDetailList
            (@RequestParam(value = "", ),
             @RequestParam(value = "isActive"), required = false,
             @RequestParam(value = "isAccepted"), required = false,
             @RequestParam(value = "sort", required = false) {
        return ResponseEntity.ok()
    }
}
