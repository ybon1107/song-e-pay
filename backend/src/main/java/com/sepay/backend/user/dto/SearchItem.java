package com.sepay.backend.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchItem {
    Integer userNo;
    String songNo;
    String krwNo;
    String historyContent;
    List<Integer> typeCode;  // 배열을 처리할 수 있도록 List<Integer>로 변경
    Integer stateCode;
    Date beginDate;
    Date endDate;
}