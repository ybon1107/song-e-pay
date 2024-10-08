
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

//    List<HistoryDTO> selectAllHistories(PageRequest pageRequest);

//    int getTotalCount();


    void updateMemo(HistoryDTO historyDTO);
}