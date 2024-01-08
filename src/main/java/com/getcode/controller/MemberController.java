package com.getcode.controller;

import com.getcode.config.jwt.TokenDto;
import com.getcode.config.jwt.TokenProvider;
import com.getcode.config.redis.RedisService;
import com.getcode.config.s3.S3Service;
import com.getcode.domain.member.Member;
import com.getcode.dto.member.EmailVerificationResultDto;
import com.getcode.dto.member.MemberInfoDto;
import com.getcode.dto.member.MemberLoginRequestDto;
import com.getcode.dto.member.SignUpDto;
import com.getcode.dto.member.SignUpResponseDto;
import com.getcode.dto.s3.S3FileDto;
import com.getcode.dto.s3.S3FileUpdateDto;
import com.getcode.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "사용자 관련 API 명세")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final RedisService redisService;
    private final S3Service s3Service;
    private final TokenProvider tokenProvider;

    @Operation(summary = "일반 회원가입", description = "이메일: 기존 이메일 형식 / 닉네임: 2자 이상 / 비밀번호: 8자 이상")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signup(@Valid @RequestBody SignUpDto signUpDto) {
        Member member = memberService.signup(signUpDto);
        SignUpResponseDto res = SignUpResponseDto.toDto(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary = "일반 로그인", description = "로그인후, access/refresh Token 발행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberService.login(memberRequestDto));
    }

    @Operation(summary = "사용자 개인정보 조회", description = "Acceess Token 인증 후, 사용자 개인정보 조회")
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

    @PostMapping("/update-profile")
    public ResponseEntity<S3FileUpdateDto> updateImageUrl(@RequestPart(name = "fileType") String fileType,
                                                          @RequestPart(name = "files") List<MultipartFile> multipartFiles) {
        S3FileDto file = s3Service.uploadFiles(fileType, multipartFiles).get(0);
        S3FileUpdateDto fileUrl = new S3FileUpdateDto(file.getUploadFileUrl());
        memberService.addProfile(fileUrl);
        return ResponseEntity.status(HttpStatus.OK).body(fileUrl);
    }

    @PatchMapping("/logout")
    public String logout(HttpServletRequest request) {
        String accessToken = tokenProvider.resolveAccessToken(request);
        String ref = tokenProvider.resolveRefreshToken(request);
        memberService.logout(ref, accessToken);
        return accessToken;
    }
}
