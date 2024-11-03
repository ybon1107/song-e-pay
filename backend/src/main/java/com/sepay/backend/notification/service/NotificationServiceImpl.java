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
        notificationDTO = mapper.selectByNo(notificationDTO.getNotiNo());



        // 웹소켓으로 알림 보내기
        // JSON 형식으로 메시지를 전송
        String messageJson = "{" +
                "\"notiNo\": "+notificationDTO.getNotiNo()+"," +
                "\"check\": \""+notificationDTO.getCheck()+"\"," +
                "\"content\": \""+notificationDTO.getContent()+"\"," +
                "\"createdAt\": "+new java.sql.Timestamp(notificationDTO.getCreatedAt().getTime()).getTime()+"," + // TIMESTAMP로 변환
                "\"amount\": "+notificationDTO.getAmount()+
                "}";
        System.out.println("---------------------------messageJson : " +messageJson);
        messagingTemplate.convertAndSend("/queue/notifications/user/" + notificationDTO.getUserId(), messageJson);
    }

    @Override
    public NotificationDTO getNotification(Integer notiNo) {
        return mapper.selectByNo(notiNo);
    }

    // 유저 알림 가져오기
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

    // 송금 받고 알림 잔액 초기화
    @Override
    public int updateNotification(Integer notiNo) {
        return mapper.updateNotiAmount(notiNo);
    }

}