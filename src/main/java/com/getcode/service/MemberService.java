package com.getcode.service;

import com.getcode.config.redis.RedisService;
import com.getcode.domain.member.Member;
import com.getcode.dto.EmailVerificationResultDto;
import com.getcode.dto.SignUpDto;
import com.getcode.repository.MemberRepository;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final MailService mailService;

    @Value("${mail.expiration}")
    private long authCodeExpirationMills = 1800000;

    @Value("${mail.length}")
    private int length;

    @Value("${mail.chars}")
    private String characters;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Transactional
    public Member signup(SignUpDto signUpDto) {
        Member member = signUpDto.toEntity();
        member.passwordEncoding(passwordEncoder);
        memberRepository.save(member);
        return member;
    }

    public void sendCodeToEmail(String toEmail) {
        String title = "이메일 인증 번호";
        String authCode = createCode();

        mailService.sendEmail(toEmail, title, authCode);

        redisService.setValues(AUTH_CODE_PREFIX + toEmail, authCode, Duration.ofMillis(authCodeExpirationMills));
    }

    public EmailVerificationResultDto verifiedCode(String email, String authCode) {
        String redisAuthCode = redisService.getValue(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        return EmailVerificationResultDto.toDto(authResult);
    }

    // 인증번호 생성로직
    private String createCode() {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

}
