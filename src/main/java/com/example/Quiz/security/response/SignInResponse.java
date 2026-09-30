package com.example.Quiz.security.response;

import com.example.Quiz.security.AuthUser;
import lombok.AllArgsConstructor;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@AllArgsConstructor
public class SignInResponse {
    String username;
    List<String> roles;
    String token;
    public SignInResponse(AuthUser authUser, String token) {
        username = authUser.getUsername();
        roles = authUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        this.token=token;
    }

}
