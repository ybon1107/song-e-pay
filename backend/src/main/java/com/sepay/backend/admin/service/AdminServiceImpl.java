package com.sepay.backend.admin.service;

import com.sepay.backend.admin.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final AdminMapper mapper;

    @Override
    public Map<String, Object> getStatistics() {
        // 최종 JSON 형식을 위한 맵 구성
        Map<String, Object> response = new HashMap<>();

        // 성별 통계를 가져옴
        List<Map<String, Object>> genderStats = mapper.selectUserGender();
        List<Map<String, Object>> AgeStats = mapper.selectUserAge();
        List<Map<String, Object>> AgeCountryStats = mapper.selectUserAgeByCountry();


        // 성별 관련 데이터를 담을 Map 생성
        Map<String, Object> ageData = new HashMap<>();
        ageData.put("all", AgeStats); // 국가별 성별 통계
        ageData.put("countries", AgeCountryStats); // 국가별 성별 통계

        response.put("gender", genderStats);
        response.put("age", ageData);

        return response; // 최종 응답 반환
    }

//    @Override
//    public List<Map<String, Object>> getUserStatisticsByCountry() {
//        return mapper.selectUserStatisticsByCountry();
//    }

}
