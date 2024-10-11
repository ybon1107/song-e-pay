package com.sepay.backend.security.service;

import com.sepay.backend.security.account.domain.CustomUser;
import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.security.account.mapper.UserDetailsMapper;

<<<<<<< HEAD
import com.sepay.backend.security.exception.WithdrawnUserException;
=======
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
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
<<<<<<< HEAD

        UserVO userVO = userDetailsMapper.get(username);
        if (userVO == null) {
            log.error("User with userId: " + username + " not found");
            throw new UsernameNotFoundException("가입한 회원정보가 없습니다");
        } else {
            String userState = userDetailsMapper.checkUserStatus(username);
            if (userState.equals("10")) {
                throw new WithdrawnUserException("탈퇴한 회원입니다.");
            }
        }

        log.debug("UserVO loaded: {}", userVO);
       log.debug("AuthList: {}", userVO.getAuthList());
=======
        UserVO userVO = userDetailsMapper.get(username);
        if (userVO == null) {
            log.error("User with username: " + username + " not found");
            throw new UsernameNotFoundException("User not found");
        }
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
        return new CustomUser(userVO);
    }
}
