package com.getcode.dto.member;

import com.getcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberNickNameDto {
    private String nickname;
    public static MemberNickNameDto toDto(Member member) {
        return new MemberNickNameDto(member.getNickname());
    }
}
