package com.sepay.backend.security.handler;

import com.sepay.backend.security.util.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler { // 인증 OK, 사용자가 해당 리소스에 대한 권한이 없을 때 호출

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("========== Access Denied ============");
        JsonResponse.sendError(response, HttpStatus.FORBIDDEN, "Access Denied");
    }
}
