package com.getcode.controller;

import com.getcode.domain.member.Member;
import com.getcode.dto.SignUpDto;
import com.getcode.dto.SignUpResponseDto;
import com.getcode.service.MailService;
import com.getcode.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signup(@Valid @RequestBody SignUpDto signUpDto) {
        Member member = memberService.signup(signUpDto);
        SignUpResponseDto res = SignUpResponseDto.toDto(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

//    @PostMapping("/emails/verification-requests")

}
