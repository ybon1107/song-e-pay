package com.sepay.backend.user.service;

import com.sepay.backend.payment.dto.PasswordDTO;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.dto.UserRegisterDTO;

import java.util.Map;

public interface UserService {
    UserDTO getInfo(Integer userNo);

    boolean checkSecondaryPassword(PasswordDTO passwordDTO);

    UserDTO get(String username);

//    UserDTO login(String userId, String password);

    UserDTO register(UserRegisterDTO userRegisterDTO);

    boolean isEmailRegistered(String email);

<<<<<<< HEAD
=======
    // 자동환전에서 사용
    Map<String, String> getUserAccounts(Integer userNo);

>>>>>>> LBY
    String getUserImg(String userId);
}
