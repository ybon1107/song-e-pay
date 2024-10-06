package com.sepay.backend.admin.mapper;


import java.util.Map;

public interface AdminMapper {
    // 전체 사용자 수와 성별, 연령대별 통계 가져오는 메서드
    Map<String, Object> selectUserStatistics(Boolean countryCode);
}
