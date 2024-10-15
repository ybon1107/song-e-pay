package com.sepay.backend.payment.mapper;

import com.sepay.backend.payment.dto.PaymentDTO;

public interface PaymentMapper {

    // 숙소 결제
    int insertPayAccommodation(PaymentDTO paymentDTO);
}
