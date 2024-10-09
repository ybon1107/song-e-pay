package com.sepay.backend.schedule.controller;

import com.sepay.backend.schedule.dto.ScheduleDTO;
import com.sepay.backend.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @ResponseBody
    @PostMapping("/add")
    public String addSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        scheduleService.saveSchedule(scheduleDTO);
        return "Success";
    }
    @ResponseBody
    @GetMapping("/list")
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> schedules = scheduleService.getAllSchedules();
        log.info("Fetched Schedules: {}", schedules);
        return scheduleService.getAllSchedules();

    }

    @PostMapping("/update/{eventNo}")
    public ScheduleDTO updateSchedule(@PathVariable Integer eventNo, @RequestBody ScheduleDTO scheduleDTO) {
        scheduleDTO.setEventNo(eventNo);  // 경로 변수로 받은 eventNo 설정
        ScheduleDTO updatedSchedule = scheduleService.updateSchedule(scheduleDTO);
        log.info("Updated Schedule: {}", updatedSchedule); // 업데이트된 데이터 로그 출력
        return updatedSchedule; // 업데이트된 데이터 반환
    }

    @ResponseBody
    @DeleteMapping("/delete/{eventNo}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Integer eventNo) {
        scheduleService.deleteSchedule(eventNo);
        log.info("Deleted Schedule with eventNo: {}", eventNo);
        return ResponseEntity.ok("Schedule deleted successfully");
    }
}