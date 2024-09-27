package com.sepay.backend.mail.service;

import com.sepay.backend.mail.dto.MailDTO;

public interface MailService {
    void sendEmail(MailDTO mailDTO);
}
