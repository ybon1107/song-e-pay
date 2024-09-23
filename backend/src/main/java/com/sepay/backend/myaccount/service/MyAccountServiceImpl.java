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





}
