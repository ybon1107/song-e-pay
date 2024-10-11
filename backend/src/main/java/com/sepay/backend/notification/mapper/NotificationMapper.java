package com.sepay.backend.notification.mapper;

import com.sepay.backend.notification.dto.NotificationDTO;
import java.util.List;

public interface NotificationMapper {

    // 알림 등록
    void insertNotification(NotificationDTO notificationDTO);

    // 알림 가져오기
    List<NotificationDTO> selectByUserId(String userId);

    // 알림 삭제
    int deleteNoti(Integer notiNo);

    // 알림 확인
    int updateNoti(Integer notiNo);
}