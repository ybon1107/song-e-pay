package com.sepay.backend.notification.service;

import com.sepay.backend.notification.dto.NotificationDTO;

public interface NotificationService {
    NotificationDTO getNotification(Integer userNo);
}
