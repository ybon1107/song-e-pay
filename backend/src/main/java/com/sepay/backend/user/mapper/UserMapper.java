package com.sepay.backend.user.mapper;


import com.sepay.backend.security.account.domain.AuthVO;
import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.user.dto.UserDTO;

import java.util.Map;

public interface UserMapper {
    UserDTO selectUserByUserNo(Integer userNo);

    String getSecondaryPassword(int userNo);

    UserVO get(String username);

    String getPassword(String username);

    int insertAuth(AuthVO authVO);

    UserDTO selectUser(Map map);

    // 회원가입
    int insertUser(UserVO userVO);

    UserDTO selectUserByEmail(String email);
    
    int checkEmail(String email);
}
