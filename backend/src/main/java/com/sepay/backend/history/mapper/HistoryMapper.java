package com.sepay.backend.history.mapper;


import com.sepay.backend.common.pagination.Page;
import com.sepay.backend.common.pagination.PageRequest;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.user.dto.SearchItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HistoryMapper {
    List<HistoryDTO> getFilter(@Param("searchItem") SearchItem searchItem, @Param("pageRequest") PageRequest pageRequest);

//    List<HistoryDTO> getFilter(SearchItem searchItem, PageRequest pageRequest, String sortOrder);

    int getTotalCountForFilter(SearchItem searchItem);

    // 이용 내역 추가
    int insertHistory(HistoryDTO historyDTO);

    void updateMemo(HistoryDTO historyDTO);
}