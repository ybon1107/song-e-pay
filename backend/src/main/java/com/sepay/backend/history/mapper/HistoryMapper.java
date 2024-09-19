package com.sepay.backend.history.mapper;


import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.user.dto.SearchItem;

import java.util.List;

public interface HistoryMapper {
    List<HistoryDTO> selectByUserNo(SearchItem searchItem);

}
