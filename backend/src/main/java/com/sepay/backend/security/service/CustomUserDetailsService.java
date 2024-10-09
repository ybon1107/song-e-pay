package com.sepay.backend.security.service;

import com.sepay.backend.security.account.domain.CustomUser;
import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.security.account.mapper.UserDetailsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO userVO = userDetailsMapper.get(username);
        if (userVO == null) {
            log.error("User with username: " + username + " not found");
            throw new UsernameNotFoundException("User not found");
        }
        log.debug("UserVO loaded: {}", userVO);
       log.debug("AuthList: {}", userVO.getAuthList());
        return new CustomUser(userVO);
    }
}
