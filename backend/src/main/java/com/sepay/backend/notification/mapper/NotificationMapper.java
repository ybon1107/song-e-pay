package com.sepay.backend.notification.mapper;

import com.sepay.backend.notification.dto.NotificationDTO;

public interface NotificationMapper {
    NotificationDTO selectByUserNo(Integer userNo);
}
