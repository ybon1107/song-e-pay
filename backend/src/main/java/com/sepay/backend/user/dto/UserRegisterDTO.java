package com.sepay.backend.user.dto;

import com.sepay.backend.security.account.domain.UserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDTO {
    String userId; // email = user_id
    String password;
    String firstName;
    String lastName;
    Integer countryCode;
    String birthday;
    Character gender;
    String phoneNo;

//    MultipartFile profilePicFile;

    public UserVO toVO() {
        return UserVO.builder()
                .username(userId)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .countryCode(countryCode)
                .birthday(birthday)
                .gender(gender)
                .phoneNo(phoneNo)
                .build();
    }
}
