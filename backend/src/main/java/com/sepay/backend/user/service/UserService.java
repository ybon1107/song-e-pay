package com.sepay.backend.user.service;

import com.sepay.backend.payment.dto.PasswordDTO;
import com.sepay.backend.user.dto.UserDTO;

public interface UserService {
    UserDTO getInfo(Integer userNo);

    boolean checkSecondaryPassword(PasswordDTO passwordDTO);

    UserDTO login(String userId, String password);

    int register(UserDTO userDTO);
}
