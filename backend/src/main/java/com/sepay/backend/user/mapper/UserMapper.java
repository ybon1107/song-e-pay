package com.sepay.backend.user.mapper;


import com.sepay.backend.security.account.domain.AuthVO;
import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.user.dto.UserDTO;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface UserMapper {

    String getSecondaryPassword(String userId);

    UserVO get(String username);

    String getPassword(String username);

    int insertAuth(AuthVO authVO);

    UserDTO selectUser(Map map);

    // 회원가입
    int insertUser(UserVO userVO);

    // 이메일로 유저 정보
    UserDTO selectUserByEmail(String email);
    
    int checkEmail(String email);

    // 자동환전에서 사용
    @Select("SELECT song_no, krw_no FROM user WHERE user_no = #{userNo}")
    Map<String, String> selectUserAccounts(Integer userNo);
}
