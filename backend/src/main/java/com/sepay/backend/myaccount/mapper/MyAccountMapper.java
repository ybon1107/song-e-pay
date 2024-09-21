package com.sepay.backend.myaccount.mapper;

import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;

public interface MyAccountMapper {
    KrwAccountDTO selectKrwAccount(Integer userNo);
    SongAccountDTO selectSongAccount(Integer userNo);
    SongAccountDTO findSongAccountBalance(Integer userNo);
}
