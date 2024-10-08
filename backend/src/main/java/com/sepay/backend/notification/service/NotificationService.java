package com.sepay.backend.notification.service;

import com.sepay.backend.notification.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotification(Integer userNo);
    void saveNotification(NotificationDTO notificationDTO);
}