package com.sepay.backend.notification.service;

import com.sepay.backend.notification.dto.NotificationDTO;
import com.sepay.backend.notification.mapper.NotificationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper mapper;

    @Override
    public List<NotificationDTO> getNotification(Integer userNo) {
        return mapper.selectByUserNo(userNo);
    }

    @Override
    public void saveNotification(NotificationDTO notificationDTO) {
        mapper.insertNotification(notificationDTO);
    }
}