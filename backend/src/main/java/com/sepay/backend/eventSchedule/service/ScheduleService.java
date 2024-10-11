package com.sepay.backend.eventSchedule.service;

import com.sepay.backend.eventSchedule.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {

    void saveSchedule(ScheduleDTO scheduleDTO);

    List<ScheduleDTO> getAllSchedules(String userId, Integer year, Integer month);

    ScheduleDTO updateSchedule(ScheduleDTO scheduleDTO); // 업데이트 메서드 추가

    void deleteSchedule(Integer eventNo);
}