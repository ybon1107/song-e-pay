package com.sepay.backend.user.service;

import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.dto.UserRegisterDTO;

import java.util.Map;

public interface UserService {

    String selectSecondPwd(String userId);
    

    UserDTO get(String username);

//    UserDTO login(String userId, String password);

    UserDTO register(UserRegisterDTO userRegisterDTO);

    boolean isEmailRegistered(String email);

    UserDTO getUserByEmail(String userId);
}
