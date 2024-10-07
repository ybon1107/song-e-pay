package com.sepay.backend.user.mapper;


import com.sepay.backend.security.account.domain.AuthVO;
import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.user.dto.UserDTO;

import java.util.Map;

public interface UserMapper {
    UserDTO selectUserByUserNo(Integer userNo);

    String getSecondaryPassword(int userNo);

    UserVO get(String username);

    int insertAuth(AuthVO authVO);

    UserDTO selectUser(Map map);

    // 회원가입
    int insertUser(UserVO userVO);

    UserDTO selectUserByEmail(String email);

    default UserDTO toUserDTO(UserVO userVO) {
        if (userVO == null) {
            return null;
        }

        return UserDTO.builder()
                .userNo(userVO.getUserNo())
                .accountNo(userVO.getAccountNo())
                .songNo(userVO.getSongNo())
                .krwNo(userVO.getKrwNo())
                .countryCode(userVO.getCountryCode())
                .userId(userVO.getUserId())
                .password(userVO.getPassword())
                .firstName(userVO.getFirstName())
                .lastName(userVO.getLastName())
                .birthday(userVO.getBirthday())
                .gender(userVO.getGender())
                .phoneNo(userVO.getPhoneNo())
                .secondPwd(userVO.getSecondPwd())
                .profilePic(userVO.getProfilePic())
                .address(userVO.getAddress())
                .postCode(userVO.getPostCode())
                .stateCode(userVO.getStateCode())
                .createAt(userVO.getCreateAt())
                .updateAt(userVO.getUpdateAt())
                .authList(userVO.getAuthList()
                        .stream()
                        .map(AuthVO::getAuth)
                        .toList())
                .build();
    }
}
