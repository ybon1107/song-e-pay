package com.sepay.backend.payment.mapper;

import com.sepay.backend.payment.dto.PasswordDTO;

public interface PaymentMapper {
    int getPassword(int userNo);
}
