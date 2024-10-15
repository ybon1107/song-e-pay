package com.sepay.backend.payment.controller;

import com.google.zxing.WriterException;
import com.sepay.backend.payment.dto.PaymentDTO;
import com.sepay.backend.payment.service.PaymentService;
import com.sepay.backend.reservation.dto.AccommodationPaymentDTO;
import com.sepay.backend.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/qr-scan")
    public ResponseEntity<?> handleQRScan(@RequestBody PaymentDTO dto) {
        try {
            paymentService.payment(dto);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 숙소 카카오페이 결제
    @PostMapping("/accommodation/kakao")
    public ResponseEntity<?> kakaoAccommodation(@RequestBody AccommodationPaymentDTO dto) {
        log.info("paymentDTO : {}", dto);
        return ResponseEntity.ok(paymentService.accommodationPaymentKakao(dto));
    }

    // 숙소 결제
    @PostMapping("/accommodation")
    public ResponseEntity<?> handleAccommodation(@RequestBody PaymentDTO dto) {
        log.info("paymentDTO : {}", dto);
        try {
            return ResponseEntity.ok(paymentService.accommodationPayment(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }
}
