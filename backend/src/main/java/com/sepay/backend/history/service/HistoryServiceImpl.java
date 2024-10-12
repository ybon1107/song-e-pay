package com.sepay.backend.history.service;

import com.sepay.backend.common.pagination.Page;
import com.sepay.backend.common.pagination.PageRequest;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.history.mapper.HistoryMapper;
import com.sepay.backend.user.dto.SearchItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{
    private final HistoryMapper mapper;

    @Override
    public Page<HistoryDTO> getFilter(SearchItem searchItem, PageRequest pageRequest) {
        log.info("getFilter called with searchItem: {} and pageRequest: {}", searchItem, pageRequest);

        try {
            // sortOrder는 이제 SearchItem 내에서 처리됨
            List<HistoryDTO> filteredHistories = mapper.getFilter(searchItem, pageRequest);
            int totalCount = mapper.getTotalCountForFilter(searchItem);  // 필터된 데이터의 총 개수

            log.info("Filtered Histories: {}, Total Count: {}", filteredHistories, totalCount);

            return Page.of(pageRequest, totalCount, filteredHistories);
        } catch (Exception e) {
            log.error("Error occurred in getFilter method", e);
            throw e;  // 예외를 다시 던져서 상위 계층에서 처리
        }
    }

    @Override
    public void updateMemo(HistoryDTO historyDTO) {
        // Mapper를 호출하여 DB의 메모를 업데이트
        mapper.updateMemo(historyDTO);
    }

    @Override
    public int saveHistory(HistoryDTO historyDTO) {
        return mapper.insertHistory(historyDTO);
    }
}