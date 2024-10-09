package com.sepay.backend.admin.controller;

import com.sepay.backend.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    final AdminService adminService;

    @GetMapping("/statistics")
    public Map<String, Object> statistics(){
        return adminService.getStatistics();
//        try{
//            Map<String, Object> aa= adminService.getUserStatistics();
//            System.out.println(aa);
//
//        } catch (Exception e) {
//            log.error("Error analyze user: {}", e);
//        }
//        return aa;
    }
}
