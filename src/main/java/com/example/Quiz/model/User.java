package com.example.Quiz.model;

import com.example.Quiz.model.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_username", columnList = "user_name")
        }
)@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    @Size(min = 1, max = 100)
    private String fullName;

    @Column(name= "user_name", nullable = false, unique = true)
    @Size(min = 1, max = 100)
    private String username;

    @Column(name = "phone_number", unique = true)
    @Size(min = 1, max = 15)
    private String phoneNumber;

    @Column(name = "profile_image_url",columnDefinition = "TEXT")
    private String profileImageUrl;

    private Double balance = 0.0;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role = Roles.ROLE_USER;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private UserEmailPassword userEmailPassword;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    public User(String fullName, String username, String phoneNumber, String profileImageUrl) {
        this.fullName = fullName;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
    }
    public User(String fullName, String username, String phoneNumber, String profileImageUrl, Double balance, boolean isEnabled, Roles role) {
        this.fullName = fullName;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.balance = balance;
        this.isEnabled = isEnabled;
        this.role = role;
    }
    public User(String fullName, String username){
        this.fullName = fullName;
        this.username = username;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }
    public void withdraw(double amount) {
        this.balance -= amount;
    }


}
