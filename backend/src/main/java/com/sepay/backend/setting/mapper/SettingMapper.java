package com.sepay.backend.setting.mapper;

import com.sepay.backend.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

public interface SettingMapper {

    UserDTO updateUser(UserDTO user);

    int updateAccount(Map map);

    int deleteAccount(int userNo);

    int updatePassword(int password, int userNo);

    int updateSecondPassword(int secondPwd, int userNo);

    int deleteSonge(String songNo);

    int deleteKrw(String krwNo);

    int deleteUser(int userNo);
}
