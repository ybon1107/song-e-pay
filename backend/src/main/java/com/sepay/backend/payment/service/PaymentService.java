package com.sepay.backend.payment.service;

import com.google.zxing.WriterException;
import com.sepay.backend.notification.dto.NotificationDTO;
import com.sepay.backend.payment.dto.PaymentDTO;
import com.sepay.backend.reservation.dto.AccommodationPaymentDTO;
import com.sepay.backend.user.dto.UserDTO;

import java.io.IOException;

public interface PaymentService {
    byte[] createQR(String url) throws WriterException, IOException;

    void payment(PaymentDTO paymentDTO);

    // 카카오 숙소 결제
    int accommodationPaymentKakao(AccommodationPaymentDTO dto);

    // 일반 숙소 결제
    int accommodationPayment(PaymentDTO dto);

}
