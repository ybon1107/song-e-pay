package com.sepay.backend.user.mapper;


import com.sepay.backend.security.account.domain.AuthVO;
import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.user.dto.UserDTO;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface UserMapper {

    UserVO get(String username);

    String getPassword(String username);

    int insertAuth(AuthVO authVO);

    UserDTO selectUser(Map map);

    // 회원가입
    int insertUser(UserVO userVO);

    // 이메일로 유저 정보
    UserDTO selectUserByEmail(String email);
    
    int checkEmail(String email);

    int checkNumberExists(String number);

    void insertSongAccount(UserVO userVO);
    void insertKrwAccount(UserVO userVO);
}
