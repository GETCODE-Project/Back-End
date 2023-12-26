package com.getcode.dto;

import com.getcode.domain.member.Member;
import com.getcode.domain.member.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class signUpDto {
    private String email;
    private String nickname;
    private String password;
    private Role role;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .role(Role.ROLE_USER)
                .build();
    }

}


