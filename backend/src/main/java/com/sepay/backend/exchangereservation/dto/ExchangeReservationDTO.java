package com.sepay.backend.exchangereservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeReservationDTO {
    private Integer resNo;
    private String userId;
    private String songNo;
    private String krwNo;
    private Integer baseCode;
    private Integer targetCode;
    private Double targetExchange;
    private Integer targetKrw;
    private Date createAt;
    private Date updateAt;
}
