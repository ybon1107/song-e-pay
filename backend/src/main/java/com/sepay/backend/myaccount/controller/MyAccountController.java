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

}
