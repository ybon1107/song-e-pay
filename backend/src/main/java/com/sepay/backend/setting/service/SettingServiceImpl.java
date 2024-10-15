package com.sepay.backend.setting.service;

import com.sepay.backend.S3.service.S3Service;
import com.sepay.backend.setting.mapper.SettingMapper;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    final SettingMapper mapper;
    final UserMapper userMapper;
    final S3Service s3Service;

    @Value("${aws.s3.bucket.url}")
    private String bucketUrl;

    // 유저 정보 변경
    @Override
    public int modifyUser(UserDTO user) {
        return mapper.updateUser(user);
    }

    // 계좌 등록
    @Override
    public int addAccount(String accountNo, String accountPwd, String userId) {
        if(accountPwd.equals(mapper.selectAccountPwd(accountNo))){
            HashMap map = new HashMap();
            map.put("accountNo", accountNo);
            map.put("userId", userId);
            return mapper.updateAccount(map);
        }
        return 0;
    }

    // 계좌 해지
    @Override
    public int cancelAccount(String userId) {
        return mapper.deleteAccount(userId);
    }

    // 비밀번호 변경
    @Override
    public int changePassword(String newPw, String userId) {
        HashMap map = new HashMap();
        map.put("newPw", newPw);
        map.put("userId", userId);
        return mapper.updatePassword(map);
    }

    @Override
    public String checkPassword(String userId) {
        return mapper.selectPassword(userId);
    }

    // 2차 비밀번호 변경
    @Override
    public int modifySecondPassword(String secondPwd, String userId) {
        HashMap map = new HashMap();
        map.put("secondPwd", secondPwd);
        map.put("userId", userId);
        return mapper.updateSecondPassword(map);
    }

    @Override
    public boolean checkSecondPassword(String userId, String secondPwd) {
        return secondPwd.equals(mapper.selectSecondPassword(userId));
    }

    // 송이 계좌 삭제
    @Override
    public int deleteSonge(String songNo) {
        return mapper.deleteSonge(songNo);
    }

    // 원화 계좌 삭제
    @Override
    public int deleteKrw(String krwNo) {
        return mapper.deleteKrw(krwNo);
    }

    // 유저 삭제
    @Override
    public int deleteUser(String userId) {
//        UserDTO user = userMapper.selectUserByEmail(userId);
//        mapper.deleteKrw(user.getKrwNo());
//        mapper.deleteSonge(user.getSongNo());
        return mapper.deleteUser(userId);
    }

    // 프로필 이미지 업로드
    @Override
    public String updateProfileImage(String userId, MultipartFile profileImg) {
        String fileName = mapper.selectUserProfileImg(userId).replace(bucketUrl, "");
        System.out.println("fileName = " + fileName);
        if (!fileName.equals("profile/default.jpg")) {
            s3Service.deleteFile(fileName);
        }
        return s3Service.uploadFile(profileImg, profileImg.getOriginalFilename());
    }

    // 프로필 이미지 주소 가져오기
    @Override
    public String getProfileImage(String userId) {
        return mapper.selectUserProfileImg(userId);
    }
}
