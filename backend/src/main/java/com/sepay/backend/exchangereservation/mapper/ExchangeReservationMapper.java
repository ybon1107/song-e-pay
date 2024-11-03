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
    List<ExchangeReservationDTO> selectReservation(String userId);

    // 알람 내역 삭제
    int deleteReservation(Integer resNo);

    // 알람 내역 카운트
    int countReservation(Map map);

    // 자동 환전 예약
    int insertAutoexchange(ExchangeReservationDTO exchangeReservationDTO);

    // 자동 환전 예약 화면에 보여주기
    List<ExchangeReservationDTO> selectAutoexchange(String userId);

    // 자동 환전 예약 카운트
    int countAutoexchange(Map map);

    // 모든 알림 내역 가지고 오기
    List<ExchangeReservationDTO> selectAllReservation();

    // 모든 환전 내역 가지고 오기
    List<ExchangeReservationDTO> selectAllAutoExchange();

}