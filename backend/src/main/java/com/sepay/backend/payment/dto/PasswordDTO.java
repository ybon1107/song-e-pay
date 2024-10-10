package com.sepay.backend.payment.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordDTO {
    private String userId;
    private String password;
}