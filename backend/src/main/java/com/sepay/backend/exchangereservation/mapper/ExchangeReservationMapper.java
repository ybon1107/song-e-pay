package com.sepay.backend.exchangereservation.mapper;


import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;

public interface ExchangeReservationMapper {
    ExchangeReservationDTO selectByUserNo(Integer userNo);

}
