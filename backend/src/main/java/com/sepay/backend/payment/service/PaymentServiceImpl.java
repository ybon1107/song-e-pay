package com.sepay.backend.payment.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.history.service.HistoryService;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.mapper.MyAccountMapper;
import com.sepay.backend.myaccount.service.MyAccountService;
import com.sepay.backend.payment.dto.PaymentDTO;
import com.sepay.backend.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
//    private final PaymentMapper mapper;
    private final MyAccountMapper mapper;
    private final HistoryService historyService;

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
    public void payment(PaymentDTO dto) {
        if (dto.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (dto.getUserId() == null || dto.getKrwNo() == null) {
            throw new IllegalArgumentException("Invalid user information");
        }

        double krwBalance = mapper.selectKrwBalance(dto.getKrwNo());

        if (krwBalance < dto.getAmount()) {
            throw new Error("Insufficient KRW balance");
        }

        try {
            // 원화 금액 감소
            KrwAccountDTO krwDTO = KrwAccountDTO.builder()
                    .krwNo(dto.getKrwNo())
                    .balance(krwBalance - dto.getAmount())
                    .updatedAt(new Date())
                    .build();
            int resKrw = mapper.updateKrwAccount(krwDTO);

            if (resKrw <= 0) {
                throw new Error("Failed to update KRW account");
            }

            // 내역 추가
            HistoryDTO historyDTO = HistoryDTO.builder()
                    .userId(dto.getUserId())
                    .songNo(dto.getSongNo())
                    .krwNo(dto.getKrwNo())
                    .typeCode(1)
                    .stateCode(1)
                    .historyContent(dto.getHistoryContent())
                    .amount(dto.getAmount())
                    .historyDate(new Date())
                    .build();
            int resHistory = historyService.saveHistory(historyDTO);

            if (resHistory <= 0) {
                throw new Error("Failed to insert payment history");
            }
        } catch (Exception e) {
//            logger.error("Payment processing error: ", e);
            throw new Error("An error occurred during payment processing", e);
        }
    }


}
