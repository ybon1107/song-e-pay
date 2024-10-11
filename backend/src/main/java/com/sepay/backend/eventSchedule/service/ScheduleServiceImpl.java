package com.sepay.backend.eventSchedule.service;

import com.sepay.backend.eventSchedule.dto.ScheduleDTO;
import com.sepay.backend.eventSchedule.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;

    @Override
    public void saveSchedule(ScheduleDTO scheduleDTO) {
        // Mapper를 이용하여 DB에 일정 저장
        scheduleMapper.saveSchedule(scheduleDTO);
    }

    // 모든 일정 조회
    @Override
    public List<ScheduleDTO> getAllSchedules(String userId, Integer year, Integer month) {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("year", year);
        map.put("month", month);
        return scheduleMapper.selectByUserId(map);
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