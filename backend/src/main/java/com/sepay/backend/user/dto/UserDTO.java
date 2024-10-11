package com.sepay.backend.user.dto;

import com.sepay.backend.security.account.domain.AuthVO;
import com.sepay.backend.security.account.domain.UserVO;
import com.sepay.backend.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer userNo;
    private String accountNo;
    private String songNo;
    private String krwNo;
    private Integer countryCode;
    private String userId;
    private String password;
    private String firstName;
    private String lastName;
    private String birthday;
    private Character gender;
    private String phoneNo;
    private String secondPwd;
    private String profilePic;
    private String address;
    private String postCode;
    private String stateCode;
    private Date createdAt;
    private Date updatedAt;

    MultipartFile profilePicFile;

    private List<String> authList;

    // UserDTO -> UserVO
    public UserVO toVO() {
        return UserVO.builder()

                .userNo(userNo)
                .accountNo(accountNo)
                .songNo(songNo)
                .krwNo(krwNo)
                .countryCode(countryCode)
                .username(userId)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .gender(gender)
                .phoneNo(phoneNo)
                .secondPwd(secondPwd)
                .profilePic(profilePic)
                .address(address)
                .postCode(postCode)
                .stateCode(stateCode)
                .build();
    }

    // UserVO -> UserDTO
    public static UserDTO of(UserVO vo) {
        return UserDTO.builder()
                .userNo(vo.getUserNo())
                .accountNo(vo.getAccountNo())
                .songNo(vo.getSongNo())
                .krwNo(vo.getKrwNo())
                .countryCode(vo.getCountryCode())
                .userId(vo.getUsername())
                .password(vo.getPassword())
                .firstName(vo.getFirstName())
                .lastName(vo.getLastName())
                .birthday(vo.getBirthday())
                .gender(vo.getGender())
                .phoneNo(vo.getPhoneNo())
                .secondPwd(vo.getSecondPwd())
                .profilePic(vo.getProfilePic())
                .address(vo.getAddress())
                .postCode(vo.getPostCode())
                .stateCode(vo.getStateCode())
                .authList(vo.getAuthList()
                        .stream()
                        .map(a -> a.getAuth())
                        .toList())
                .build();
    }
}
