package com.getcode.dto;

import com.getcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignUpResponseDto {
    private String email;
    private String nickname;


    public static SignUpResponseDto toDto(Member member) {
        return new SignUpResponseDto(member.getEmail(), member.getNickname());
    }
}
