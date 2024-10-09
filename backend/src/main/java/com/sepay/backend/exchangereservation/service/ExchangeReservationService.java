package com.sepay.backend.exchangereservation.service;

import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;
import java.util.List;
import java.util.Map;

public interface ExchangeReservationService {
    // 알람 내역 저장
    void saveExchangeReservation(ExchangeReservationDTO exchangeReservationDTO);

    // 알람 내역 조회
    List<ExchangeReservationDTO> getExchangeReservation(String userId);

    // 알람 내역 삭제
    void removeExchangeReservation(Integer resNo);

    // 알람 내역 카운트
    int countExchangeReservations(String userId, Integer targetKrw);

    // 자동 환전 예약 저장
    void saveAutoExchange(ExchangeReservationDTO exchangeReservationDTO);

    // 자동 환전 예약 조회
    List<ExchangeReservationDTO> getAutoExchange(String userId);

    // 자동 환전 예약 카운트
    int countAutoExchange(String userId, Integer targetKrw);

    // 모든 알림 내역 불러오기
    List<ExchangeReservationDTO> getAllExchangeReservation();

    // 모든 자동 환전 내역 불러오기
    List<ExchangeReservationDTO> getAllAutoExchange();
}
