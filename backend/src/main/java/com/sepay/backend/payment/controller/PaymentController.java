package com.sepay.backend.payment.controller;

import com.google.zxing.WriterException;
import com.sepay.backend.payment.dto.PasswordDTO;
import com.sepay.backend.payment.dto.PaymentDTO;
import com.sepay.backend.payment.service.PaymentService;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;

    @PostMapping("/check-password")
    public boolean checkSecondaryPassword(@RequestBody PasswordDTO passwordDTO){
        //2차 비밀번호 확인
        return userService.checkSecondaryPassword(passwordDTO);
    }

    @GetMapping("/qr")
    public void createQR(@RequestParam String url, HttpServletResponse response) throws WriterException, IOException, IOException {
        // 현재 시간 기반으로 QR 코드 내용 생성
        String dynamicUrl = url + "?time=" + System.currentTimeMillis();

        // QR 코드 이미지 생성
        byte[] qrBytes = paymentService.createQR(dynamicUrl);

        // 응답의 content type을 이미지(PNG)로 설정
        response.setContentType("image/png");

        // byte[] 데이터를 응답의 OutputStream으로 출력
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(qrBytes);
        outputStream.close();
    }

//    @PostMapping("/qr-scan")
//    public boolean handleQRScan(@RequestBody Double amount, UserDTO user){
//        return paymentService.payment(amount,user);
//    }

    @PostMapping("/qr-scan")
    public ResponseEntity<?> handleQRScan(@RequestBody PaymentDTO dto) {
        try {
            paymentService.payment(dto.getAmount(),dto.getUserDTO());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

//    public boolean handleQRScan(@RequestBody DTORequest req){
//        return paymentService.payment(req.getKrwAccountDTO(),req.getAmount());
////        결제 로직 구현
////        System.out.println("입력");
//    }
}
