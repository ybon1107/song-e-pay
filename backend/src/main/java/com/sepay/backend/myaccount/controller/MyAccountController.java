package com.sepay.backend.myaccount.controller;

import com.sepay.backend.exchange.dto.ExchangeDTO;
import com.sepay.backend.exchange.service.ExchangeService;
import com.sepay.backend.mail.service.MailService;
import com.sepay.backend.myaccount.dto.DTORequest;
import com.sepay.backend.myaccount.service.MyAccountService;
import com.sepay.backend.notification.dto.NotificationDTO;
import com.sepay.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-accounts")
public class MyAccountController {

    final MyAccountService myAccountService;
    final UserService userService;
    private final MailService mailService;
    private final ExchangeService exchangeService;
    private final int COUNTRY_CODE_KR = 0;
    //송이 계좌 잔액 조회
    @PostMapping("/krwbalance")
    public ResponseEntity<?> getKrwAccountBalance(@RequestParam String krwNo) {
        return ResponseEntity.ok(myAccountService.selectKrwBalance(krwNo));
    }
    //원화 계좌 잔액 조회
    @PostMapping("/songebalance")
    public ResponseEntity<?> getSongAccountBalance(@RequestParam String songNo) {
        return ResponseEntity.ok(myAccountService.selectSongBalance(songNo));
    }

    // 이메일 확인
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmAccount(@RequestParam String userId) {
        return ResponseEntity.ok(myAccountService.selectUserEmail(userId));
    }

    //2차 비밀번호 확인
//    @PostMapping("/check")
//    public ResponseEntity<?> checkAccount(@RequestParam String userId) {
//        return ResponseEntity.ok(userService.selectSecondPwd(userId));
//    }
//    @PostMapping("/check")
//    public ResponseEntity<?> checkAccount(@RequestParam String userId) {
//        return ResponseEntity.ok(userService.selectSecondPwd(userId));
//    }

    // 충전
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DTORequest request) {
        // DepositRequest 클래스를 사용하여 accountDTO, songAccountDTO 및 amount를 받습니다.
        return ResponseEntity.ok(myAccountService.deposit(request.getAccountDTO(), request.getSongAccountDTO(), request.getHistoryDTO(), request.getAmount()));
    }

    // 환불
    @PostMapping("/refund")
    public ResponseEntity<?> refund(@RequestBody DTORequest request) {
        return ResponseEntity.ok(myAccountService.refund(request.getAccountDTO(), request.getSongAccountDTO(), request.getHistoryDTO(), request.getAmount()));
    }

    // 환전
    @PostMapping("/exchange")
    public ResponseEntity<?> exchange(@RequestBody DTORequest request) {

        System.out.println("--------------------------------------- reqyest : " + request);
        List<ExchangeDTO> exchangeDTO = exchangeService.getExchange(COUNTRY_CODE_KR,request.getSongAccountDTO().getCountryCode());
        System.out.println(exchangeDTO);
        if(exchangeDTO.get(0).getExchangeRate().equals(request.getExchangeRate())){
            log.info(String.valueOf(request.getExchangeRate()));
            return ResponseEntity.ok(myAccountService.exchange(request.getSongAccountDTO(), request.getKrwAccountDTO(), request.getHistoryDTO() , request.getAmount(), request.getExchangeRate()));
        }
        return ResponseEntity.badRequest().body("The exchange rate has changed.");
    }

    // 환급
    @PostMapping("/re-exchange")
    public ResponseEntity<?> reExchange(@RequestBody DTORequest request) {
        List<ExchangeDTO> exchangeDTO = exchangeService.getExchange(request.getSongAccountDTO().getCountryCode(),COUNTRY_CODE_KR);
        if(exchangeDTO.get(0).getExchangeRate().equals(request.getExchangeRate())) {
            log.info(String.valueOf(request.getExchangeRate()));
            return ResponseEntity.ok(myAccountService.reExchange(request.getSongAccountDTO(), request.getKrwAccountDTO(), request.getHistoryDTO(), request.getAmount(), request.getExchangeRate()));
        }
        return ResponseEntity.unprocessableEntity().body("환율에 변동이 발생했습니다.");
    }

    // 송금
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody DTORequest request) throws Exception {
        log.info("확인 : {}" , request.getTargetHistoryContent());
        if("no-member".equals(request.getIsMember())){
            return ResponseEntity.ok(mailService.transferTo(request.getTarget_krwNo(), request.getHistoryDTO()));
        }
        else{
            return ResponseEntity.ok(myAccountService.transfer(request.getKrwAccountDTO(), request.getHistoryDTO(), request.getAmount(), request.getTarget_krwNo(), request.getTargetHistoryContent()));
        }

    }

    // 보내는 사람(회원)의 krw_no 값 가져오기
    @PostMapping("/krwno")
    public ResponseEntity<?> getKrwNo(@RequestParam String userId) {
        return ResponseEntity.ok(myAccountService.getKrwno(userId));
    }

    // 송금 받기
    @PostMapping("/receive-transfer")
    public ResponseEntity<?> receiveTransfer(@RequestBody NotificationDTO dto) {
        log.info("dto {}", dto);
        return ResponseEntity.ok(myAccountService.receiveSongE(dto));
    }

}