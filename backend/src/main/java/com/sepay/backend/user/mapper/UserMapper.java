package com.sepay.backend.user.mapper;


import com.sepay.backend.user.dto.UserDTO;

import java.util.Map;

public interface UserMapper {
    UserDTO selectUserByUserNo(Integer userNo);

    String getSecondaryPassword(int userNo);

    UserDTO selectUser(Map map);
}
