package com.sepay.backend.security.account.dto;

import com.sepay.backend.security.account.domain.UserVO;
<<<<<<< HEAD
=======
import com.sepay.backend.user.dto.UserDTO;
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
<<<<<<< HEAD
    String userId;
    Integer countryCode;
    List<String> roles;

    public static UserInfoDTO of(UserVO user){
        return new UserInfoDTO(user.getUsername(),user.getCountryCode(),
=======
    String username;
    String password;
    List<String> roles;

    public static UserInfoDTO of(UserVO user){
        return new UserInfoDTO(user.getUsername(), user.getPassword(),
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
                user.getAuthList().stream()
                        .map(a -> a.getAuth())
                        .toList()
        );
    }
}
