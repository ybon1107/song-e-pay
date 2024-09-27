package com.sepay.backend.mail.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailDTO {
    private String to;
    private String from;
    private String subject;
    private String body;
}
