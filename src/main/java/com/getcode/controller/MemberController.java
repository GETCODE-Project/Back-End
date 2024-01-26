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
import com.getcode.service.member.MemberService;
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

    @Operation(summary = "회원가입", description = "이메일: 기존 이메일 형식 / 닉네임: 2자 이상 / 비밀번호: 8자 이상")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signup(@Valid @RequestBody SignUpDto signUpDto) {
        Member member = memberService.signup(signUpDto);
        SignUpResponseDto res = SignUpResponseDto.toDto(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary = "로그인", description = "로그인후, access/refresh Token 발행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberService.login(memberRequestDto));
    }

    @Operation(summary = "로그아웃", description = "Acceess Token 인증 후, 현재 로그인중인 사용자 로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @PatchMapping("/logout")
    public void logout(HttpServletRequest request) {
        String accessToken = tokenProvider.resolveAccessToken(request);
        String ref = tokenProvider.resolveRefreshToken(request);
        memberService.logout(ref, accessToken);
    }

    // 이메일 인증번호 보내기
    @Operation(summary = "이메일 인증번호 발송", description = "입력받은 이메일로 인증번호 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @PostMapping("/emails/verification-requests")
    public ResponseEntity<?> sendMessage(@RequestParam("email") @Valid String email) {
        memberService.sendCodeToEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "인증번호 인증", description = "메일을 통해 확인한 인증번호를 비교해 인증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @GetMapping("/emails/verifications")
    public ResponseEntity<EmailVerificationResultDto> verificationEmail(@RequestParam("email") @Valid String email,
                                                                        @RequestParam("code") String authCode) {
        EmailVerificationResultDto res = memberService.verifiedCode(email, authCode);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Operation(summary = "회원 정보 조회", description = "Acceess Token 인증 후, 사용자 개인정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/userInfo")
    public ResponseEntity<MemberInfoDto> userInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.userInfo());
    }

    @Operation(summary = "회원 정보 수정", description = "Acceess Token 인증 후, 사용자 개인정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @PostMapping("/update-profile")
    public ResponseEntity<S3FileUpdateDto> updateImageUrl(@RequestPart(name = "fileType") String fileType,
                                                          @RequestPart(name = "files") List<MultipartFile> multipartFiles) {
        S3FileDto file = s3Service.uploadFiles(fileType, multipartFiles).get(0);
        S3FileUpdateDto fileUrl = new S3FileUpdateDto(file.getUploadFileUrl());
        memberService.addProfile(fileUrl);
        return ResponseEntity.status(HttpStatus.OK).body(fileUrl);
    }
}
