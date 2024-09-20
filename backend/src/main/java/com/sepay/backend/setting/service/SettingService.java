package com.sepay.backend.setting.service;

import com.sepay.backend.user.dto.UserDTO;

public interface SettingService {
    UserDTO modifyUser(UserDTO user);

    int addAccount(String accountNo, Integer userNo);

    int cancelAccount(int userNo);

    int changePassword(int password, int userNo);

    int modifySecondPassword(int secondPwd, int userNo);

    int deleteSonge(String songNo);

    int deleteKrw(String krwNo);

    int deleteUser(int userNo);
}
