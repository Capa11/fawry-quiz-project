package com.example.Quiz.payload;

import com.example.Quiz.model.User;
import com.example.Quiz.security.AuthUser;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
@NotNull
public class UserDTO {
    private String fullName;
    private String username;
    private String phoneNumber;
    private String profileImageUrl;

    public UserDTO(String fullName, String username, String phoneNumber, String profileImageUrl) {
        this.fullName = fullName;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
    }


}
