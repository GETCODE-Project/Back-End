package com.getcode.dto.member;

import com.getcode.domain.member.Authority;
import com.getcode.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpDto {

    @Schema(description = "이메일")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Schema(title = "닉네임", description = "최소 2자 이상 입력")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, message = "닉네임은 최소 2자 이상입니다.")
    private String nickname;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.")
    private String password;

    @Schema(description = "이메일 인증 여부")
    @NotNull(message = "이메일 인증울 진행해주세요.")
    private Boolean emailVerified;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .authority(Authority.ROLE_USER)
                .emailVerified(emailVerified)
                .build();
    }



}


