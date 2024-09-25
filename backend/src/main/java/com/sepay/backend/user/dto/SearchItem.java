package com.sepay.backend.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchItem {
    Integer userNo;
    String songNo;
    String krwNo;
    Date beginDate;
    Date endDate;

}
