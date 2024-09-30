package com.sepay.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date createAt;
    private Date updateAt;
}
