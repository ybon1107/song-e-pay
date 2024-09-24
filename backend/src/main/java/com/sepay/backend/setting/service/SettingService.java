package com.sepay.backend.setting.service;

import com.sepay.backend.user.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface SettingService {
    UserDTO modifyUser(UserDTO user);

    int addAccount(String accountNo, Integer userNo);

    int cancelAccount(Integer userNo);

    int changePassword(String password, Integer userNo);

    int modifySecondPassword(String secondPwd, Integer userNo);

    int deleteSonge(String songNo);

    int deleteKrw(String krwNo);

    int deleteUser(Integer userNo);

    String updateProfileImage(MultipartFile profileImg);
}
