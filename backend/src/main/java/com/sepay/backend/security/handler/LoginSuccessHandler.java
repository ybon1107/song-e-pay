package com.sepay.backend.security.handler;

import com.sepay.backend.security.account.domain.CustomUser;
import com.sepay.backend.security.account.dto.AuthResultDTO;
import com.sepay.backend.security.account.dto.UserInfoDTO;
import com.sepay.backend.security.util.JsonResponse;
import com.sepay.backend.security.util.JwtProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler { // 로그인 성공시 호출

    private final JwtProcessor jwtProcessor;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증 결과 Principal
        CustomUser user = (CustomUser) authentication.getPrincipal();
        // CustomUser 객체에서 사용자 정보를 추출하여 토큰을 생성하고, 사용자 정보와 함께 응답

        // 인증 성공 결과를 JSON으로 직접 응답
        AuthResultDTO result = makeAuthResult(user);
        JsonResponse.send(response, result);
    }

    private AuthResultDTO makeAuthResult(CustomUser user) {
        String username = user.getUsername();
        // 토큰 생성
        String token = jwtProcessor.generateToken(username);
        // 토큰 + 사용자 기본 정보 (사용자명, ...)를 묶어서 AuthResultDTO 구성
        return new AuthResultDTO(token, UserInfoDTO.of(user.getUserVO()));
    }
}
