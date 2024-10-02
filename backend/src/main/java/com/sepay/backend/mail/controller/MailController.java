package com.sepay.backend.mail.controller;

import com.sepay.backend.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class MailController {
    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/email")
    public boolean sendMail(@RequestBody Map<String, String> request) throws Exception {
        String userId = request.get("userId");
        return mailService.sendEmail(userId);
    }

    @PostMapping("/check")
    public boolean checkCode(@RequestBody Map<String, String> request) throws Exception {
        String code = request.get("code");
        return mailService.checkCode(code);
    }
}
