package com.example.Quiz.security.request;

import lombok.*;

@Getter @Setter
public class SignInRequest {
    private String emailOrUsername;
    private String password;

    public SignInRequest(String username, String password) {
        this.emailOrUsername = username;
        this.password = password;
    }
}
