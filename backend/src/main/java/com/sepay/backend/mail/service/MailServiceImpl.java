package com.sepay.backend.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender emailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.emailSender = mailSender;
    }

    @Override
    public String createVerificationCode() {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int random = (int) (Math.random() * 3); // 0: 숫자, 1,2: 알파벳

            if (random == 0) {
                code.append((int) (Math.random() * 10));
            } else {
                char alphabet = (char) ((int) (Math.random() * 26) + 65);
                int upperOrLower = (int) (Math.random() * 2); // 0: 대문자, 1: 소문자
                if (upperOrLower == 0) {
                    code.append(alphabet);
                } else {
                    code.append(Character.toLowerCase(alphabet));
                }
            }
        }
        return code.toString();
    }

    @Override
    public boolean sendEmail (String userId){
        // 이메일 인증 코드 생성
        String verifyCode = createVerificationCode();

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreplysongepay@account.google.com");
            helper.setTo(userId);
            helper.setSubject("[Song-E Pay] Registration Verification Email");
            String body = "<h2>Your verification code is: </h2><h3>" + verifyCode + "</h3>";
            helper.setText(body, true); // true를 설정해서 HTML을 사용 가능하게 함
            helper.setReplyTo("noreplysongepay@account.google.com"); // 회신 불가능한 메일 주소 설정

            emailSender.send(message);
            log.info("Mail sent successfully: {}", userId);

        } catch (MessagingException e) {
            log.error("Error occurred while sending mail", e);

            return false;
        }
        return true;
    }

//    @Override
//    public boolean checkCode(String code) {
//        // 인증 코드 검증 로직
//
//        return true;
//    }
}
