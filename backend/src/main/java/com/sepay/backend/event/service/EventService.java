package com.sepay.backend.event.service;


import com.sepay.backend.event.dto.EventDTO;

import java.util.List;

public interface EventService {
    List<EventDTO> getEvent (Integer userNo);
}
