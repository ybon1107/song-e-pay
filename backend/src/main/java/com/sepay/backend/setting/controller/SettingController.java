package com.sepay.backend.setting.controller;


import com.sepay.backend.S3.service.S3Service;
import com.sepay.backend.setting.service.SettingService;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private S3Service s3Service;

    @PostMapping("")
    public ResponseEntity<?> updateUser(UserDTO userDTO) {
        System.out.println("userDTO: " + userDTO);

        try {
            if (userDTO.getProfilePicFile() != null) {
                String fileUrl = settingService.updateProfileImage(userDTO.getUserId(), userDTO.getProfilePicFile());
                System.out.println("fileUrl: " + fileUrl);
                userDTO.setProfilePic(fileUrl);
            }
            settingService.modifyUser(userDTO);
            return ResponseEntity.ok(userService.getUserByEmail(userDTO.getUserId()));
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 계좌 등록
    @PatchMapping("/register-account")
    public ResponseEntity<?> registerAccount(String accountNo, String userId) {
        return ResponseEntity.ok(settingService.addAccount(accountNo, userId));
    }

    // 계좌 해지
    @PatchMapping("/close-account")
    public ResponseEntity<?> closeAccount(String accountNo, String userId) {
        return ResponseEntity.ok(settingService.cancelAccount(userId));
    }

    // 비밀번호 변경
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> requestData) throws Exception {
        String currentPassword = (String) requestData.get("currentPw");
        String newPw = (String) requestData.get("newPw");
        String userId = (String) requestData.get("userId");

        String encodedNewPw = passwordEncoder().encode(newPw);
        String dbPassword = settingService.checkPassword(userId);

    if (!passwordEncoder().matches(currentPassword, dbPassword)) {
        return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다");
    }

        // 비밀번호 변경 작업 수행
        return ResponseEntity.ok(settingService.changePassword(encodedNewPw, userId));
    }


    // 2차 비밀번호 변경
    @PatchMapping("/change-secpwd")
    public ResponseEntity<?> modifySecondPassword(String secondPwd, String userId) {
        return ResponseEntity.ok(settingService.modifySecondPassword(secondPwd, userId));
    }

    // 회원 탈퇴
    @DeleteMapping("/bye/{userId}")
    public ResponseEntity<?> bye(@PathVariable String userId) {
        log.info("userId : ", userId);
        return ResponseEntity.ok(settingService.deleteUser(userId));
    }

}
