package com.example.Quiz.controller;

import com.example.Quiz.model.User;
import com.example.Quiz.model.UserEmailPassword;
import com.example.Quiz.repository.UserEmailPasswordRepository;
import com.example.Quiz.repository.UserRepository;
import com.example.Quiz.security.AuthUser;
import com.example.Quiz.security.jwt.JwtUtils;
import com.example.Quiz.security.request.SignInRequest;
import com.example.Quiz.security.request.SignUpRequest;
import com.example.Quiz.security.response.MessageResponse;
import com.example.Quiz.security.response.SignInResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserEmailPasswordRepository userEmailPasswordRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("public/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmailOrUsername(), signInRequest.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            if(e instanceof UsernameNotFoundException){
                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
            }
            else return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        //token handling
        String jwtToken = jwtUtils.generateTokenFromUsername(authUser);
        SignInResponse signInResponse = new SignInResponse(authUser,jwtToken);
        return new ResponseEntity<>(signInResponse, HttpStatus.OK);
    }
    @PostMapping("public/signup")
    @Transactional
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userEmailPasswordRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        User user = new User(signUpRequest.getFullName(),signUpRequest.getUsername(), signUpRequest.getPhoneNumber(), signUpRequest.getProfileImageUrl());
        UserEmailPassword userEmailPassword = new UserEmailPassword(user, signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
        userEmailPasswordRepository.save(userEmailPassword);

        SignInRequest signInRequest = new SignInRequest(signUpRequest.getUsername(),signUpRequest.getPassword());
        return signIn(signInRequest);
    }
}
