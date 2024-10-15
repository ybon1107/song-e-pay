package com.sepay.backend.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationPaymentDTO {
    private String merchant_uid;
    private String name;
    private String bank_name;
    private String buyer_addr;
    private String buyer_email;
    private String buyer_name;
    private String buyer_postcode;
    private String buyer_tel;
    private CustomData custom_data;
    private Double paid_amount;

    @Data
    public static class CustomData {
        private Date begin_date;
        private Date end_date;
    }
}
