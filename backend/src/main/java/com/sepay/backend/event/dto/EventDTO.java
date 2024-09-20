package com.sepay.backend.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    private Integer eventNo;
    private Integer userNo;
    private Date beginDate;
    private Date endDate;
    private String title;
    private String todo;
    private String color;
    private Date createAt;
    private Date updateAt;
}
