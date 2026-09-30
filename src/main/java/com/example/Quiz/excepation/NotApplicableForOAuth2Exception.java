package com.example.Quiz.excepation;

import org.springframework.security.core.AuthenticationException;

public class NotApplicableForOAuth2Exception extends AuthenticationException {
    public NotApplicableForOAuth2Exception(String message) {
        super(message);
    }
}
