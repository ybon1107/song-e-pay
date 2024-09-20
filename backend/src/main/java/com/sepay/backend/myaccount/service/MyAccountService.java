package com.sepay.backend.myaccount.service;

import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;

public interface MyAccountService {
    KrwAccountDTO getKrwAccountByUserNo(Integer userNo);
    SongAccountDTO getSongAccountByUserNo(Integer userNo);
}
