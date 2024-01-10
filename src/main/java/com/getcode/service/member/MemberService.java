package com.getcode.service.member;

import static com.getcode.config.security.SecurityUtil.*;

import com.getcode.config.jwt.TokenDto;
import com.getcode.config.jwt.TokenProvider;
import com.getcode.config.mail.MailService;
import com.getcode.config.redis.RedisService;
import com.getcode.domain.member.Member;
import com.getcode.domain.member.RefreshToken;
import com.getcode.dto.member.EmailVerificationResultDto;
import com.getcode.dto.member.MemberInfoDto;
import com.getcode.dto.member.MemberLoginRequestDto;
import com.getcode.dto.member.SignUpDto;
import com.getcode.dto.s3.S3FileUpdateDto;
import com.getcode.exception.member.DuplicateEmailException;
import com.getcode.exception.member.DuplicateNicknameException;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.member.NotVerifiedException;
import com.getcode.repository.MemberRepository;
import com.getcode.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisService redisService;
    private final MailService mailService;

    @Value("${mail.expiration}")
    private long authCodeExpirationMills = 1800000;

    @Value("${mail.length}")
    private int length;

    @Value("${mail.chars}")
    private String characters;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    // 회원가입
    @Transactional
    public Member signup(SignUpDto signUpDto) {
        Optional<Member> memberEmail = memberRepository.findByEmail(signUpDto.getEmail());
        Optional<Member> memberNickname = memberRepository.findByNickname(signUpDto.getNickname());

        if (memberEmail.isPresent()) {
            throw new DuplicateEmailException();
        }

        if (memberNickname.isPresent()) {
            throw new DuplicateNicknameException();
        }

        Member member = signUpDto.toEntity();
        member.passwordEncoding(passwordEncoder);
        memberRepository.save(member);
        return member;
    }

    // 로그인
    @Transactional
    public TokenDto login(MemberLoginRequestDto memberRequestDto) {
        String email = memberRequestDto.getEmail();

        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 토큰 생성 메소드
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    // 로그아웃 => Http Request / Response에 대해서는 Service 계층에서 알 필요없다.
    public void logout(String refreshToken, String accessToken) {
        Claims claims = tokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        String redisRefreshToken = redisService.getValue(email);

        if (redisService.checkExistsValue(redisRefreshToken)) {
            redisService.deleteValues(email);
            long accessTokenExpirationMillis = tokenProvider.getAccessTokenExpirationMillis();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
        }
    }


    // 개인정보
    public MemberInfoDto userInfo() {
        String email = getCurrentMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

        if (!member.isEmailVerified()) {
            throw new NotVerifiedException();
        }

        return MemberInfoDto.toDto(member);
    }

    // 이메일 인증번호 보내기
    public void sendCodeToEmail(String toEmail) {
        String title = "이메일 인증 번호";
        String authCode = createCode();

        mailService.sendEmail(toEmail, title, authCode);

        redisService.setValues(AUTH_CODE_PREFIX + toEmail, authCode, Duration.ofMillis(authCodeExpirationMills));
    }

    // 이메일 인증
    @Transactional
    public EmailVerificationResultDto verifiedCode(String email, String authCode) {
        String redisAuthCode = redisService.getValue(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        if (authResult) {
            Member member = memberRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
            member.updateEmailVerified();
        }
        return EmailVerificationResultDto.toDto(authResult);
    }

    // 프로필 추가
    @Transactional
    public S3FileUpdateDto addProfile(S3FileUpdateDto request) {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        member.updateImage(request.getImageUrl());
        return request;
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
