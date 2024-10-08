package com.sepay.backend.exchange.service;

import com.sepay.backend.exchange.dto.ExchangeDTO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExchangeService {
    List<ExchangeDTO> getExchange(@Param("baseCode") Integer baseCode, @Param("targetCode") Integer targetCode);
    int updateExchange(ExchangeDTO exchangeDTO);
}