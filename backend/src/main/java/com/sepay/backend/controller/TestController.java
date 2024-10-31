package com.sepay.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public TestController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/api/test/send-notification")
    public ResponseEntity<String> sendTestNotification(@RequestBody TestNotificationRequest request) {
        System.out.println("Received userId: " + request.getUserId());
        System.out.println("Received message: " + request.getMessage());

        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        // JSON 형식으로 메시지를 전송
        String messageJson = "{" +
                "\"notiNo\": 1," +
                "\"check\": \"0\"," +
                "\"content\": \"테스트\"," +
                "\"createdAt\": 1728707766000," +
                "\"amount\": \"0\"" +
                "}";

        messagingTemplate.convertAndSend(
                "/queue/notifications/user/" + request.getUserId(),
                messageJson
        );
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/api/test/send-notification2")
    public void sendNotification(@RequestBody TestNotificationRequest request) {
        messagingTemplate.convertAndSendToUser(
                request.getUserId(),
                "/topic/alerts",
                request.getMessage()
        );
    }
}

class TestNotificationRequest {
    private String userId;
    private String message;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}