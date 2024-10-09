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
    public int addAccount(String accountNo, Integer userNo) {
        HashMap map = new HashMap();
        map.put("accountNo", accountNo);
        map.put("userNo", userNo);
        return mapper.updateAccount(map);
    }

    // 계좌 해지
    @Override
    public int cancelAccount(Integer userNo) {
        return mapper.deleteAccount(userNo);
    }

    // 비밀번호 변경
    @Override
    public int changePassword(String newPw, Integer userNo) {
        HashMap map = new HashMap();
        map.put("newPw", newPw);
        map.put("userNo", userNo);
        return mapper.updatePassword(map);
    }

    @Override
    public boolean checkPassword(int useNo, String currentPw) {
        return currentPw.equals(mapper.selectPassword(useNo));
    }

    // 2차 비밀번호 변경
    @Override
    public int modifySecondPassword(String secondPwd, Integer userNo) {
        HashMap map = new HashMap();
        map.put("secondPwd", secondPwd);
        map.put("userNo", userNo);
        return mapper.updateSecondPassword(map);
    }

    @Override
    public boolean checkSecondPassword(int useNo, String secondPwd) {
        return secondPwd.equals(mapper.selectSecondPassword(useNo));
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
    public int deleteUser(Integer userNo) {
        UserDTO user = userMapper.selectUserByUserNo(userNo);
        mapper.deleteKrw(user.getKrwNo());
        mapper.deleteSonge(user.getSongNo());
        return mapper.deleteUser(userNo);
    }

    // 프로필 이미지 업로드
    @Override
    public String updateProfileImage(Integer userNo, MultipartFile profileImg) {
        String fileName = mapper.selectUserProfileImg(userNo).replace(bucketUrl, "");
        System.out.println("fileName = " + fileName);
        if (!fileName.equals("profile/default.jpg")) {
            s3Service.deleteFile(fileName);
        }
        return s3Service.uploadFile(profileImg, profileImg.getOriginalFilename());
    }

    // 프로필 이미지 주소 가져오기
    @Override
    public String getProfileImage(Integer userNo) {
        return mapper.selectUserProfileImg(userNo);
    }
}
