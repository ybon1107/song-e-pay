package com.sepay.backend.mail.service;

import com.sepay.backend.mail.dto.MailDTO;
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
    public void sendEmail(MailDTO mailDTO) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreplysongepay@account.google.com");
            helper.setTo(mailDTO.getTo());
            helper.setSubject(mailDTO.getSubject());
            helper.setText(mailDTO.getBody(), true); // true를 설정해서 HTML을 사용 가능하게 함
            helper.setReplyTo("noreplysongepay@account.google.com"); // 회신 불가능한 메일 주소 설정

            emailSender.send(message);
            log.info("Mail sent successfully: {}", mailDTO.getBody());
        } catch (MessagingException e) {
            log.error("Error occurred while sending mail", e);
            throw new RuntimeException(e);
        }
    }

    public SimpleMailMessage createEmailForm(MailDTO mailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getTo());
        message.setSubject(mailDTO.getSubject());
        message.setText(mailDTO.getBody());
        return message;
    }
}
