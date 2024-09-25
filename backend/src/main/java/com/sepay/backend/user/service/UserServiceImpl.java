package com.sepay.backend.user.service;

import com.sepay.backend.payment.dto.PasswordDTO;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper mapper;

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
    public UserDTO login(String userId, String password) {
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("password", password);
        return mapper.selectUser(map);
    }
}
