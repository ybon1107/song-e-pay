package com.sepay.backend.myaccount.service;

import com.sepay.backend.exchange.mapper.ExchangeMapper;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.history.mapper.HistoryMapper;
import com.sepay.backend.history.service.HistoryService;
import com.sepay.backend.myaccount.dto.AccountDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;
import com.sepay.backend.myaccount.mapper.MyAccountMapper;
import com.sepay.backend.notification.dto.NotificationDTO;
import com.sepay.backend.notification.mapper.NotificationMapper;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyAccountServiceImpl implements MyAccountService {
    private final MyAccountMapper mapper;
    private final HistoryMapper historyMapper;
    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;
    private final HistoryService historyService;

    @Override
    public Double selectKrwBalance(String krwNo) {
        return mapper.selectKrwBalance(krwNo);
    }

    @Override
    public Double selectSongBalance(String songNo) {
        return mapper.selectSongBalance(songNo);
    }

    @Override
    public Boolean selectUserEmail(String userId) {
        String searchUserId = mapper.selectUserEmail(userId);
        return searchUserId != null && !searchUserId.isEmpty(); // userId가 null이 아니고 비어있지 않으면 true, 그렇지 않으면 false
    }

    // 충전 : 계좌 -> 송이
    @Override
    @Transactional
    public boolean deposit(AccountDTO accountDTO, SongAccountDTO songAccountDTO, HistoryDTO historyDTO, Double amount) {
        // 계좌에 충전 금액보다 많을 때
        if(mapper.selectAccountBalance(accountDTO.getAccountNo()) >= amount) {
            // 계좌 잔액 감소
            accountDTO.setBalance(mapper.selectAccountBalance(accountDTO.getAccountNo()) - amount);
            mapper.updateAccount(accountDTO);

            // 송이 계좌 증가
            songAccountDTO.setBalance(mapper.selectSongBalance(songAccountDTO.getSongNo()) + amount);
            songAccountDTO.setUpdatedAt(new Date());
            mapper.updateSongAccount(songAccountDTO);

            historyDTO.setTypeCode(3); // 환전 타입 코드
            historyDTO.setStateCode(1); // 성공 상태 코드
            historyDTO.setHistoryDate(new Date()); // 현재 날짜로 설정
            historyDTO.setHistoryContent("My Account → SongE"); // 내용 설정
            historyDTO.setAmount(amount); // 금액 설정

            // history insert
            historyMapper.insertHistory(historyDTO);
            return true;
        }
        return false;
    }

    // 환불 : 송이 -> 계좌
    @Override
    @Transactional
    public boolean refund(AccountDTO accountDTO, SongAccountDTO songAccountDTO, HistoryDTO historyDTO, Double amount) {
        // 송이 계좌에 환불 금액보다 많을 때
        if(mapper.selectSongBalance(songAccountDTO.getSongNo()) >= amount) {
            // 송이 계좌 감소
            songAccountDTO.setBalance(mapper.selectSongBalance(songAccountDTO.getSongNo()) - amount);
            songAccountDTO.setUpdatedAt(new Date());
            mapper.updateSongAccount(songAccountDTO);

            // 계좌 증가
            accountDTO.setBalance(mapper.selectAccountBalance(accountDTO.getAccountNo()) + amount);
            mapper.updateAccount(accountDTO);

            // history insert
            historyDTO.setTypeCode(4); //환불
            historyDTO.setStateCode(1); // 성공 상태 코드
            historyDTO.setHistoryDate(new Date()); // 현재 날짜로 설정
            historyDTO.setHistoryContent("SongE → My Account"); // 내용 설정
            historyDTO.setAmount(amount); // 금액 설정

            historyMapper.insertHistory(historyDTO);
            return true;
        }
        return false;
    }

    // 환전 : 송이 -> 원화
    @Override
    @Transactional
    public boolean exchange(SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO, HistoryDTO historyDTO , Double amount, Double exchangeRate) {
        // 송이 계좌에 환전 금액보다 많을 때
        double songToKrw = Math.round((amount / exchangeRate) * 100.0) / 100.0;

        if(mapper.selectSongBalance(songAccountDTO.getSongNo()) >= songToKrw) {
            // 송이 계좌 감소
            //double songAmount = Math.round(amount * exchangeRate);
            songAccountDTO.setBalance(mapper.selectSongBalance(songAccountDTO.getSongNo())  - songToKrw);
            songAccountDTO.setUpdatedAt(new Date());
            mapper.updateSongAccount(songAccountDTO);

            // 원화 금액 증가
            krwAccountDTO.setBalance(mapper.selectKrwBalance(krwAccountDTO.getKrwNo()) + amount);
            krwAccountDTO.setUpdatedAt(new Date());
            mapper.updateKrwAccount(krwAccountDTO);

            // history insert
            historyDTO.setTypeCode(5); // 환전 타입 코드
            historyDTO.setStateCode(1); // 성공 상태 코드
            historyDTO.setHistoryDate(new Date()); // 현재 날짜로 설정
            historyDTO.setHistoryContent("SongE → WonE"); // 내용 설정
            historyDTO.setAmount(amount); // 금액 설정
            historyDTO.setExchangeRate(exchangeRate); //환율 설정

            historyMapper.insertHistory(historyDTO);
           return true;
        }
        return false;
    }

    // 환급 : 원화 -> 송이
    @Override
    @Transactional
    public boolean reExchange(SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO, HistoryDTO historyDTO , Double amount, Double exchangeRate) {

        double krwToSong = Math.round(amount * exchangeRate);
        // 원화 계좌에 환급 금액보다 많을 때
        if(mapper.selectKrwBalance(krwAccountDTO.getKrwNo()) >= krwToSong) {
            // 원화 계좌 감소
            //double krwAmount = Math.round(amount * exchangeRate);
            krwAccountDTO.setBalance(mapper.selectKrwBalance(krwAccountDTO.getKrwNo()) - krwToSong);
            krwAccountDTO.setUpdatedAt(new Date());
            mapper.updateKrwAccount(krwAccountDTO);

            // 송이 계좌 증가
            songAccountDTO.setBalance(mapper.selectSongBalance(songAccountDTO.getSongNo()) + amount);
            songAccountDTO.setUpdatedAt(new Date());
            mapper.updateSongAccount(songAccountDTO);

            // history insert
            historyDTO.setTypeCode(6); // 환급 타입 코드
            historyDTO.setStateCode(1); // 성공 상태 코드
            historyDTO.setHistoryDate(new Date()); // 현재 날짜로 설정
            historyDTO.setHistoryContent("WonE → SongE"); // 내용 설정
            historyDTO.setAmount(amount); // 금액 설정
            historyDTO.setExchangeRate(exchangeRate); //환율 설정

            historyMapper.insertHistory(historyDTO);

           return true;
        }
        return false;
    }

    // 송금
    @Override
    @Transactional
    public String transfer(KrwAccountDTO myKrwAccount, HistoryDTO historyDTO, Double amount, String target_krwNo, String targetHistoryContent) {
        String message = "계좌에 잔액이 부족합니다";
        // 원화 계좌에 송금 금액보다 많을 때
        if(mapper.selectKrwBalance(myKrwAccount.getKrwNo()) >= amount) {
            // 내 계좌 감소
            myKrwAccount.setBalance(mapper.selectKrwBalance(myKrwAccount.getKrwNo()) - amount);
            mapper.updateKrwAccount(myKrwAccount);

            // 상대 계좌 증가

            KrwAccountDTO targetKrwAccount = new KrwAccountDTO();
            targetKrwAccount.setKrwNo(target_krwNo);
            targetKrwAccount.setBalance(mapper.selectKrwBalance(target_krwNo) + amount);
            mapper.updateKrwAccount(targetKrwAccount);
            // history insert

            // 상대방 정보 조회
            UserDTO targetUser = mapper.selectUserByKrwNo(target_krwNo);
            historyMapper.insertHistory(historyDTO);
            // 상대 history insert
            HistoryDTO targetHistoryDTO = new HistoryDTO();
            targetHistoryDTO.setUserId(targetUser.getUserId());
            targetHistoryDTO.setSongNo(targetUser.getSongNo());
            targetHistoryDTO.setKrwNo(target_krwNo);
            targetHistoryDTO.setTypeCode(8); // 또는 적절한 코드
            targetHistoryDTO.setStateCode(1); // 또는 적절한 상태 코드
            targetHistoryDTO.setHistoryContent(targetHistoryContent);
            targetHistoryDTO.setAmount(amount);
            // 필요한 다른 필드들 설정
            historyMapper.insertHistory(targetHistoryDTO);

            message = "success";
        }
        return message;
    }

    @Override
    @Transactional
    public String getKrwno(String userId) { return mapper.selectKrwNo(userId);}

    // 송금 받기
    @Override
    @Transactional
    public int receiveSongE(NotificationDTO notificationDTO) {
        UserDTO user = userMapper.selectUserByEmail(notificationDTO.getUserId());
        KrwAccountDTO dto = new KrwAccountDTO();
        dto.setKrwNo(user.getKrwNo());
        Double balance = mapper.selectKrwBalance(dto.getKrwNo()) + notificationDTO.getAmount();
        dto.setBalance(balance);

        notificationMapper.updateNotiAmount(notificationDTO.getNotiNo());

        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setUserId(user.getUserId());
        historyDTO.setSongNo(user.getSongNo());
        historyDTO.setKrwNo(user.getKrwNo());
        historyDTO.setTypeCode(8);
        historyDTO.setStateCode(1);
        historyDTO.setHistoryContent("입금");
        historyDTO.setAmount(Double.valueOf(notificationDTO.getAmount()));
        historyDTO.setExchangeRate((double) -1);
        historyService.saveHistory(historyDTO);

        return mapper.updateKrwAccount(dto);
    }
}
