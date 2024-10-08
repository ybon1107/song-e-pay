package com.sepay.backend.user.service;

import com.sepay.backend.payment.dto.PasswordDTO;
import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper mapper;
    final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getInfo(Integer userNo) {
        return mapper.selectUserByUserNo(userNo);
    }

    @Override
    public boolean checkSecondaryPassword(PasswordDTO passwordDTO) {
        //추후에 userNo  동적 변경
        String userPassword = mapper.getSecondaryPassword(1);

        if(passwordDTO.getPassword().equals(userPassword)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public UserDTO getUserId(String userId) {
        return mapper.getUserInfo(userId);
    }

    @Override
    public UserDTO login(String userId, String password) {
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("password", password);
        return mapper.selectUser(map);
    }

    @Override
    public int register(UserDTO userDTO) {
        try {
            // 추후 password 암호화 추가(security & JWT)
            return mapper.insertUser(userDTO);
        } catch (Exception e) {
            log.error("Error registering user: ", e);
            throw new RuntimeException("Registration failed");
        }
    }

    // 이메일 중복 확인 메서드 추가
    @Override
    public boolean isEmailRegistered(String userId) {
        UserVO user = mapper.getUserInfo(userId).toVO();
        return user != null ? true : false;
    }

    @Override
    public String getUserImg(String userId) {
        return mapper.selectUserImg(userId);
    }
}
