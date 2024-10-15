package com.sepay.backend.payment.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sepay.backend.eventSchedule.dto.ScheduleDTO;
import com.sepay.backend.eventSchedule.service.ScheduleService;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.history.service.HistoryService;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.mapper.MyAccountMapper;
import com.sepay.backend.payment.dto.PaymentDTO;
import com.sepay.backend.payment.mapper.PaymentMapper;
import com.sepay.backend.reservation.dto.AccommodationPaymentDTO;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.service.UserService;
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
    private final PaymentMapper paymentMapper;
    private final MyAccountMapper mapper;
    private final HistoryService historyService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final MyAccountMapper myAccountMapper;

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
                    .userId(user.getUserId())
                    .songNo(user.getSongNo())
                    .krwNo(user.getKrwNo())
                    .typeCode(1)
                    .stateCode(1)
                    .historyContent("USD → KRW 환전")
                    .amount(amount)
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

    // 카카오 숙소 결제
    @Override
    @Transactional
    public int accommodationPaymentKakao(AccommodationPaymentDTO dto) {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setUserId(dto.getBuyer_email());
        paymentDTO.setName(dto.getName());
        paymentDTO.setAmount(dto.getPaid_amount());
        paymentDTO.setHistory("숙소예약");

        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setUserId(dto.getBuyer_email());
        historyDTO.setSongNo("null");
        historyDTO.setKrwNo("null");
        historyDTO.setTypeCode(1);
        historyDTO.setStateCode(1);
        historyDTO.setHistoryContent(dto.getName());
        historyDTO.setAmount(dto.getPaid_amount());
        historyDTO.setExchangeRate((double) -1);
        historyService.saveHistory(historyDTO);

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setUserId(dto.getBuyer_email());
        scheduleDTO.setTitle(dto.getName());
        scheduleDTO.setBeginDate(dto.getCustom_data().getBegin_date());
        scheduleDTO.setEndDate(dto.getCustom_data().getEnd_date());
        scheduleDTO.setTodo("숙소");
        scheduleDTO.setColor("000000");
        scheduleService.saveSchedule(scheduleDTO);

        return paymentMapper.insertPayAccommodation(paymentDTO);
    }

    // 일반 숙소 결제
    @Override
    @Transactional
    public int accommodationPayment(PaymentDTO dto) {
        UserDTO userDTO = userService.getUserByEmail(dto.getUserId());

        Double balance = myAccountMapper.selectKrwBalance(userDTO.getKrwNo());

        if(dto.getAmount() > balance) {
            throw new IllegalArgumentException("Insufficient KRW balance");
        }

        KrwAccountDTO krwAccountDTO = new KrwAccountDTO();
        krwAccountDTO.setKrwNo(userDTO.getKrwNo());
        krwAccountDTO.setBalance(balance - dto.getAmount());
        myAccountMapper.updateKrwAccount(krwAccountDTO);

        dto.setHistory("숙소예약");

        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setUserId(userDTO.getUserId());
        historyDTO.setSongNo(userDTO.getSongNo());
        historyDTO.setKrwNo(userDTO.getKrwNo());
        historyDTO.setTypeCode(1);
        historyDTO.setStateCode(1);
        historyDTO.setHistoryContent(dto.getName());
        historyDTO.setAmount(dto.getAmount());
        historyDTO.setExchangeRate((double) -1);
        historyService.saveHistory(historyDTO);

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setUserId(dto.getUserId());
        scheduleDTO.setTitle(dto.getName());
        scheduleDTO.setBeginDate(dto.getCheckInDate());
        scheduleDTO.setEndDate(dto.getCheckOutDate());
        scheduleDTO.setTodo("숙소");
        scheduleDTO.setColor("000000");
        scheduleService.saveSchedule(scheduleDTO);

        return paymentMapper.insertPayAccommodation(dto);
    }

}
