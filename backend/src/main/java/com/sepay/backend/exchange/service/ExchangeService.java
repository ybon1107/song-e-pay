package com.sepay.backend.exchange.service;

import com.sepay.backend.exchange.dto.ExchangeDTO;
import java.util.List;

public interface ExchangeService {
    List<ExchangeDTO> getExchange(Integer baseCode, Integer targetCode);
    int updateExchange(ExchangeDTO exchangeDTO);
}