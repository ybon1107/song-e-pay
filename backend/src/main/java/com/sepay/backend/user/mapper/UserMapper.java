package com.sepay.backend.user.mapper;


import com.sepay.backend.user.dto.UserDTO;

public interface UserMapper {
    UserDTO selectUser(Integer userNo);

    String getPassword(int userNo);
}
