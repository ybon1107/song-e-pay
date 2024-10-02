package com.sepay.backend.setting.service;

import com.sepay.backend.user.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface SettingService {
    int modifyUser(UserDTO user);

    int addAccount(String accountNo, Integer userNo);

    int cancelAccount(Integer userNo);

    int changePassword(String newPw, Integer userNo);

    boolean checkPassword(int useNo, String currentPw);

    int modifySecondPassword(String secondPwd, Integer userNo);

    boolean checkSecondPassword(int useNo, String secondPwd);

    int deleteSonge(String songNo);

    int deleteKrw(String krwNo);

    int deleteUser(Integer userNo);

    String updateProfileImage(Integer userNo, MultipartFile profileImg);

    String getProfileImage(Integer userNo);
}
