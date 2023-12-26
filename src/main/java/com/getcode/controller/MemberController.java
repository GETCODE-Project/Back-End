package com.getcode.controller;

import com.getcode.config.redis.RedisService;
import com.getcode.domain.member.Member;
import com.getcode.dto.SignUpDto;
import com.getcode.dto.SignUpResponseDto;
import com.getcode.service.MailService;
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
}
