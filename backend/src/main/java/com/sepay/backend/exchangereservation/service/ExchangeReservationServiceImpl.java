package com.sepay.backend.exchangereservation.service;

import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;
import com.sepay.backend.exchangereservation.mapper.ExchangeReservationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeReservationServiceImpl implements ExchangeReservationService {
    private final ExchangeReservationMapper mapper;

    @Override
    public ExchangeReservationDTO getExchangeReservation(Integer userNo) {
        return mapper.selectByUserNo(userNo);
    }
}
