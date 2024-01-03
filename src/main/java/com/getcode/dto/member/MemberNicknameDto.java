package com.getcode.dto.member;

import com.getcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberNicknameDto {
    private String nickname;
    public static MemberNicknameDto toDto(Member member) {
        return new MemberNicknameDto(member.getNickname());
    }
}
