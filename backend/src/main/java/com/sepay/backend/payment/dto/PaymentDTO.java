package com.sepay.backend.payment.dto;

import com.sepay.backend.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Double amount;
    private String userId;
    private String krwNo;
    private String songNo;
    private String historyContent;
}
