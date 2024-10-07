package com.sepay.backend.exchange.service;

import org.springframework.stereotype.Service;
import com.sepay.backend.exchange.mapper.ExchangeMapper;
import com.sepay.backend.exchange.dto.ExchangeDTO;

import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeMapper exchangeMapper;

    public ExchangeServiceImpl(ExchangeMapper exchangeMapper) {
        this.exchangeMapper = exchangeMapper;
    }

    @Override
    public List<ExchangeDTO> getExchange(Integer baseCode, Integer targetCode) {
        return exchangeMapper.getExchange(baseCode, targetCode);
    }

    @Override
    public int updateExchange(ExchangeDTO rate) {
        return exchangeMapper.updateExchange(rate);
    }
}