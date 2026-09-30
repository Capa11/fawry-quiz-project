package com.example.Quiz.security.request;

import lombok.*;

@Getter @Setter
public class SignUpRequest {
    private String fullName;
    private String username;
    private String phoneNumber;
    private String profileImageUrl;
    private String email;
    private String password;


}
