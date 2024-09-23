package com.sepay.backend.event.mapper;

import com.sepay.backend.event.dto.EventDTO;

import java.util.List;

public interface EventMapper {
    List<EventDTO> selectByUserNo(Integer userNo);

}
