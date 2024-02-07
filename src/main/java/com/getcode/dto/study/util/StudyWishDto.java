package com.getcode.dto.study.util;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.WishStudy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyWishDto {
    private Member member;
    private Study study;

    public static WishStudy toEntity(Member member, Study study) {
        return WishStudy.builder()
                .member(member)
                .study(study)
                .build();
    }

}
