package com.getcode.dto.study.util;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyLike;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyLikeDto {
    private Member member;
    private Study study;

    public static StudyLike toEntity(Member member, Study study) {
        return StudyLike.builder()
                .member(member)
                .study(study)
                .build();
    }
}
