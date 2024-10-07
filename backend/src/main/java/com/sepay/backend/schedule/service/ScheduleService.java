package com.sepay.backend.schedule.service;

import com.sepay.backend.schedule.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {

    void saveSchedule(ScheduleDTO scheduleDTO);

    List<ScheduleDTO> getAllSchedules();

    ScheduleDTO updateSchedule(ScheduleDTO scheduleDTO); // 업데이트 메서드 추가
}