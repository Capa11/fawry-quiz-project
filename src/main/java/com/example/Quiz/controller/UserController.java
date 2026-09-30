package com.example.Quiz.controller;

import com.example.Quiz.model.User;
import com.example.Quiz.payload.AdminUserDTO;
import com.example.Quiz.payload.UserDTO;
import com.example.Quiz.security.AuthUser;
import com.example.Quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getAllUsers(@ParameterObject Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(authUser.getUser());
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody AdminUserDTO adminUserDTO) {
        try {
            userService.createUser(adminUserDTO);
            return ResponseEntity.ok("User created successfully");
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage().toLowerCase();
            if (message.contains("username")) {
                return ResponseEntity.badRequest().body("Username already taken");
            }
            if (message.contains("phone_number")) {
                return ResponseEntity.badRequest().body("Phone number already taken");
            }
            if (message.contains("email")) {
                return ResponseEntity.badRequest().body("Email already taken");
            }
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody AdminUserDTO adminUserDTO) {
        userService.updateUser(id,adminUserDTO);
        return ResponseEntity.ok("User updated successfully");
    }
    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserDTO userDTO) {
        userService.updateCurrentUser(authUser.getUser(),userDTO);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
