package com.sepay.backend.security.filter;

import com.sepay.backend.security.account.dto.LoginDTO;
import com.sepay.backend.security.handler.LoginFailureHandler;
import com.sepay.backend.security.handler.LoginSuccessHandler;
import com.sepay.backend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // 스프링 생성자 주입을 통해 전달
    public JwtUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager,
            LoginSuccessHandler loginSuccessHandler,
            LoginFailureHandler loginFailureHandler,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        super(authenticationManager);

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;

        //setFilterProcessesUrl("/api/users/login");		          // POST 로그인 요청 url
        setFilterProcessesUrl("/api/auth/login");		          // POST 로그인 요청 url
        setAuthenticationSuccessHandler(loginSuccessHandler);	// 로그인 성공 핸들러 등록
        setAuthenticationFailureHandler(loginFailureHandler);  // 로그인 실패 핸들러 등록
    }

    // 로그인 요청 URL인 경우 로그인 작업 처리
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        // 요청 BODY의 JSON에서 userId, password  LoginDTO
        LoginDTO login = LoginDTO.of(request);
        String password = login.getPassword(); // 입력받은 비밀번호
        UserDetails userDB = userDetailsService.loadUserByUsername(login.getUsername()); // DB에서 가져온 사용자 정보

        log.info("입력받은 비밀번호: " + password);
        log.info("DB에 저장된 암호화된 비밀번호: " + userDB.getPassword());
        log.info("비밀번호 일치 여부: " + passwordEncoder.matches(password, userDB.getPassword()));

        // 인증 토큰(UsernamePasswordAuthenticationToken) 구성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());

        // AuthenticationManager에게 인증 요청
        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
