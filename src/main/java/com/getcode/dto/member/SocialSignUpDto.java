package com.getcode.dto.member;

import com.getcode.domain.member.Authority;
import com.getcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SocialSignUpDto {
    private String nickname;
    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
