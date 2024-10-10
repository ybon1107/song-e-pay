package com.sepay.backend.schedule.service;

import com.sepay.backend.schedule.dto.ScheduleDTO;
import com.sepay.backend.schedule.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;

    @Override
    public void saveSchedule(ScheduleDTO scheduleDTO) {
        // Mapper를 이용하여 DB에 일정 저장
        scheduleMapper.saveSchedule(scheduleDTO);
    }

    @Override
    public List<ScheduleDTO> getAllSchedules() {
        // Mapper를 이용하여 모든 일정 조회
        return scheduleMapper.findAll();
    }


    @Override
    public ScheduleDTO updateSchedule(ScheduleDTO scheduleDTO) {
        int rowsAffected = scheduleMapper.updateSchedule(scheduleDTO);
        if (rowsAffected > 0) {
            return scheduleDTO; // 업데이트된 ScheduleDTO를 반환
        } else {
            throw new RuntimeException("Failed to update schedule with eventNo: " + scheduleDTO.getEventNo());
        }
    }

    @Override
    public void deleteSchedule(Integer eventNo) {
        int rowsAffected = scheduleMapper.deleteSchedule(eventNo);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to delete schedule with eventNo: " + eventNo);
        }
    }
}