package com.sepay.backend.payment.controller;

import com.google.zxing.WriterException;
import com.sepay.backend.payment.service.QrService;
import com.sepay.backend.payment.service.QrServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class QRController {
    private final QrService qrService;

    @Autowired
    public QRController(QrServiceImpl qrService) {
        this.qrService = qrService;
    }

    @PostMapping("/check-password")
    public void checkPassword(@RequestBody String password){
        System.out.println("password:"+password);
        return;
    }

    @GetMapping("/qr")
    public void createQR(@RequestParam String url, HttpServletResponse response) throws WriterException, IOException, IOException {
        // 현재 시간 기반으로 QR 코드 내용 생성
        String dynamicUrl = url + "?time=" + System.currentTimeMillis();

        // QR 코드 이미지 생성
        byte[] qrBytes = qrService.createQR(dynamicUrl);

        // 응답의 content type을 이미지(PNG)로 설정
        response.setContentType("image/png");

        // byte[] 데이터를 응답의 OutputStream으로 출력
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(qrBytes);
        outputStream.close();
    }

    @GetMapping("/qr-scan")
    @ResponseBody
    public void handleQRScan(){
        //결제 로직 구현
        System.out.println("입력");
        return;
    }
}
