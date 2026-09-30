package com.example.Quiz.controller;

import com.example.Quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/users")
public class PublicUserController {

    private final UserService userService;

    @GetMapping("/username/exists")
    public ResponseEntity<Boolean> usernameExists(@RequestParam String username) {
        return ResponseEntity.ok(userService.usernameExists(username));
    }

    @GetMapping("/email/exists")
    public ResponseEntity<Boolean> emailExists(@RequestParam String email) {
        return ResponseEntity.ok(userService.emailExists(email));
    }
}
