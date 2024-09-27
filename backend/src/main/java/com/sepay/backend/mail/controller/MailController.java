package com.sepay.backend.mail.controller;

import com.sepay.backend.mail.dto.MailDTO;
import com.sepay.backend.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register/email")
public class MailController {
    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/check")
    public void sendMail() throws Exception {
        // 프론트엔드에서 입력받은 값 가져와서 메일 보내기
        String subject = "회원가입 인증 메일입니다."; // 메일 제목
        String body = "회원가입을 위한 인증 메일입니다. 인증 코드를 입력하여 회원가입을 완료해주세요.";
        String to = "yesslee31@gmail.com"; // 입력받은 user의 이메일 가져와서 넣기
        String from = "noreplysongepay@accounts.google.com";

        MailDTO mailDTO = new MailDTO(to, from, subject, body);
        mailService.sendEmail(mailDTO);
    }
}
