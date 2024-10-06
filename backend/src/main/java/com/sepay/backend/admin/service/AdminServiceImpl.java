package com.sepay.backend.admin.service;

import com.sepay.backend.admin.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final AdminMapper mapper;

    @Override
    public Map<String, Object> getUserStatistics() {
        return mapper.selectUserStatistics(false);
    }

}
