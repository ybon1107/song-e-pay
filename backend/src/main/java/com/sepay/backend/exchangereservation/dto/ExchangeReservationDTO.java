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
    private Integer userNo;
    private String songNo;
    private String krwNo;
    private Double targetExchange;
    private Integer targetKrw;
    private Date createAT;
    private Date updateAT;
}
