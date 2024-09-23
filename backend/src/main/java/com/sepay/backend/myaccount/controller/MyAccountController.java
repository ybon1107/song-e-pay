package com.sepay.backend.myaccount.controller;

import com.sepay.backend.myaccount.dto.AccountDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
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
    //송이 계좌 조회
    @PostMapping("/krwbalance")
    public ResponseEntity<?> getKrwAccountBalance(@RequestParam String krwNo) {
        return ResponseEntity.ok(myAccountService.getKrwAccountByUserNo(krwNo));
    }
    //원화 계좌 조회
    @PostMapping("/songebalance")
    public ResponseEntity<?> getSongAccountBalance(@RequestParam String songNo) {
        return ResponseEntity.ok(myAccountService.getSongAccountByUserNo(songNo));
    }
    //송이 계좌 충전
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam double amount, AccountDTO accountDTO, SongAccountDTO songAccountDTO) {
        return ResponseEntity.ok(myAccountService.deposit(accountDTO, songAccountDTO, amount));
    }

    // 환불
    @PostMapping("/refund")
    public ResponseEntity<?> refund(@RequestParam double amount, AccountDTO accountDTO, SongAccountDTO songAccountDTO) {
        return ResponseEntity.ok(myAccountService.refund(accountDTO, songAccountDTO, amount));
    }

    // 환전
    @PostMapping("/exchange")
    public ResponseEntity<?> exchange(@RequestParam double amount, double exchangeRate, SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO) {
        return ResponseEntity.ok(myAccountService.exchange(songAccountDTO, krwAccountDTO , amount, exchangeRate));
    }

    // 환급
    @PostMapping("/re-exchange")
    public ResponseEntity<?> reExchange(@RequestParam double amount, double exchangeRate, SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO) {
        return ResponseEntity.ok(myAccountService.reExchange(songAccountDTO, krwAccountDTO , amount, exchangeRate));
    }

    // 송금
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam double amount, KrwAccountDTO myKrwAccount, String target_krwNo) {
        return ResponseEntity.ok(myAccountService.transfer(myKrwAccount, amount, target_krwNo));
    }
}
