package com.sepay.backend.myaccount.service;

import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.myaccount.dto.AccountDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;
import com.sepay.backend.notification.dto.NotificationDTO;

import java.math.BigDecimal;
import java.util.Map;

public interface MyAccountService {
    // krw 계좌 잔액 조회
    Double selectKrwBalance(String krwNo);

    // songe 계좌 잔액 조회
    Double selectSongBalance(String songNo);

    // 이메일 확인
    Boolean selectUserEmail(String userId);

    // 충전
    boolean deposit(AccountDTO accountDTO, SongAccountDTO songAccountDTO, HistoryDTO historyDTO, Double amount);

    // 환불
    boolean refund(AccountDTO accountDTO, SongAccountDTO songAccountDTO, HistoryDTO historyDTO, Double amount);

    // 환전
    boolean exchange(SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO, HistoryDTO historyDTO, Double amount, Double exchangeRate);

    // 환급
    boolean reExchange(SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO, HistoryDTO historyDTO, Double amount, Double exchangeRate);

    // 송금
    String transfer(KrwAccountDTO krwAccountDTO, HistoryDTO historyDTO, Double amount, String target_krwNo, String targetHistoryContent);

    String getKrwno(String userId);

    // 송금 받기
    int receiveSongE(NotificationDTO dto);
}
