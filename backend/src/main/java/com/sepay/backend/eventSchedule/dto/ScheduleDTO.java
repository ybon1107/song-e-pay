package com.sepay.backend.eventSchedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDTO {
    private String userId;
    private Integer eventNo;
    private Date beginDate;  // 시작 날짜 (형식: "yyyy-MM-dd HH:mm:ss")
    private Date endDate;    // 종료 날짜 (형식: "yyyy-MM-dd HH:mm:ss")
    private String title;      // 일정 제목
    private String todo;       // 일정 설명
    private String color;
    private Date createdAt;
    private Date updatedAt;

    // Getter
    public Integer getEventNo() {
        return eventNo;
    }

    // Setter
    public void setEventNo(Integer eventNo) {
        this.eventNo = eventNo;
    }

}