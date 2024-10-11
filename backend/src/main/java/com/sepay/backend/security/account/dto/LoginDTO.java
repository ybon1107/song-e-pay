package com.sepay.backend.security.account.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private String username;
    private String password;

    public static LoginDTO of(HttpServletRequest request) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(request.getInputStream(), LoginDTO.class);
        }catch (Exception e) {
            e.printStackTrace();
<<<<<<< HEAD
            throw new BadCredentialsException("userId 또는 password가 없습니다.");
=======
            throw new BadCredentialsException("username 또는 password가 없습니다.");
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
        }
    }
}
