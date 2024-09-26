package com.sepay.backend.exchangereservation.service;

import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;
import java.util.List;
import java.util.Map;

public interface ExchangeReservationService {
    // 알람 내역 저장
    void saveExchangeReservation(ExchangeReservationDTO exchangeReservationDTO);

    // 알람 내역 조회
    List<ExchangeReservationDTO> getExchangeReservation(Integer userNo);

    // 알람 내역 삭제
    void removeExchangeReservation(Integer resNo);

    // 알람 내역 카운트
    int countExchangeReservations(Integer userNo, Integer targetKrw);
}
