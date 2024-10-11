package com.sepay.backend.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryDTO {
    private String userId;
    private Integer historyNo;
    private Integer userNo;
    private String songNo;
    private String krwNo;
    private Integer typeCode;
    private Integer stateCode;
    private Date historyDate;
    private String historyContent;
    private Double amount;
    private Double exchangeRate;
    private String memo;
}