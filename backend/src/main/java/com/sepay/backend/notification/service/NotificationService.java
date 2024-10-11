package com.sepay.backend.notification.service;

import com.sepay.backend.notification.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {

    // 알림 등록
    void saveNotification(NotificationDTO notificationDTO);

    // 알림 가져오기
    List<NotificationDTO> getNotification(String userId);

    // 알림 삭제
    int deleteNotification(Integer notiNo);

    // 알림 확인
    int readNotification(Integer notiNo);
}