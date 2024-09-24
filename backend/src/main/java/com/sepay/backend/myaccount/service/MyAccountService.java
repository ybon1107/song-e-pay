package com.sepay.backend.myaccount.service;

import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.myaccount.dto.AccountDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;

import java.math.BigDecimal;
import java.util.Map;

public interface MyAccountService {
    Double selectKrwBalance(String krwNo);

    Double selectSongBalance(String songNo);

    // 충전
    String deposit(AccountDTO accountDTO, SongAccountDTO songAccountDTO, HistoryDTO historyDTO, Double amount);

    // 환불
    String refund(AccountDTO accountDTO, SongAccountDTO songAccountDTO, HistoryDTO historyDTO, Double amount);

    // 환전
    String exchange(SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO, HistoryDTO historyDTO, Double amount, Double exchangeRate);

    // 환급
    String reExchange(SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO, HistoryDTO historyDTO, Double amount, Double exchangeRate);

    // 송금
    String transfer(KrwAccountDTO krwAccountDTO, HistoryDTO historyDTO, Double amount, String target_krwNo);
}
