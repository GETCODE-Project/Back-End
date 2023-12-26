package com.getcode.controller;

import com.getcode.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final MailService mailService;

    @GetMapping("/send-email")
    public String sendEmail() {
        String to = "kyun9151@naver.com";
        String subject = "Test Email with Attachment";
        String text = "안녕!";

        try {
            mailService.sendEmail(to, subject, text);
            return "Email sent successfully!";
        } catch (Exception e) {
            return "Error sending email: " + e.getMessage();
        }
    }
}
