package com.getcode.dto.member;

import com.getcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberInfoDto {
    private String email;
    private String nickname;
    protected String profileImg;

    public static MemberInfoDto toDto(Member member) {
        return new MemberInfoDto(member.getEmail(), member.getNickname(), member.getImageUrl());
    }
}
