package com.sepay.backend.setting.mapper;

import com.sepay.backend.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

public interface SettingMapper {

    // 정보 변경
    int updateUser(UserDTO user);

    // 계좌 등록
    int updateAccount(Map map);

    // 계좌 해지
    int deleteAccount(String userId);

    // 비밀번호 변경
    int updatePassword(Map map);

    // 비밀번호 확인
    String selectPassword(String userId);

    // 2차 비밀번호 변경
    int updateSecondPassword(Map map);

    // 2차 비밀번호 확인
    String selectSecondPassword(String userId);

    // 송이 계좌 삭제
    int deleteSonge(String songNo);

    // 원화 계좌 삭제
    int deleteKrw(String krwNo);

    // 회원 삭제
    int deleteUser(String userId);

    // 프로필 이미지 확인
    String selectUserProfileImg(String userId);

    String selectAccountPwd(String accountNo);
}
