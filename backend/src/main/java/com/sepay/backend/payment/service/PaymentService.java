package com.sepay.backend.payment.service;

import com.google.zxing.WriterException;
<<<<<<< HEAD
=======
import com.sepay.backend.notification.dto.NotificationDTO;
import com.sepay.backend.payment.dto.PaymentDTO;
import com.sepay.backend.reservation.dto.AccommodationPaymentDTO;
>>>>>>> a3b5ae426b7f1c856bee370fcd645a3b8079e823
import com.sepay.backend.user.dto.UserDTO;

import java.io.IOException;

public interface PaymentService {
    byte[] createQR(String url) throws WriterException, IOException;

    void payment(Double dto, UserDTO user);

    // 카카오 숙소 결제
    int accommodationPaymentKakao(AccommodationPaymentDTO dto);

    // 일반 숙소 결제
    int accommodationPayment(PaymentDTO dto);

}
