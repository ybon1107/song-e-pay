package com.sepay.backend.mail.service;

import com.sepay.backend.history.dto.HistoryDTO;

public interface MailService {
    String createVerificationCode();
    boolean sendEmail(String userId) throws Exception;
    boolean checkCode(String code);
    boolean transferTo(String userId, HistoryDTO historyDTO) throws Exception;
}