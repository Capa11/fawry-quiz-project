package com.example.Quiz.service;

import com.example.Quiz.model.User;
import com.example.Quiz.model.UserEmailPassword;
import com.example.Quiz.payload.AdminUserDTO;
import com.example.Quiz.payload.UserDTO;
import com.example.Quiz.repository.UserEmailPasswordRepository;
import com.example.Quiz.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEmailPasswordRepository userEmailPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserEmailPasswordRepository userEmailPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userEmailPasswordRepository = userEmailPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void createUser(AdminUserDTO adminUserDTO) throws DataIntegrityViolationException {
        User user = new User(adminUserDTO.getFullName(),adminUserDTO.getUsername(),adminUserDTO.getPhoneNumber(),adminUserDTO.getProfileImageUrl(),adminUserDTO.getBalance(),adminUserDTO.getIsEnabled(),adminUserDTO.getRole());
        User savedUser = userRepository.save(user);
        UserEmailPassword userEmailPassword = new UserEmailPassword(savedUser, adminUserDTO.getEmail(), passwordEncoder.encode(adminUserDTO.getPassword()));
        userEmailPasswordRepository.save(userEmailPassword);
    }

    @Transactional
    public void updateUser(Long id, AdminUserDTO adminUserDTO) throws DataIntegrityViolationException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFullName(adminUserDTO.getFullName());
        user.setUsername(adminUserDTO.getUsername());
        user.setPhoneNumber(adminUserDTO.getPhoneNumber());
        user.setProfileImageUrl(adminUserDTO.getProfileImageUrl());
        user.setBalance(adminUserDTO.getBalance());
        user.setIsEnabled(adminUserDTO.getIsEnabled());
        user.setRole(adminUserDTO.getRole());

        userRepository.save(user);

        UserEmailPassword userEmailPassword = userEmailPasswordRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't have email and password"));

        userEmailPassword.setEmail(adminUserDTO.getEmail());
        userEmailPassword.setPasswordHash(passwordEncoder.encode(adminUserDTO.getPassword()));

        userEmailPasswordRepository.save(userEmailPassword);
    }

    @Transactional
    public void updateCurrentUser(User user, UserDTO userDTO) throws DataIntegrityViolationException {
        user.setFullName(userDTO.getFullName());
        user.setUsername(userDTO.getUsername());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setProfileImageUrl(userDTO.getProfileImageUrl());

        userRepository.save(user);
    }


    @Transactional
    public void updateCurrentUserPassword(String email, String Password) throws DataIntegrityViolationException {
        UserEmailPassword userEmailPassword = userEmailPasswordRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userEmailPassword.setPasswordHash(passwordEncoder.encode(Password));
        userEmailPasswordRepository.save(userEmailPassword);
    }




    @Transactional
    public void deleteUser(Long id) throws EmptyResultDataAccessException {
        userRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public Boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public Boolean emailExists(String email) {
        return userEmailPasswordRepository.existsByEmail(email);
    }
}
