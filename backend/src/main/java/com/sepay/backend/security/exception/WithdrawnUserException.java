package com.sepay.backend.security.exception;

import org.springframework.security.core.AuthenticationException;

public class WithdrawnUserException extends AuthenticationException {
    public WithdrawnUserException(String msg) {
        super(msg);
    }
}