package com.example.Quiz.payload;

import com.example.Quiz.model.User;
import com.example.Quiz.model.UserEmailPassword;
import com.example.Quiz.model.enums.Roles;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
@NotNull

public class AdminUserDTO {
    private String fullName;
    private String username;
    private String phoneNumber;
    private String profileImageUrl;
    private String email;
    private String password;
    private Double balance;
    private Boolean isEnabled;
    private Roles role;

    public AdminUserDTO(String fullName, String username, String phoneNumber, String profileImageUrl, String email, String password, Double balance, boolean isEnabled, Roles role) {
        this.fullName = fullName;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.isEnabled = isEnabled;
        this.role = role;
    }
    public AdminUserDTO(Long id,String fullName, String username, String phoneNumber, String profileImageUrl, String email, String password, Double balance, boolean isEnabled, Roles role) {
        this.fullName = fullName;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.isEnabled = isEnabled;
        this.role = role;
    }

}



