package com.sepay.backend.setting.controller;


import com.sepay.backend.S3.service.S3Service;
import com.sepay.backend.setting.service.SettingService;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    final UserService userService;

    @Autowired
    private S3Service s3Service;

    @PostMapping("")
    public ResponseEntity<?> updateUser(UserDTO userDTO) {
        System.out.println("userDTO: " + userDTO);

        try {
            if (userDTO.getProfilePicFile() != null) {
                String fileUrl = settingService.updateProfileImage(userDTO.getUserNo(), userDTO.getProfilePicFile());
                System.out.println("fileUrl: " + fileUrl);
                userDTO.setProfilePic(fileUrl);
            }
            settingService.modifyUser(userDTO);
            return ResponseEntity.ok(userService.getInfo(userDTO.getUserNo()));
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
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
    public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> requestData) throws Exception {
        String currentPassword = (String) requestData.get("currentPw");
        String newPw = (String) requestData.get("newPw");
        Integer userNo = (Integer) requestData.get("userNo");

        log.info("settingService.checkPassword(userNo, currentPassword) : " + settingService.checkPassword(userNo, currentPassword));
        if (!settingService.checkPassword(userNo, currentPassword)) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다");
        }

        // 비밀번호 변경 작업 수행
        return ResponseEntity.ok(settingService.changePassword(newPw, userNo));
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
