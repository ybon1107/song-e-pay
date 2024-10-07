package com.sepay.backend.schedule.mapper;

import com.sepay.backend.schedule.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleMapper {

    void saveSchedule(ScheduleDTO scheduleDTO);

    List<ScheduleDTO> findAll();
    // 일정 업데이트
    int updateSchedule(ScheduleDTO scheduleDTO);

    ScheduleDTO getScheduleById(Integer eventNo);

    int deleteSchedule(Integer eventNo);
}

