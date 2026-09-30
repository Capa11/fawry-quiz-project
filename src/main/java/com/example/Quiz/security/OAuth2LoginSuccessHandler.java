package com.example.Quiz.security;

import com.example.Quiz.model.User;
import com.example.Quiz.model.UserAuthProvider;
import com.example.Quiz.model.enums.Providers;
import com.example.Quiz.model.enums.Roles;
import com.example.Quiz.repository.UserAuthProviderRepository;
import com.example.Quiz.repository.UserRepository;
import com.example.Quiz.security.jwt.JwtUtils;
import com.example.Quiz.security.response.SignInResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    private final ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(authUser);

        SignInResponse signInResponse = new SignInResponse(authUser, jwtToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), signInResponse);

    }



}