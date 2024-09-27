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
            // 필터링된 결과와 페이지네이션 처리
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
    public Page<HistoryDTO> getAllHistories(PageRequest pageRequest) {
        // 페이지 요청에 맞는 거래 내역을 가져옴
        List<HistoryDTO> histories = mapper.selectAllHistories(pageRequest);

        // 전체 거래 내역 수를 가져옴
        int totalCount = mapper.getTotalCount();

        // Page 객체를 생성하여 반환
        return Page.of(pageRequest, totalCount, histories);
    }

    @Override
    public void updateMemo(HistoryDTO historyDTO) {
        // Mapper를 호출하여 DB의 메모를 업데이트
        mapper.updateMemo(historyDTO);
    }
}