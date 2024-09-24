package com.sepay.backend.myaccount.controller;

import com.sepay.backend.myaccount.dto.AccountDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;
import com.sepay.backend.myaccount.service.MyAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-accounts")
public class MyAccountController {

    final MyAccountService myAccountService;

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
