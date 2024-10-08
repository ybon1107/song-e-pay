package com.sepay.backend.notification.mapper;

import com.sepay.backend.notification.dto.NotificationDTO;
import java.util.List;

public interface NotificationMapper {
    List<NotificationDTO> selectByUserNo(Integer userNo);
    void insertNotification(NotificationDTO notificationDTO);
}