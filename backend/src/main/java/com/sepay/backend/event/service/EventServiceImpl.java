package com.sepay.backend.event.service;

import com.sepay.backend.event.dto.EventDTO;
import com.sepay.backend.event.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventMapper mapper;


    @Override
    public List<EventDTO> getEvent(Integer userNo) {
        return mapper.selectByUserNo(userNo);
    }

}
