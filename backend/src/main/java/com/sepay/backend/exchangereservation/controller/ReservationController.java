package com.sepay.backend.exchangereservation.controller;

import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;
import com.sepay.backend.exchangereservation.service.ExchangeReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange-reservation")
public class ReservationController {

    private final ExchangeReservationService exchangeReservationService;

    @PostMapping("")
    public ResponseEntity<?> saveExchangeReservation(@RequestBody ExchangeReservationDTO exchangeReservationDTO) {
        log.info("환전 예약 저장 요청: {}", exchangeReservationDTO);
        try {
            exchangeReservationService.saveExchangeReservation(exchangeReservationDTO);
            return ResponseEntity.ok().body("환율 알림이 성공적으로 저장되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("환율 알림 저장 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ExchangeReservationDTO>> getExchangeReservation(@PathVariable String userId) {
        log.info("환전 예약 조회 요청. 사용자 번호: {}", userId);
        List<ExchangeReservationDTO> reservations = exchangeReservationService.getExchangeReservation(userId);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{resNo}")
    public ResponseEntity<Void> removeExchangeReservation(@PathVariable Integer resNo) {
        log.info("삭제 요청. 예약 번호: {}", resNo);
        exchangeReservationService.removeExchangeReservation(resNo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/count")
    public ResponseEntity<Integer> countExchangeReservations(@RequestBody String userId, Integer targetKrw) {
        log.info("환전 예약 개수 조회 요청: {}",  userId,  targetKrw);
        int count = exchangeReservationService.countExchangeReservations(userId, targetKrw);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/setalert")
    public ResponseEntity<?> saveAutoExchange(@RequestBody ExchangeReservationDTO exchangeReservationDTO) {
        log.info("자동 환전 예약 저장 요청: {}", exchangeReservationDTO);
        try {
            exchangeReservationService.saveAutoExchange(exchangeReservationDTO);
            return ResponseEntity.ok().body("환율 알림이 성공적으로 저장되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("환율 알림 저장 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }

    @GetMapping("/setalert/{userId}")
    public ResponseEntity<List<ExchangeReservationDTO>> getAutoExchange(@PathVariable String userId) {
        log.info("자동 환전 예약 조회 요청. 사용자 번호: {}", userId);
        List<ExchangeReservationDTO> reservations = exchangeReservationService.getAutoExchange(userId);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/setalert/count")
    public ResponseEntity<Integer> countAutoExchange(@RequestBody String userId, Integer targetKrw) {
        log.info("환전 예약 개수 조회 요청: {}",  userId,  targetKrw);
        int count = exchangeReservationService.countAutoExchange(userId, targetKrw);
        return ResponseEntity.ok(count);
    }
}