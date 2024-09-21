package com.sepay.backend.myaccount.controller;

import com.sepay.backend.myaccount.dto.SongAccountDTO;
import com.sepay.backend.myaccount.service.MyAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-accounts")
public class MyAccountController {
    private final MyAccountService myAccountService;

    @PostMapping("/balance")
    public ResponseEntity<?> getAccountBalance(@RequestParam Integer userNo) {
        SongAccountDTO accountBalance = myAccountService.getSongAccountBalance(userNo);
        if (accountBalance != null) {
            return ResponseEntity.ok(accountBalance); // 200 OK와 함께 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 NOT FOUND
        }
    }
}
