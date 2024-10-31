package com.sepay.backend.payment.dto;

import com.sepay.backend.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Integer paymentNo;
    private String userId;
    private String name;
    private Double amount;
    private String history;
    private Date checkInDate;
    private Date checkOutDate;
    private Date createdAt;
    private Date updatedAt;

    private String krwNo;
    private String songNo;
    private String historyContent;
}
