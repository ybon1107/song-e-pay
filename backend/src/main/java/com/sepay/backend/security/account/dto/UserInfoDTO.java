package com.sepay.backend.security.account.dto;

import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    String username;
    String password;
    List<String> roles;

    public static UserInfoDTO of(UserVO user){
        return new UserInfoDTO(user.getUsername(), user.getPassword(),
                user.getAuthList().stream()
                        .map(a -> a.getAuth())
                        .toList()
        );
    }
}
