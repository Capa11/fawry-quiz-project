package com.example.Quiz.repository;

import com.example.Quiz.model.User;
import com.example.Quiz.model.UserEmailPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEmailPasswordRepository extends JpaRepository<UserEmailPassword, Long> {
    Optional<UserEmailPassword> findByEmail(String email);


    boolean existsByEmail(String email);

    Optional<UserEmailPassword> findByUser(User user);

}