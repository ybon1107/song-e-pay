package com.sepay.backend.notification.controller;

import com.sepay.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotification(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getNotification(userId));
    }

    @DeleteMapping("/{notiNo}")
    public ResponseEntity<?> deleteNotification(@PathVariable Integer notiNo) {
        return ResponseEntity.ok(notificationService.deleteNotification(notiNo));
    }

    @PatchMapping("/{notiNo}")
    public ResponseEntity<?> readNotification(@PathVariable Integer notiNo) {
        return ResponseEntity.ok(notificationService.readNotification(notiNo));
    }
}
