package com.sepay.backend.exchangereservation.service;

import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;
import com.sepay.backend.exchangereservation.mapper.ExchangeReservationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeReservationServiceImpl implements ExchangeReservationService {

    private final ExchangeReservationMapper mapper;

    @Override
    public void saveExchangeReservation(ExchangeReservationDTO exchangeReservationDTO) {
        HashMap map = new HashMap();
        map.put("userId", exchangeReservationDTO.getUserId());
        map.put("targetKrw", 0);

        Integer reservationCount = mapper.countReservation(map);
        System.out.println("reservationCount : " + reservationCount);

        if (reservationCount >= 2) {
            log.info("알림 저장 실패: {}", exchangeReservationDTO);
            throw new IllegalArgumentException("최대 예약 수를 초과했습니다. 예약 수: " + reservationCount);
        } else {
            mapper.insertReservation(exchangeReservationDTO);
            log.info("알림이 저장되었습니다: {}", exchangeReservationDTO);
        }
    }

    @Override
    public List<ExchangeReservationDTO> getExchangeReservation (String userId) {
        log.info("알림 내역을 조회합니다. userNo: {}", userId);
        return mapper.selectReservation(userId);
    }

    @Override
    public void removeExchangeReservation(Integer resNo) {
        log.info("알림을 삭제합니다. resNo: {}", resNo);
        mapper.deleteReservation(resNo);
    }

    @Override
    public int countExchangeReservations(String userId, Integer targetKrw) {
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("targetKrw", targetKrw);
        return mapper.countReservation(map);
    }

    @Override
    public void saveAutoExchange(ExchangeReservationDTO exchangeReservationDTO) {
        HashMap map = new HashMap();
        map.put("userId", exchangeReservationDTO.getUserId());
        map.put("targetKrw", 0);

        Integer exchangeCount = mapper.countAutoexchange(map);
        System.out.println("reservationCount : " + exchangeCount);

        if (exchangeCount >= 1) {
            log.info("자동 환전 예약 실패: {}", exchangeReservationDTO);
            throw new IllegalArgumentException("최대 예약 수를 초과했습니다. 예약 수: " + exchangeCount);
        } else {
            mapper.insertAutoexchange(exchangeReservationDTO);
            log.info("자동 환전 예약이 되었습니다: {}", exchangeReservationDTO);
        }
    }

    @Override
    public List<ExchangeReservationDTO> getAutoExchange (String userId) {
        log.info("자동 환전 예약을 조회합니다. userId: {}", userId);
        return mapper.selectAutoexchange(userId);
    }

    @Override
    public int countAutoExchange(String userId, Integer targetKrw) {
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("targetKrw", targetKrw);
        return mapper.countAutoexchange(map);
    }

    // 모든 알림 내역 가지고 오기
    @Override
    public List<ExchangeReservationDTO> getAllExchangeReservation() {
        return mapper.selectAllReservation();
    }

    // 모든 자동 환전 내역 가지고오기
    @Override
    public List<ExchangeReservationDTO> getAllAutoExchange() {
        return mapper.selectAllAutoExchange();
    }
}

