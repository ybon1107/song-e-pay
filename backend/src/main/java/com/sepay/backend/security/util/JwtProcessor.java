package com.sepay.backend.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtProcessor {
    static private final long TOKEN_VALID_MILISECOND = 1000L * 60 * 30; // 토큰 유효 시간 30분

    // 테스트용 토큰(30초)
//    static private final long TOKEN_VALID_MILISECOND = 1000L * 30; // 토큰 유효 시간 30초

//    private String secretKey = "Enough long random secret key string assignment "; // 충분히 긴 임의의(랜덤한) 비밀키 문자열 배정
//    private Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
       private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HS256 알고리즘을 사용하는 키 생성

    // JWT 생성
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + TOKEN_VALID_MILISECOND))
                .signWith(key)
                .compact();
    }

    // JWT Subject(userId) 추출 - 해석 불가인 경우 예외 발생
    // 예외 ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException,
    //      IllegalArgumentException
    public String getUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            System.out.println("Token is expired");
            throw new IllegalArgumentException("Token is expired", e);
        } catch (JwtException e) {
            System.out.println("Token is invalid");
            throw new IllegalArgumentException("Token is invalid", e);
        } catch (IllegalArgumentException e) {
            System.out.println("Token is empty");
            throw new IllegalArgumentException("Token is empty", e);
        }
    }

    // JWT 검증(유효 기간 검증) - 해석 불가인 경우 예외 발생
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date()); // 토큰 만료 여부 확인
        } catch (ExpiredJwtException e) {
            log.warn("Token is expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("Token is invalid: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("Token is empty or malformed: {}", e.getMessage());
            return false;
        }
    }
}
