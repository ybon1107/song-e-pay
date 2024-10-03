package com.sepay.backend.mail.service;

public interface MailService {
    String createVerificationCode();
    boolean sendEmail(String userId) throws Exception;
    boolean checkCode(String code);
}
