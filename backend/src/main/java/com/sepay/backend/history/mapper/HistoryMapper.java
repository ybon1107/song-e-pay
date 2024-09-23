package com.sepay.backend.history.mapper;


import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.user.dto.SearchItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface HistoryMapper {
    List<HistoryDTO> getFilter(SearchItem searchItem);

    List<HistoryDTO> selectAllHistories();

    void updateMemo(HistoryDTO historyDTO);
}