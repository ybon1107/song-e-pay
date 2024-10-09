package com.sepay.backend.security.account.mapper;

import com.sepay.backend.security.account.domain.UserVO;

public interface UserDetailsMapper {
    public UserVO get(String username);
}
