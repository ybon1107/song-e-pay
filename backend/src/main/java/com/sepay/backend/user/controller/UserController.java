package com.sepay.backend.user.controller;


import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(String userId, String password) {
        String message = "success";
        UserDTO user = userService.login(userId, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed: Invalid ID or password.");
        }
        return ResponseEntity.ok(user);
    }
}
