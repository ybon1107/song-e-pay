package com.sepay.backend.eventSchedule.mapper;

import com.sepay.backend.eventSchedule.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleMapper {

    // 일정 저장
    void saveSchedule(ScheduleDTO scheduleDTO);

    // 일정 전부 가져오기
    List<ScheduleDTO> selectByUserId(Map map);

    // 일정 업데이트
    int updateSchedule(ScheduleDTO scheduleDTO);

    // 일정 삭제
    int deleteSchedule(Integer eventNo);
}
