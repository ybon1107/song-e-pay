package com.sepay.backend.security.account.mapper;

import com.sepay.backend.security.account.domain.AuthVO;
import com.sepay.backend.security.account.domain.UserVO;

import java.util.List;

public interface UserDetailsMapper {
    public UserVO get(String username);
    String checkUserStatus(String username);
}
