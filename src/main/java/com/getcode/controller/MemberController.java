package com.getcode.controller;

import com.getcode.config.jwt.TokenDto;
import com.getcode.config.redis.RedisService;
import com.getcode.domain.member.Member;
import com.getcode.dto.member.EmailVerificationResultDto;
import com.getcode.dto.member.MemberInfoDto;
import com.getcode.dto.member.MemberLoginRequestDto;
import com.getcode.dto.member.SignUpDto;
import com.getcode.dto.member.SignUpResponseDto;
import com.getcode.exception.common.BusinessExceptionResponse;
import com.getcode.exception.member.DuplicateEmailException;
import com.getcode.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 관련 API 명세")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final RedisService redisService;

    @Operation(summary = "일반 회원가입", description = "이메일: 기존 이메일 형식 / 닉네임: 2자 이상 / 비밀번호: 8자 이상")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "404", description = "특정 유저가 존재하지 않음",
                    content = @Content(schema = @Schema(implementation = BusinessExceptionResponse.class)))
    })
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

    @Operation(summary = "사용자 개인 정보회회", description = "Acceess Token 인증 후, 사용자 개인정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted"),
    })
    @GetMapping("/userInfo")
    public ResponseEntity<MemberInfoDto> userInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.userInfo());
    }

    // 이메일 인증번호 보내기
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
