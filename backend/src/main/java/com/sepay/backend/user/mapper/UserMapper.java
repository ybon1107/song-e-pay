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

    UserDTO getUserInfo(String userId);

    UserDTO selectUser(Map map);

<<<<<<< HEAD
    // 회원가입
    int insertUser(UserVO userVO);

    // 이메일로 유저 정보
    UserDTO selectUserByEmail(String email);
    
    int checkEmail(String email);

    // 자동환전에서 사용
    @Select("SELECT song_no, krw_no FROM user WHERE user_no = #{userNo}")
    Map<String, String> selectUserAccounts(Integer userNo);
<<<<<<< HEAD
=======
    int insertUser(UserDTO userDTO);

    UserDTO selectUserByEmail(String email);
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
=======

    int checkNumberExists(String number);

    void insertSongAccount(UserVO userVO);
    void insertKrwAccount(UserVO userVO);
>>>>>>> cdb2a9f9b122d2d9458257fb1c094a60015059f1
}
