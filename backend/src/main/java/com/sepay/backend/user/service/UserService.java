package com.sepay.backend.user.service;

import com.sepay.backend.payment.dto.PasswordDTO;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.dto.UserRegisterDTO;

<<<<<<< HEAD
import java.util.Map;

=======
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
public interface UserService {

    String selectSecondPwd(String userId);
    
    boolean checkSecondaryPassword(PasswordDTO passwordDTO);

<<<<<<< HEAD
    UserDTO get(String username);

//    UserDTO login(String userId, String password);

    UserDTO register(UserRegisterDTO userRegisterDTO);

    boolean isEmailRegistered(String email);

    UserDTO getUserByEmail(String userId);

    // 자동환전에서 사용
    Map<String, String> getUserAccounts(Integer userNo);

=======
    UserDTO getUserId(String userId);

    UserDTO login(String userId, String password);

    int register(UserDTO userDTO);

    boolean isEmailRegistered(String email);
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
}
