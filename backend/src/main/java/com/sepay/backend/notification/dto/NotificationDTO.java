package com.sepay.backend.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Integer notiNo;
    private String userId;
    private Integer resveNo;
    private String notiContent;
    private Double exchangeRate;
    private Date createAt;
}
