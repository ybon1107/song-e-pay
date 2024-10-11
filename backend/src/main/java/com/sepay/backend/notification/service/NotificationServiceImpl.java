package com.sepay.backend.notification.service;

import com.sepay.backend.notification.dto.NotificationDTO;
import com.sepay.backend.notification.mapper.NotificationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper mapper;

    // 알림 저장
    @Override
    public void saveNotification(NotificationDTO notificationDTO) {
        mapper.insertNotification(notificationDTO);
    }

    // 알림 가져오기
    @Override
    public List<NotificationDTO> getNotification(String userId) {
        return mapper.selectByUserId(userId);
    }

    // 알림 삭제
    @Override
    public int deleteNotification(Integer notiNo) {
        return mapper.deleteNoti(notiNo);
    }

    // 알림 확인
    @Override
    public int readNotification(Integer notiNo) {
        return mapper.updateNoti(notiNo);
    }

    // 웹소켓
    public void notifyUser(int userId) {
        // 특정 사용자에게 알림 전송
        String destination = "/topic/alerts/" + userId;
        messagingTemplate.convertAndSend(destination);
    }

}