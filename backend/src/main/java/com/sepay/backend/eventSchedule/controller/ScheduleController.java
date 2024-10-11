package com.sepay.backend.eventSchedule.controller;

import com.sepay.backend.eventSchedule.dto.ScheduleDTO;
import com.sepay.backend.eventSchedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping("/list/{userId}")
    public List<ScheduleDTO> getAllSchedules(
            @PathVariable String userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        log.info("Fetching schedules for userId: {}, year: {}, month: {}", userId, year, month);
        List<ScheduleDTO> schedules = scheduleService.getAllSchedules(userId, year, month);
        log.info("Fetched Schedules: {}", schedules);
        return schedules;
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