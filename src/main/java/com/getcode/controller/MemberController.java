package com.getcode.controller;

import com.getcode.config.jwt.TokenDto;
import com.getcode.config.redis.RedisService;
import com.getcode.domain.member.Member;
import com.getcode.dto.member.EmailVerificationResultDto;
import com.getcode.dto.member.MemberInfoDto;
import com.getcode.dto.member.MemberLoginRequestDto;
import com.getcode.dto.member.SignUpDto;
import com.getcode.dto.member.SignUpResponseDto;
import com.getcode.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final RedisService redisService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signup(@Valid @RequestBody SignUpDto signUpDto) {
        Member member = memberService.signup(signUpDto);
        SignUpResponseDto res = SignUpResponseDto.toDto(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberService.login(memberRequestDto));
    }

    @GetMapping("/userInfo")
    public ResponseEntity<MemberInfoDto> userInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.userInfo());
    }

    @GetMapping("/redis")
    public String redisTest() {
        redisService.setValues("key", "1");
        return "aa";
    }

    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") @Valid String email) {
        memberService.sendCodeToEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/emails/verifications")
    public ResponseEntity<EmailVerificationResultDto> verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("code") String authCode) {
        EmailVerificationResultDto res = memberService.verifiedCode(email, authCode);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
