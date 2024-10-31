package com.sepay.backend.user.service;

import com.sepay.backend.security.account.domain.AuthVO;
import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.setting.mapper.SettingMapper;
import com.sepay.backend.user.dto.UserDTO;
import com.sepay.backend.user.dto.UserRegisterDTO;
import com.sepay.backend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper mapper;
    final PasswordEncoder passwordEncoder;
    private final SettingMapper settingMapper;

    @Override
    public String selectSecondPwd(String userId){
        return settingMapper.selectSecondPassword(userId);
    }

    @Override
    public UserDTO get(String username) {
        UserVO userVO = Optional.ofNullable(mapper.get(username)).orElseThrow(NoSuchElementException::new);
        return UserDTO.of(userVO);
    }

//    @Override
//    public UserDTO login(String userId, String password) {
//        log.info("Login request for user: {}", userId);
//        // DB에서 가져온 사용자 Password
//         String dbPassword = mapper.getPassword(userId);
//         log.info("DB Password: " + dbPassword);
//
//         if (passwordEncoder.matches(password, dbPassword)) {
//             log.info("Login success for user: {}", userId);
//             log.info("User: {}", UserDTO.of(mapper.get(userId)));
//             return UserDTO.of(mapper.get(userId));
//         } else {
//             return null;
//         }
//
//
////        HashMap map = new HashMap();
////        map.put("userId", userId);
////        map.put("password", password);
////        return mapper.selectUser(map);
//    }

@Transactional
@Override
public UserDTO register(UserRegisterDTO userRegisterDTO) {
    try {
        UserVO userVO = userRegisterDTO.toVO();
        userVO.setPassword(passwordEncoder.encode(userVO.getPassword()));

        String id = userVO.getUsername().split("@")[0];

        // 계좌 번호 생성
        userVO.setSongNo("song_"+id);
        userVO.setKrwNo("krw_"+id);

        // 사용자 정보, song_account, krw_account 모두 한 번에 삽입
        mapper.insertUser(userVO);
        mapper.insertSongAccount(userVO);
        mapper.insertKrwAccount(userVO);

        AuthVO authVO = new AuthVO();
        authVO.setUsername(userVO.getUsername());
        authVO.setAuth("ROLE_USER");
        mapper.insertAuth(authVO);

        log.info("userVO: " + userVO);
        log.info("authVO: " + authVO);

        return get(userVO.getUsername());
    } catch (Exception e) {
        log.error("Error registering user: ", e);
        throw new RuntimeException("Registration failed");
    }
}

    // 이메일 중복 확인 메서드 추가
    @Override
    public boolean isEmailRegistered(String userId) {
        UserDTO user = mapper.selectUserByEmail(userId);
        return user != null;
    }

    @Override
    public UserDTO getUserByEmail(String userId) {
        return mapper.selectUserByEmail(userId);
    }
}
