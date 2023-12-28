package com.getcode.config.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthMemberProfile {

    private String email;

    private String nickname;

    private String name;

    private String provider;

    public AuthMember toMember() {
        return AuthMember.builder()
                .email(email)
                .nickname(nickname)
                .name(name)
                .provider(provider)
                .build();
    }
}
