package com.sepay.backend.security.account.mapper;

<<<<<<< HEAD
import com.sepay.backend.security.account.domain.AuthVO;
import com.sepay.backend.security.account.domain.UserVO;

import java.util.List;

public interface UserDetailsMapper {
    public UserVO get(String username);
    String checkUserStatus(String username);
=======
import com.sepay.backend.security.account.domain.UserVO;

public interface UserDetailsMapper {
    public UserVO get(String username);
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
}
