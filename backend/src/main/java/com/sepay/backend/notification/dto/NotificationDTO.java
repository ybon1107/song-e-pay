package com.sepay.backend.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Integer notiNo;
    private Integer resNo;
    private Integer historyNo;
    private String userId;
    private String content;
    private Integer amount;
    private char check;
    private Date createdAt;
}
