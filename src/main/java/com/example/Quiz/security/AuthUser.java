package com.example.Quiz.security;

import com.example.Quiz.excepation.NotApplicableForOAuth2Exception;
import com.example.Quiz.model.User;
import com.example.Quiz.model.UserEmailPassword;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AuthUser implements UserDetails, OAuth2User {

    @Getter
    private final User user;
    private final Map<String, Object> attributes;
    private UserEmailPassword userEmailPassword;


    public AuthUser(User user, UserEmailPassword userEmailPassword) {
        this.user = user;
        this.attributes = Map.of();
        this.userEmailPassword = userEmailPassword;
    }

    public AuthUser(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }
    public Long getId(){
        return user.getId();
    }

    // ===== UserDetails =====
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority( user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return userEmailPassword.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return user.getIsEnabled(); }

    // ===== OAuth2User =====
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        // Fallback: full name from DB
        return attributes.getOrDefault("name", user.getFullName()).toString();
    }

}
