package com.sepay.backend.myaccount.service;

import com.sepay.backend.myaccount.dto.AccountDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;

import java.math.BigDecimal;
import java.util.Map;

public interface MyAccountService {
    KrwAccountDTO getKrwAccountByUserNo(String krwNo);

    SongAccountDTO getSongAccountByUserNo(String songNo);


}
