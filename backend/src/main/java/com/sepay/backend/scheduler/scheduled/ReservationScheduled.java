package com.sepay.backend.scheduler.scheduled;

import com.sepay.backend.exchange.dto.ExchangeDTO;
import com.sepay.backend.exchange.service.ExchangeService;
import com.sepay.backend.exchangereservation.dto.ExchangeReservationDTO;
import com.sepay.backend.exchangereservation.service.ExchangeReservationService;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;
import com.sepay.backend.myaccount.service.MyAccountService;
import com.sepay.backend.notification.dto.NotificationDTO;
import com.sepay.backend.notification.service.NotificationService;
import com.sepay.backend.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.sepay.backend.user.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReservationScheduled {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ExchangeReservationService exchangeReservationService;

    @Autowired
    private MyAccountService myAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 30000) // 30초마다 실행
    public void checkAndExecuteAutoExchangeReservations() {
        log.info("자동 환전 예약 확인 및 실행 작업 시작");

        try {
            List<ExchangeReservationDTO> allReservations = exchangeReservationService.getAllAutoExchange();
            log.info("전체 예약 수: {}", allReservations.size());
            List<ExchangeReservationDTO> autoExchangeReservations = allReservations.stream()
                    .collect(Collectors.toList());

            log.info("자동 환전 대상 예약 수: {}", autoExchangeReservations.size());

            for (ExchangeReservationDTO reservation : autoExchangeReservations) {
                processAutoExchangeReservation(reservation);
            }
        } catch (Exception e) {
            log.error("자동 환전 예약 처리 중 오류 발생", e);
        }

        log.info("자동 환전 예약 확인 및 실행 작업 완료");
    }

    private void processAutoExchangeReservation(ExchangeReservationDTO reservation) {
        try {
            log.debug("처리 중인 예약: resNo={}, baseCode={}, targetCode={}",
                    reservation.getResNo(), reservation.getBaseCode(), reservation.getTargetCode());

            List<ExchangeDTO> latestExchangeRates = exchangeService.getExchange(reservation.getBaseCode(), reservation.getTargetCode());

            if (latestExchangeRates.isEmpty()) {
                log.warn("환율 정보를 찾을 수 없습니다. 예약 ID: {}", reservation.getResNo());
                return;
            }

            ExchangeDTO latestExchangeRate = latestExchangeRates.get(0);

            log.debug("예약 ID: {}, 현재 환율: {}, 목표 환율: {}, 목표 원화 금액: {}",
                    reservation.getResNo(), latestExchangeRate.getExchangeRate(), reservation.getTargetExchange(), reservation.getTargetKrw());

            if (isTargetRateReached(latestExchangeRate.getExchangeRate(), reservation.getTargetExchange())) {
                executeAutoExchange(reservation, latestExchangeRate);
            }
        } catch (Exception e) {
            log.error("자동 환전 예약 처리 중 오류 발생. 예약 ID: " + reservation.getResNo(), e);
        }
    }

    private boolean isTargetRateReached(double currentExchangeRate, double targetRate) {
        // 현재 환율이 목표 환율 이하일 때 조건 충족
        return currentExchangeRate <= targetRate;
    }

    private void executeAutoExchange(ExchangeReservationDTO reservation, ExchangeDTO latestExchangeRate) {
        log.info("자동 환전 실행 시작. 예약 ID: {}, 송이 -> 원화, 목표 환율: {}, 현재 환율: {}, 목표 원화 금액: {}",
                reservation.getResNo(), reservation.getTargetExchange(), latestExchangeRate.getExchangeRate(), reservation.getTargetKrw());

        try {
            String userId = reservation.getUserId();
            UserDTO userAccounts = userService.getUserByEmail(userId);
            String songNo = userAccounts.getSongNo();
            String krwNo = userAccounts.getKrwNo();

            if (songNo == null || krwNo == null) {
                log.error("사용자 계좌 정보를 찾을 수 없습니다. 예약 ID: {}, 사용자 번호: {}", reservation.getResNo(), userId);
                return;
            }

            double songAmount = Math.round((reservation.getTargetKrw() / latestExchangeRate.getExchangeRate()) * 100.0) / 100.0;
            double krwAmount = reservation.getTargetKrw();

            SongAccountDTO songAccountDTO = new SongAccountDTO();
            songAccountDTO.setSongNo(songNo);

            KrwAccountDTO krwAccountDTO = new KrwAccountDTO();
            krwAccountDTO.setKrwNo(krwNo);

            HistoryDTO historyDTO = HistoryDTO.builder()
                    .userId(userId)
                    .songNo(songNo)
                    .krwNo(krwNo)
                    .typeCode(7) // 환전 타입 코드
                    .stateCode(1) // 성공 상태 코드
                    .historyDate(new Date())
                    .historyContent("자동 환전: 송이 -> 원화")
                    .amount(songAmount)
                    .exchangeRate(latestExchangeRate.getExchangeRate())
                    .memo("자동 환전 예약 실행")
                    .build();


            boolean result = myAccountService.exchange(songAccountDTO, krwAccountDTO, historyDTO, krwAmount, latestExchangeRate.getExchangeRate());

            if (result) {
                log.info("자동 환전 성공. 예약 ID: {}, 현재 환율: {}, 송이 차감액: {}, 원화 입금액: {}",
                        reservation.getResNo(), latestExchangeRate.getExchangeRate(), songAmount, krwAmount);
                exchangeReservationService.removeExchangeReservation(reservation.getResNo());
                saveAlert(reservation, userId);
            } else {
                log.warn("자동 환전 실패. 예약 ID: {}, 현재 환율: {}, 오류 메시지: {}",
                        reservation.getResNo(), latestExchangeRate.getExchangeRate(), result);
            }
        } catch (Exception e) {
            log.error("자동 환전 실행 중 오류 발생. 예약 ID: " + reservation.getResNo(), e);
        }
    }

    private void saveAlert(ExchangeReservationDTO reservation, String userId) {
        String message = String.format("Automatic currency exchange has been completed.");

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserId(userId);
        notificationDTO.setResNo(reservation.getResNo());
        notificationDTO.setContent(message);
        notificationDTO.setAmount(0);

        notificationService.saveNotification(notificationDTO); // 알림 저장 호출

    }
}
