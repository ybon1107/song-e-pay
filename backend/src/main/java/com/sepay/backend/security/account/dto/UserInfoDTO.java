package com.sepay.backend.security.account.dto;

import com.sepay.backend.security.account.domain.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    String userId;
    Integer countryCode;
    List<String> roles;

    public static UserInfoDTO of(UserVO user){
        return new UserInfoDTO(user.getUsername(),user.getCountryCode(),
                user.getAuthList().stream()
                        .map(a -> a.getAuth())
                        .toList()
        );
    }
}
