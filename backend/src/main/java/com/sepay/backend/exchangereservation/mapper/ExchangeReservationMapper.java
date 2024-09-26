package com.sepay.backend.exchangereservation.mapper;

import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;
import java.util.List;

@Mapper
public interface ExchangeReservationMapper {
    // 알람 내역 저장
    int insertReservation(ExchangeReservationDTO exchangeReservationDTO);

    // 알람 내역 화면에 보여주기
    List<ExchangeReservationDTO> selectReservation(Integer userNo);

    // 알람 내역 삭제
    int deleteReservation(Integer resNo);

    // 알람 내역 카운트
    int countReservation(Map map);
}