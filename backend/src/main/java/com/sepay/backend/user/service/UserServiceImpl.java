package com.sepay.backend.user.service;

import com.sepay.backend.payment.dto.PasswordDTO;
import com.sepay.backend.security.account.domain.AuthVO;
import com.sepay.backend.security.account.domain.UserVO;
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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper mapper;
    final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getInfo(Integer userNo) {
        return mapper.selectUserByUserNo(userNo);
    }

    @Override
    public boolean checkSecondaryPassword(PasswordDTO passwordDTO) {
        //추후에 userNo  동적 변경
        String userPassword = mapper.getSecondaryPassword(1);

        if(passwordDTO.getPassword().equals(userPassword)){
            return true;
        }
        else{
            return false;
        }
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
            // 추후 password 암호화 추가(security & JWT)
            UserVO userVO = userRegisterDTO.toVO();
            userVO.setPassword(passwordEncoder.encode(userVO.getPassword()));
            mapper.insertUser(userVO);

            AuthVO authVO = new AuthVO();
            authVO.setUsername(userVO.getUsername());
            authVO.setAuth("ROLE_USER");
            mapper.insertAuth(authVO);

            log.info("userVO: " + userVO);
            log.info("authVO: " + authVO);

            return get(userVO.getUsername());
//            return mapper.insertUser(userDTO);
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

    // 자동환전에서 사용
    @Override
    public Map<String, String> getUserAccounts(Integer userNo) {
        return mapper.selectUserAccounts(userNo);
    }

    @Override
    public String getUserImg(String userId) {
        return mapper.selectUserImg(userId);
    }
}
