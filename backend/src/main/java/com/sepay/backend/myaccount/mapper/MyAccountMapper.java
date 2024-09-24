package com.sepay.backend.myaccount.mapper;

import com.sepay.backend.myaccount.dto.AccountDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;

public interface MyAccountMapper {
    KrwAccountDTO selectKrwAccount(String krwNo);

    SongAccountDTO selectSongAccount(String songNo);

    Double selectAccountBalance(String account);

    Double selectSongBalance(String songNo);

    Double selectKrwBalance(String krwNo);

    int updateAccount(AccountDTO accountDTO);

    int updateSongAccount(SongAccountDTO songAccountDTO);

    int updateKrwAccount(KrwAccountDTO krwAccountDTO);

}
