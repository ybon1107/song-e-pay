package com.sepay.backend.history.service;

import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.user.dto.SearchItem;

import java.util.List;

public interface HistoryService {
    List<HistoryDTO> getHistory(SearchItem searchItem);
}
