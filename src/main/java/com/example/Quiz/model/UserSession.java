package com.example.Quiz.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name="user_sessions",indexes = {
@Index(name = "idx_user_sessions_user_id", columnList = "user_id")
})
@Getter
@NoArgsConstructor
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(name = "session_token",nullable = false)
    private String sessionToken;

    @Column(name="expires_at",nullable = false)
    private LocalDateTime expiresAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
