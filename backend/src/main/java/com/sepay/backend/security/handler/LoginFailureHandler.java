package com.sepay.backend.security.handler;

<<<<<<< HEAD
import com.sepay.backend.security.exception.WithdrawnUserException;
=======
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
import com.sepay.backend.security.util.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
<<<<<<< HEAD
import org.springframework.security.core.userdetails.UsernameNotFoundException;
=======
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
<<<<<<< HEAD
public class LoginFailureHandler implements AuthenticationFailureHandler {
=======
public class LoginFailureHandler implements AuthenticationFailureHandler { // 로그인 실패시 호출
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
<<<<<<< HEAD
        String errorMessage;
        
        if (exception instanceof WithdrawnUserException) {
            errorMessage = exception.getMessage();
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = exception.getMessage();
        } else {
            errorMessage = "사용자 ID 또는 비밀번호가 일치하지 않습니다.";
        }
        JsonResponse.sendError(response, HttpStatus.UNAUTHORIZED, errorMessage);
=======
        JsonResponse.sendError(response, HttpStatus.UNAUTHORIZED, "사용자 ID 또는 비밀번호가 일치하지 않습니다.");

>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
    }
}
