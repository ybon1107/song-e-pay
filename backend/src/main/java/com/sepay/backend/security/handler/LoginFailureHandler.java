package com.sepay.backend.security.handler;

import com.sepay.backend.security.exception.WithdrawnUserException;
import com.sepay.backend.security.util.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        
        if (exception instanceof WithdrawnUserException) {
            errorMessage = exception.getMessage();
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = exception.getMessage();
        } else {
            errorMessage = "사용자 ID 또는 비밀번호가 일치하지 않습니다.";
        }
        JsonResponse.sendError(response, HttpStatus.UNAUTHORIZED, errorMessage);
    }
}
