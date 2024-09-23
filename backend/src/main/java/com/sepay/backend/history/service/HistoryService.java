package com.sepay.backend.history.service;

import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.user.dto.SearchItem;

import java.util.List;

public interface HistoryService {

    List<HistoryDTO> getFilter(SearchItem searchItem);

    List<HistoryDTO> getAllHistories(); // 모든 거래 내역을 가져오는 메서드

    void updateMemo(HistoryDTO historyDTO); // 메모 업데이트 메서드 추가
}