package com.sepay.backend.user.service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SMSService {

    private final DefaultMessageService messageService;

    // 인증번호 저장소
    private Map<String, String> verificationCodes = new HashMap<>();

    public SMSService() {
        this.messageService = NurigoApp.INSTANCE.initialize("API추가", "API추가", "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendVerificationCode(String phoneNumber, String verificationCode) {
        // 발신 메시지 설정
        Message message = new Message();
        message.setFrom("01022116854"); // 발신자 번호
        message.setTo(phoneNumber); // 수신자 번호
        message.setText("[Song-e]인증번호는 " + verificationCode);

        // 인증번호를 저장 (실제 서비스에서는 DB나 Redis를 사용)
        verificationCodes.put(phoneNumber, verificationCode);
        System.out.println("Stored verification code for " + phoneNumber + ": " + verificationCode); // 로그 추가

        // 메시지 전송
        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }


    // 인증번호 확인
    public boolean verifyCode(String phoneNumber, String code) {
        String storedCode = verificationCodes.get(phoneNumber);
        System.out.println("Stored code for " + phoneNumber + ": " + storedCode); // 로그 추가
        System.out.println("Provided code: " + code); // 로그 추가
        return storedCode != null && storedCode.equals(code);
    }

    // 6자리 인증번호 생성
    public String generateVerificationCode() {
        return String.format("%06d", (int)(Math.random() * 1000000));  // 6자리 랜덤 번호 생성
    }
}
