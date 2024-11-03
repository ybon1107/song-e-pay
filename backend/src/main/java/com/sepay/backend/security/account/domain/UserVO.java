package com.sepay.backend.security.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO {
    private Integer userNo;
    private String accountNo;
    private String songNo;
    private String krwNo;
    private Integer countryCode;
    private String username; // = userId = email = user_id
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

    private List<AuthVO> authList; // user_auth 테이블과 JOIN 필요
}



