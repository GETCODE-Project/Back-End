package com.getcode.config.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmail(String email, String authCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 메일 수신자
            mimeMessageHelper.setTo(email);
            // 메일 제목
            mimeMessageHelper.setSubject("이메일 인증을 위한 인증 코드 발송");
            // 메일 내용
            mimeMessageHelper.setText(setContext(authCode), true);
            // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

        } catch (MessagingException | MailSendException e) {
            log.info(e.getMessage());
            throw new IllegalArgumentException("메일주소를 확인해주세요.");
        }
    }

    // thymeleaf를 이용해 메일의 html에 인증코드를 삽입
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("verificationCodeForm", context);
    }
}
