package com.sepay.backend.exchange.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeDTO {
    private Integer baseCode;
    private Integer targetCode;
    private Double exchangeRate;
    private Date updatedAt;
}
