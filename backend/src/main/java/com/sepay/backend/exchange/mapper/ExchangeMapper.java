package com.sepay.backend.exchange.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.sepay.backend.exchange.dto.ExchangeDTO;

import java.util.List;

@Mapper
public interface ExchangeMapper {
    List<ExchangeDTO> getExchange(Integer baseCode, Integer targetCode);
    int updateExchange(ExchangeDTO rate);
}