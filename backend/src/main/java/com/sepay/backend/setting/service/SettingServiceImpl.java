package com.sepay.backend.setting.service;

import com.sepay.backend.setting.mapper.SettingMapper;
import com.sepay.backend.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    final SettingMapper mapper;

    @Override
    public UserDTO modifyUser(UserDTO user) {
        return null;
    }

    @Override
    public int addAccount(String accountNo, Integer userNo) {
        HashMap map = new HashMap();
        map.put("accountNo", accountNo);
        map.put("userNo", userNo);
        mapper.updateAccount(map);
        return 0;
    }

    @Override
    public int cancelAccount(int userNo) {
        return 0;
    }

    @Override
    public int changePassword(int password, int userNo) {
        return 0;
    }

    @Override
    public int modifySecondPassword(int secondPwd, int userNo) {
        return 0;
    }

    @Override
    public int deleteSonge(String songNo) {
        return 0;
    }

    @Override
    public int deleteKrw(String krwNo) {
        return 0;
    }

    @Override
    public int deleteUser(int userNo) {
        return 0;
    }
}
