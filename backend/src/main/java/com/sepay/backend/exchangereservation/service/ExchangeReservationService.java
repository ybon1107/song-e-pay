package com.sepay.backend.exchangereservation.service;

import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;

public interface ExchangeReservationService {
    ExchangeReservationDTO getExchangeReservation(Integer userNo);

}
