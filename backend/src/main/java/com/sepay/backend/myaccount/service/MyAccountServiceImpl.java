package com.sepay.backend.myaccount.service;

import com.sepay.backend.myaccount.dto.AccountDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;
import com.sepay.backend.myaccount.mapper.MyAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyAccountServiceImpl implements MyAccountService {
    private final MyAccountMapper mapper;

    @Override
    public KrwAccountDTO getKrwAccountByUserNo(String krwNo) {
        return mapper.selectKrwAccount(krwNo);
    }

    @Override
    public SongAccountDTO getSongAccountByUserNo(String songNo) {
        return mapper.selectSongAccount(songNo);
    }

    // 충전 : 계좌 -> 송이
    @Override
    public String deposit(AccountDTO accountDTO, SongAccountDTO songAccountDTO, Double amount) {
        String message = "계좌에 잔액이 부족합니다";
        // 계좌에 충전 금액보다 많을 때
        if(mapper.selectAccountBalance(accountDTO.getAccountNo()) >= amount) {
            // 계좌 잔액 감소
            accountDTO.setBalance(accountDTO.getBalance() - amount);
            mapper.updateAccount(accountDTO);

            // 송이 계좌 증가
            songAccountDTO.setBalance(songAccountDTO.getBalance() + amount);
            mapper.updateSongAccount(songAccountDTO);
            message = "success";
        }
        return message;
    }

    // 환불 : 송이 -> 계좌
    @Override
    public String refund(AccountDTO accountDTO, SongAccountDTO songAccountDTO, Double amount) {
        String message = "계좌에 잔액이 부족합니다";
        // 송이 계좌에 환불 금액보다 많을 때
        if(mapper.selectSongBalance(songAccountDTO.getSongNo()) >= amount) {
            // 송이 계좌 감소
            songAccountDTO.setBalance(songAccountDTO.getBalance() - amount);
            mapper.updateSongAccount(songAccountDTO);

            // 계좌 증가
            accountDTO.setBalance(accountDTO.getBalance() + amount);
            mapper.updateAccount(accountDTO);
            message = "success";
        }
        return message;
    }

    // 환전 : 송이 -> 원화
    @Override
    public String exchange(SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO, Double amount, Double exchangeRate) {
        String message = "계좌에 잔액이 부족합니다";
        // 송이 계좌에 환전 금액보다 많을 때
        if(mapper.selectSongBalance(songAccountDTO.getSongNo()) >= amount) {
            // 송이 계좌 감소
            songAccountDTO.setBalance(songAccountDTO.getBalance() - amount);
            mapper.updateSongAccount(songAccountDTO);

            // 원화 금액 증가
            amount *= exchangeRate;
            krwAccountDTO.setBalance(krwAccountDTO.getBalance() + amount);
            mapper.updateKrwAccount(krwAccountDTO);
        }
        return message;
    }

    // 환급 : 원화 -> 송이
    @Override
    public String reExchange(SongAccountDTO songAccountDTO, KrwAccountDTO krwAccountDTO, Double amount, Double exchangeRate) {
        String message = "계좌에 잔액이 부족합니다";
        // 원화 계좌에 환급 금액보다 많을 때
        if(mapper.selectKrwBalance(krwAccountDTO.getKrwNo()) >= amount) {
            // 원화 계좌 감소
            krwAccountDTO.setBalance(krwAccountDTO.getBalance() - amount);
            mapper.updateKrwAccount(krwAccountDTO);

            // 송이 계좌 증가
            amount *= exchangeRate;
            songAccountDTO.setBalance(songAccountDTO.getBalance() + amount);
            mapper.updateSongAccount(songAccountDTO);
        }
        return message;
    }

    // 송금
    @Override
    public String transfer(KrwAccountDTO myKrwAccount, Double amount, String target_krwNo) {
        String message = "계좌에 잔액이 부족합니다";
        // 원화 계좌에 송금 금액보다 많을 때
        if(mapper.selectKrwBalance(myKrwAccount.getKrwNo()) >= amount) {
            // 내 계좌 감소
            myKrwAccount.setBalance(myKrwAccount.getBalance() - amount);
            mapper.updateKrwAccount(myKrwAccount);

            // 상대 계좌 증가
            KrwAccountDTO targetKrwAccount = mapper.selectKrwAccount(target_krwNo);
            targetKrwAccount.setBalance(targetKrwAccount.getBalance() + amount);
            mapper.updateKrwAccount(targetKrwAccount);
        }
        return message;
    }

}
