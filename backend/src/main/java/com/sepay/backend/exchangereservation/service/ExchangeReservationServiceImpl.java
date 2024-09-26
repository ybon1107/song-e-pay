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
        map.put("userNo", exchangeReservationDTO.getUserNo());
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
    public List<ExchangeReservationDTO> getExchangeReservation (Integer userNo) {
        log.info("알림 내역을 조회합니다. userNo: {}", userNo);
        return mapper.selectReservation(userNo);
    }

    @Override
    public void removeExchangeReservation(Integer resNo) {
        log.info("알림을 삭제합니다. resNo: {}", resNo);
        mapper.deleteReservation(resNo);
    }

    @Override
    public int countExchangeReservations(Integer userNo, Integer targetKrw) {
        // userNo를 countData에서 가져옵니다.
        HashMap map = new HashMap();
        map.put("userNo", userNo);
        map.put("targetKrw", targetKrw);
        return mapper.countReservation(map);
    }
}

