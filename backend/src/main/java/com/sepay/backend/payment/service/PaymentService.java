package com.sepay.backend.payment.service;

import com.google.zxing.WriterException;
import com.sepay.backend.payment.dto.PaymentDTO;
import com.sepay.backend.user.dto.UserDTO;

import java.io.IOException;

public interface PaymentService {
    byte[] createQR(String url) throws WriterException, IOException;

    void payment(PaymentDTO dto);
}
