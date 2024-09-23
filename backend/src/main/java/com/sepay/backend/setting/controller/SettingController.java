package com.sepay.backend.setting.controller;


import com.sepay.backend.setting.service.SettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/setting")
public class SettingController {
    private final SettingService settingService;

    @PatchMapping("/add")
    public ResponseEntity<?> select(@RequestBody String accountNo, Integer userNo) {
        log.info("accountNo : {}", accountNo);
        log.info("userNo : {}", userNo);
        return ResponseEntity.ok(settingService.addAccount(accountNo, userNo));
    }

}
