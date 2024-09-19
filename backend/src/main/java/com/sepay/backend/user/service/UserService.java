package com.sepay.backend.user.service;

import com.sepay.backend.user.dto.UserDTO;

public interface UserService {
    UserDTO getInfo(Integer userNo);
}
