package com.sepay.backend.payment.controller;

import com.google.zxing.WriterException;
import com.sepay.backend.payment.service.QrServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class QRController {
    private QrServiceImpl qrServiceImpl;

    @Autowired
    public QRController(QrServiceImpl qrServiceImpl) {
        this.qrServiceImpl = qrServiceImpl;
    }

    @GetMapping("/qr")
    public void createQR(@RequestParam String url, HttpServletResponse response) throws WriterException, IOException, IOException {
        // QR 코드 이미지 생성
        byte[] qrBytes = qrServiceImpl.createQR(url);

        // 응답의 content type을 이미지(PNG)로 설정
        response.setContentType("image/png");

        // byte[] 데이터를 응답의 OutputStream으로 출력
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(qrBytes);
        outputStream.close();
    }
}
