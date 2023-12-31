package com.getcode.dto.member;

import com.getcode.domain.member.Authority;
import com.getcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SocialLoginRequestDto {
    private String email;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(null)
                .authority(Authority.ROLE_USER)
                .build();
    }
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, null);
    }
}
