package com.sepay.backend.payment.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.mapper.MyAccountMapper;
import com.sepay.backend.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
//    private final PaymentMapper mapper;
    private final MyAccountMapper mapper;

    @Override
    public byte[] createQR(String url) throws WriterException, IOException {
        // QR 코드 크기 설정
        int width = 200;
        int height = 200;

        // QR Code - BitMatrix: QR 코드 정보 생성
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

        // QR 코드 생성 및 바이트 배열로 변환
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
            return out.toByteArray();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payment(Double amount, UserDTO user) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (user == null || user.getKrwNo() == null) {
            throw new IllegalArgumentException("Invalid user information");
        }

        String krwNo = user.getKrwNo();
        double krwBalance = mapper.selectKrwBalance(krwNo);

        if (krwBalance < amount) {
            throw new Error("Insufficient KRW balance");
        }

        try {
            // 원화 금액 감소
            KrwAccountDTO krwDTO = KrwAccountDTO.builder()
                    .krwNo(krwNo)
                    .balance(krwBalance - amount)
                    .build();
            int resKrw = mapper.updateKrwAccount(krwDTO);

            if (resKrw <= 0) {
                throw new Error("Failed to update KRW account");
            }

            // 내역 추가
            HistoryDTO historyDTO = HistoryDTO.builder()
                    .userNo(user.getUserNo())
                    .songNo(user.getSongNo())
                    .krwNo(user.getKrwNo())
                    .typeCode(1)
                    .stateCode(1)
                    .historyContent("USD → KRW 환전")
                    .amount(amount)
                    .build();
            int resHistory = mapper.insertHistory(historyDTO);

            if (resHistory <= 0) {
                throw new Error("Failed to insert payment history");
            }
        } catch (Exception e) {
//            logger.error("Payment processing error: ", e);
            throw new Error("An error occurred during payment processing", e);
        }
    }


}
