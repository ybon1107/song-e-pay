package com.sepay.backend.mail.service;

import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.history.mapper.HistoryMapper;
import com.sepay.backend.history.service.HistoryService;
import com.sepay.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.sepay.backend.myaccount.mapper.MyAccountMapper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final MyAccountMapper mapper;
    private final JavaMailSender emailSender;
    private final UserService userService;
    private final HistoryMapper historyMapper;

    private String verificationCode; // 인증 코드를 저장할 필드
    private LocalDateTime codeGeneratedTime; // 인증 코드 생성 시간을 저장할 필드
    private static final long CODE_VALID_DURATION = 3; // 인증 코드 유효 시간: 3분(분단위)

    @Override
    public String createVerificationCode() {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append((int) (Math.random() * 10));
        }
        return code.toString();
    }

    @Override
    public boolean sendEmail (String userId){
        // 이메일 중복 확인
        if (userService.isEmailRegistered(userId)) {
            log.info("Email already registered: {}", userId);
            return false;
        }

        // 이메일 인증 코드 생성
        this.verificationCode = createVerificationCode();
        this.codeGeneratedTime = LocalDateTime.now(); // 인증 코드 생성 시간 저장
        log.info("Generated verification code: {}", this.verificationCode); // 생성된 인증 코드 로그 출력

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreplysongepay@account.google.com");
            helper.setTo(userId);
            helper.setSubject("[Song-E Pay] Registration Verification Email");
            String body = "<h2>Your verification code is: </h2><h3>" + verificationCode + "</h3>";
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

    @Override
    public boolean checkCode(String code) {
        // 전달받은 코드와 저장된 인증코드 비교
        log.info("Verification code check: {}", code);
        log.info("Verification code: {}", this.verificationCode);

        // 인증 코드 생성 시간과 현재 시간을 비교해서 인증 코드 유효 시간 확인
        if (this.verificationCode != null && this.verificationCode.equals(code)) {
            Duration duration = Duration.between(this.codeGeneratedTime, LocalDateTime.now());
            if (duration.toMinutes() <= CODE_VALID_DURATION) {
                log.info("Verification successful");
                return true;
            } else {
                log.info("Verification failed: Code expired");
            }
        } else {
            log.info("Verification failed: Invalid code");
        }
        return false;
    }

    @Override
    public boolean transferTo (String userId, HistoryDTO historyDTO){
        try {
            // 보내는 사람
            historyMapper.insertHistory(historyDTO);

            String transferFromUserId = historyDTO.getUserId();
            Double transferAmount = historyDTO.getAmount();
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreplysongepay@account.google.com");
            helper.setTo(userId);
            helper.setSubject("[Song-E Pay] Transaction Notification");
            String body = "<div style=\"font-family: Arial, sans-serif; color: #333; line-height: 1.6;\">"
                    + "<h2>Thank you for your interest in our service, Song-E Pay!</h2>"
                    + "<p>We appreciate your interest in Song-E Pay.</p>"
                    + "<p>Dear user, <strong>" + transferFromUserId + "</strong> has transferred an amount of <strong>₩ "
                    + transferAmount + "</strong> to <strong>" + userId + "</strong> using our service.</p>"
                    + "<p>To claim this amount, please complete the registration process using your email, <strong>" + userId + "</strong>.</p>"
                    + "<div style=\"text-align: center; margin: 20px;\">"
                    + "<a href=\"http://localhost:5173/register/legal\" style=\"background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px;\">"
                    + "Complete Registration"
                    + "</a>"
                    + "</div>"
                    + "<p>For security reasons, please keep this information confidential and do not share it with others.</p>"
                    + "<p>Best regards,<br>Song-E Pay Team</p>"
                    + "<hr>"
                    + "<p style=\"font-size: 12px; color: #777;\">This is an automated message, please do not reply.</p>"
                    + "</div>";


            helper.setText(body, true); // HTML로 내용을 설정
            helper.setReplyTo("noreplysongepay@account.google.com"); // 회신 불가능한 메일 주소 설정

            emailSender.send(message);
            log.info("Mail sent successfully: {}", userId);
        } catch (MessagingException e) {
            log.info("userId : ", userId);
            log.error("Error occurred while sending mail", e);

            return false;
        }
        return true;
    }
}
