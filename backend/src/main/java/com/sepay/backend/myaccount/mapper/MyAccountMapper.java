package com.sepay.backend.myaccount.mapper;

import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.myaccount.dto.AccountDTO;
import com.sepay.backend.myaccount.dto.KrwAccountDTO;
import com.sepay.backend.myaccount.dto.SongAccountDTO;

public interface MyAccountMapper {
    Double selectAccountBalance(String account);

    Double selectSongBalance(String songNo);

    Double selectKrwBalance(String krwNo);

    String selectUserEmail(String userId);

    String selectSecondPwd(Integer userId);

    int updateAccount(AccountDTO accountDTO);

    int updateSongAccount(SongAccountDTO songAccountDTO);

    int updateKrwAccount(KrwAccountDTO krwAccountDTO);

    int insertHistory(HistoryDTO historyDTO);
}
