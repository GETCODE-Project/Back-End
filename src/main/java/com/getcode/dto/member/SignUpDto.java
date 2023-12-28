package com.getcode.dto.member;

import com.getcode.domain.member.Authority;
import com.getcode.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    private Authority authority;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .authority(Authority.ROLE_USER)
                .emailVerified(false)
                .build();
    }



}


