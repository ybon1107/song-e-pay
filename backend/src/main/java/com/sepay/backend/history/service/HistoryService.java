package com.sepay.backend.history.service;

import com.sepay.backend.common.pagination.Page;
import com.sepay.backend.common.pagination.PageRequest;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.user.dto.SearchItem;

import javax.swing.*;
import java.util.List;

public interface HistoryService {

    Page<HistoryDTO> getFilter(SearchItem searchItem, PageRequest pageRequest); // 페이지네이션 포함된 필터 메서드

//    Page<HistoryDTO> getAllHistories(PageRequest pageRequest); // 모든 거래 내역을 가져오는 메서드

    void updateMemo(HistoryDTO historyDTO); // 메모 업데이트 메서드 추가

    int saveHistory(HistoryDTO historyDTO);
}