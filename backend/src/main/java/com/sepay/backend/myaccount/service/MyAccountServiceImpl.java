package com.sepay.backend.myaccount.service;

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
    public KrwAccountDTO getKrwAccountByUserNo(Integer userNo) {
        return mapper.selectKrwAccount(userNo);
    }

    @Override
    public SongAccountDTO getSongAccountByUserNo(Integer userNo) {
        return mapper.selectSongAccount(userNo);
    }

    @Override
    public SongAccountDTO getSongAccountBalance(Integer userNo) {
        // Songepay 테이블에서 계좌 잔액을 가져옴
        return mapper.findSongAccountBalance(userNo);
    }
}
