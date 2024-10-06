package com.sepay.backend.exchange.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sepay.backend.exchange.mapper.ExchangeMapper;
import com.sepay.backend.exchange.dto.ExchangeDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    final ExchangeMapper exchangeMapper;

    @Override
    public List<ExchangeDTO> getExchange(Integer baseCode, Integer targetCode) {
        return exchangeMapper.selectExchange(baseCode, targetCode);
    }

    @Override
    public int updateExchange(ExchangeDTO rate) {
        return exchangeMapper.updateExchange(rate);
    }
}