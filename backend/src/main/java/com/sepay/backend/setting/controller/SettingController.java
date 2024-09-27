package com.sepay.backend.setting.controller;


import com.sepay.backend.S3.service.S3Service;
import com.sepay.backend.setting.service.SettingService;
import com.sepay.backend.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/setting")
public class SettingController {

    final SettingService settingService;

    @Autowired
    private S3Service s3Service;

    // 설정 저장
    @PatchMapping("")
    public ResponseEntity<UserDTO> updateUser(@RequestParam MultipartFile profileImg , UserDTO userDTO) {
        if (profileImg != null) {
            String fileUrl = settingService.updateProfileImage(profileImg);
            userDTO.setProfilePic(fileUrl);
        }
        return ResponseEntity.ok(settingService.modifyUser(userDTO));
    }

    // 계좌 등록
    @PatchMapping("/register-account")
    public ResponseEntity<?> registerAccount(String accountNo, Integer userNo) {
        return ResponseEntity.ok(settingService.addAccount(accountNo, userNo));
    }

    // 계좌 해지
    @PatchMapping("/close-account")
    public ResponseEntity<?> closeAccount(String accountNo, Integer userNo) {
        return ResponseEntity.ok(settingService.cancelAccount(userNo));
    }

    // 비밀번호 변경
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> requestData) {
        String password = (String) requestData.get("password");
        Integer userNo = (Integer) requestData.get("userNo");
        return ResponseEntity.ok(settingService.changePassword(password, userNo));
    }

    // 2차 비밀번호 변경
    @PatchMapping("/change-secpwd")
    public ResponseEntity<?> modifySecondPassword(String secondPwd, Integer userNo) {
        return ResponseEntity.ok(settingService.modifySecondPassword(secondPwd, userNo));
    }

    // 회원 탈퇴
    @DeleteMapping("/bye/{userNo}")
    public ResponseEntity<?> bye(@PathVariable Integer userNo) {
        log.info("userNo : ", userNo);
        return ResponseEntity.ok(settingService.deleteUser(userNo));
    }
    
}
