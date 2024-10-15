package com.sepay.backend.setting.service;

import com.sepay.backend.user.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface SettingService {
    int modifyUser(UserDTO user);

    int addAccount(String accountNo,String accountPwd, String userId);

    int cancelAccount(String userId);

    int changePassword(String newPw, String userId);

    String checkPassword(String userId);

    int modifySecondPassword(String secondPwd, String userId);

    boolean checkSecondPassword(String userId, String secondPwd);

    int deleteSonge(String songNo);

    int deleteKrw(String krwNo);

    int deleteUser(String userId);

    String updateProfileImage(String userId, MultipartFile profileImg);

    String getProfileImage(String userId);
}
