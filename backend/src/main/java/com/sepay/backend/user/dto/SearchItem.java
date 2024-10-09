package com.sepay.backend.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchItem {
    String userId;
    Integer userNo;
    String songNo;
    String krwNo;
    String historyContent;
    List<Integer> typeCode;  // 배열을 처리할 수 있도록 List<Integer>로 변경
    Integer stateCode;
    Date beginDate;
    Date endDate;
    int page;
    int amount;

    private String sortOrder;  // 정렬 기준 필드 추가
    // 기타 필드들

    // Getters and Setters
    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}