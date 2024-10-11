package com.sepay.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void sendTestNotification(@RequestBody TestNotificationRequest request) {
        System.out.println("Received userId: " + request.getUserId());
        System.out.println("Received message: " + request.getMessage());

        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        messagingTemplate.convertAndSendToUser(
                request.getUserId(),
                "/topic/notifications",
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