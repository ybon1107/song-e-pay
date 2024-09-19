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
    public List<HistoryDTO> getHistory(SearchItem searchItem) {

        return mapper.selectByUserNo(searchItem);
    }
}
