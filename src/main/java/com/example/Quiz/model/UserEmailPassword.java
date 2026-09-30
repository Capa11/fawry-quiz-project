package com.example.Quiz.model;

import jakarta.persistence.*;
        import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@Table(name = "user_email_password")
@Entity
@NoArgsConstructor
public class UserEmailPassword{
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "email",nullable = false,unique = true)
    @Email
    @Size(min = 1, max = 320)
    private String email;

    @Column(name = "password_hash",nullable = false, columnDefinition = "text")
    private String passwordHash;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserEmailPassword(User user, String email, String passwordHash) {
        this.user = user;
        this.email = email;
        this.passwordHash = passwordHash;
    }

}