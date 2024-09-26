package com.sepay.backend.history.service;

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
    public List<HistoryDTO> getFilter(SearchItem searchItem) {
        // 필터링된 결과를 가져오기 위해 Mapper 호출
        System.out.println("filter service");
        return mapper.getFilter(searchItem);
    }

    @Override
    public List<HistoryDTO> getAllHistories() {
        // 데이터베이스에서 모든 거래 내역을 가져옵니다.
        return mapper.selectAllHistories();
    }

    @Override
    public void updateMemo(HistoryDTO historyDTO) {
        // Mapper를 호출하여 DB의 메모를 업데이트
        mapper.updateMemo(historyDTO);
    }
}