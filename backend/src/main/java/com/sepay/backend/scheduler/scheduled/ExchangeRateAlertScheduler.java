package com.sepay.backend.scheduler.scheduled;

import com.sepay.backend.exchange.dto.ExchangeDTO;
import com.sepay.backend.exchange.service.ExchangeService;
import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;
import com.sepay.backend.exchangereservation.service.ExchangeReservationService;
import com.sepay.backend.notification.dto.NotificationDTO;
import com.sepay.backend.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExchangeRateAlertScheduler {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ExchangeReservationService exchangeReservationService;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 30000) // 30초마다 실행
    public void checkAndSendExchangeRateAlerts() {
        log.info("환율 알림 확인 작업 시작");

        try {
            // 예약된 모든 환율 알림 가져오기
            List<ExchangeReservationDTO> allReservations = exchangeReservationService.getAllExchangeReservation();
            log.info("전체 예약 수: {}", allReservations.size());

            List<ExchangeReservationDTO> alertReservations = allReservations.stream()
                    .collect(Collectors.toList());

            log.info("환율 알림 대상 예약 수: {}", alertReservations.size());

            // 알림 처리
            for (ExchangeReservationDTO reservation : alertReservations) {
                processExchangeRateAlert(reservation);
            }
        } catch (Exception e) {
            log.error("환율 알림 처리 중 오류 발생", e);
        }
        log.info("환율 알림 확인 작업 완료");
    }

    private void processExchangeRateAlert(ExchangeReservationDTO reservation) {
        try {
            // 예약된 기준 통화 및 목표 통화의 최신 환율 정보 가져오기
            List<ExchangeDTO> latestExchangeRates = exchangeService.getExchange(reservation.getBaseCode(), reservation.getTargetCode());

            if (latestExchangeRates.isEmpty()) {
                log.warn("환율 정보를 찾을 수 없습니다. 예약 ID: {}", reservation.getResNo());
                return;
            }

            ExchangeDTO latestExchangeRate = latestExchangeRates.get(0);
            double currentRate = latestExchangeRate.getExchangeRate();
            double targetRate = reservation.getTargetExchange();

            log.debug("예약 ID: {}, 현재 환율: {}, 목표 환율: {}", reservation.getResNo(), currentRate, targetRate);

            // 환율 알림 로직 (기준 통화가 외화인지 확인하고, 목표 환율 도달 여부 체크)
            if (isTargetRateReached(reservation, currentRate)) {
                saveAlert(reservation, currentRate, reservation.getUserId());
            }

        } catch (Exception e) {
            log.error("환율 알림 처리 중 오류 발생. 예약 ID: " + reservation.getResNo(), e);
        }
    }

    private boolean isTargetRateReached(ExchangeReservationDTO reservation, double currentRate) {
        boolean isFromForeignToKRW = reservation.getBaseCode() != 0; // baseCode가 0이 아니면 외화 -> 원화
        double targetRate = reservation.getTargetExchange();

        // 외화 -> 원화의 경우: 현재 환율이 목표 환율 이하일 때 알림
        // 원화 -> 외화의 경우: 현재 환율이 목표 환율 이상일 때 알림
        return (isFromForeignToKRW && currentRate <= targetRate) || (!isFromForeignToKRW && currentRate >= targetRate);
    }

//    private void saveAlert(ExchangeReservationDTO reservation, double currentRate, String userId) {
//
//        String direction = reservation.getBaseCode() != 0 ? "하락" : "상승";
//        String message = String.format("현재 환율(%s)이 목표 환율(%s) 에 도달했습니다.",
//                currentRate, reservation.getTargetExchange());
//
//        NotificationDTO notificationDTO = new NotificationDTO();
//        notificationDTO.setUserId(userId);
//        notificationDTO.setResNo(reservation.getResNo());
//        notificationDTO.setContent(message);
//
//        notificationService.saveNotification(notificationDTO);
//
//        log.info("환율 {} 알림 저장. 예약 ID: {}, 사용자 번호: {}, 현재 환율: {}, 목표 환율: {}",
//                direction, reservation.getResNo(), reservation.getUserId(), currentRate, reservation.getTargetExchange());
//        exchangeReservationService.removeExchangeReservation(reservation.getResNo());
//    }
    private void saveAlert(ExchangeReservationDTO reservation, double currentRate, String userId) {

        String direction = reservation.getBaseCode() != 0 ? "Down" : "Up";
        String message = String.format("The current exchange rate(%s) has reached the target exchange rate(%s).",
                currentRate, reservation.getTargetExchange());

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserId(userId);
        notificationDTO.setResNo(reservation.getResNo());
        notificationDTO.setContent(message);
        notificationDTO.setAmount(0);

        notificationService.saveNotification(notificationDTO);

        log.info("Exchange rates {} Save notification. Booking ID: {}, User number: {}, Current exchange rate: {}, Target exchange rate: {}",
                direction, reservation.getResNo(), reservation.getUserId(), currentRate, reservation.getTargetExchange());
        exchangeReservationService.removeExchangeReservation(reservation.getResNo());
    }
}
